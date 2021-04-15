package edu.jm.rest.service;


import edu.jm.rest.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String name);
}
