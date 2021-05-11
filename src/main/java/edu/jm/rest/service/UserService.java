package edu.jm.rest.service;



import edu.jm.rest.model.User;
import edu.jm.rest.model.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

public interface UserService {
    User createUser(UserDto user);
    List<User> listUsers();
    void removeUser(User user);
    User updateUser(User user);
    User getUserById(long id);
    Map<String, String> validateUser(UserDto user);
    BCryptPasswordEncoder getPasswordEncoder();
}
