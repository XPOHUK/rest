package edu.jm.rest.service;



import edu.jm.rest.model.User;
import edu.jm.rest.model.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    void createUser(UserDto user);
    List<User> listUsers();
    void removeUser(User user);
    void updateUser(User user);
    User getUserById(long id);
    Map<String, String> validateUser(UserDto user);
}
