package edu.jm.rest.service;


import edu.jm.rest.dao.RoleDao;
import edu.jm.rest.dao.UserDao;
import edu.jm.rest.model.User;
import edu.jm.rest.model.UserDto;
import edu.jm.rest.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public User createUser(UserDto userDto) {
        User user = new User(userDto);
        if (userDto.getRoles() == null) {
            user.setRoles(new HashSet<>(Arrays.asList(roleDao.getRoleByName("ROLE_USER"))));
        } else {
            user.setRoles(userDto.getRoles());
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        return userDao.add(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Override
    public void removeUser(User user) {
        userDao.removeUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public Map<String, String> validateUser(UserDto userDto) {
        Map<String, String> errorMap = new HashMap<>();
        if (userDto.getEmail().isEmpty())
            errorMap.put("emailIsEmpty", "E-Mail Is Empty!");
        else if (!EmailValidator.validateEmail(userDto.getEmail()))
            errorMap.put("emailNotValid", "Email Not Valid!");
        else if (userDao.getUserByEmail(userDto.getEmail()) != null) {
            errorMap.put("userExists", "An account for that email already exists.");
        }
        if (userDto.getFirstName().isEmpty())
            errorMap.put("firstNameIsEmpty", "First Name Is Empty!");
        if (userDto.getLastName().isEmpty())
            errorMap.put("lastNameIsEmpty", "Last Name Is Empty!");
        if (userDto.getPassword().isEmpty())
            errorMap.put("passwordIsEmpty", "Password Is Empty!");
        // TODO Добавить валидацию возраста. Должно быть int, больше 0 и меньше 129
        return errorMap;
    }

    public BCryptPasswordEncoder getPasswordEncoder(){
        return this.passwordEncoder;
    }
}
