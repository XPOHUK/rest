package edu.jm.rest.dao;



import edu.jm.rest.model.Role;

import java.util.List;

public interface RoleDao {
    List<Role> getAllRoles();
    void add(Role role);
    Role getRoleByName(String name);
}
