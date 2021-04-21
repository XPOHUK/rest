package edu.jm.rest.controller;

import edu.jm.rest.model.Role;
import edu.jm.rest.model.User;
import edu.jm.rest.model.UserDto;
import edu.jm.rest.service.RoleService;
import edu.jm.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getUsers(){
        return userService.listUsers();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        System.out.println("User for del " + id);
        userService.removeUser(userService.getUserById(id));
    }

    @PostMapping("/users")
    public User createUser(@RequestBody UserDto newUser){
        //TODO Добавить валидацию через userService.validateUser
        System.out.println(newUser);
        newUser.setRoles(new HashSet<>(newUser.getRoles().stream()
                .map(role -> roleService.getRoleByName(role.getRole()))
                .collect(Collectors.toList())));
        return userService.createUser(newUser);
    }

    @GetMapping("/roles")
    public List<Role> getRoles(){
        return roleService.getAllRoles();
    }
}
