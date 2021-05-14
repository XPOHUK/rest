package edu.jm.rest.controller;

import edu.jm.rest.model.Role;
import edu.jm.rest.model.User;
import edu.jm.rest.model.UserDto;
import edu.jm.rest.service.RoleService;
import edu.jm.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest")
public class MyRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MyRestController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(UsernamePasswordAuthenticationToken token, ModelMap model){
        User user1 = (User) token.getPrincipal();
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.removeUser(userService.getUserById(id));
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDto newUser){
        newUser.setRoles(new HashSet<>(newUser.getRoles().stream()
                .map(role -> roleService.getRoleByName(role.getRole()))
                .collect(Collectors.toList())));
        return new ResponseEntity<>(userService.createUser(newUser), HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto editedUser, @PathVariable Long id){
        User persUser = userService.getUserById(id);
        persUser.setFirstName(editedUser.getFirstName());
        persUser.setLastName(editedUser.getLastName());
        persUser.setAge(editedUser.getAge());
        if (!editedUser.getPassword().isEmpty()){
            persUser.setPassword(userService.getPasswordEncoder().encode(editedUser.getPassword()));
        }
        persUser.setRoles(new HashSet<>(editedUser.getRoles().stream()
                .map(role -> roleService.getRoleByName(role.getRole()))
                .collect(Collectors.toList())));
        return new ResponseEntity<>(userService.updateUser(persUser), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(){
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }
}
