package edu.jm.rest.dao;


import edu.jm.rest.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserDao {
   User add(User user);
   List<User> listUsers();
   User getUserById(long id);
   void updateUser(User user);
   void removeUser(User user);
   UserDetails getUserByEmail(String email);
}
