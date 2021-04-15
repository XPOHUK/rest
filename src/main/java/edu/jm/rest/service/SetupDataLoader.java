package edu.jm.rest.service;


import edu.jm.rest.dao.RoleDao;
import edu.jm.rest.model.Role;
import edu.jm.rest.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    public SetupDataLoader(){
        super();
        System.out.println("Bean Listener created");
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event){

        if (alreadySetup)
            return;
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        Role adminRole = roleDao.getRoleByName("ROLE_ADMIN");
        UserDto user = new UserDto();
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setEmail("admin@admin.com");
        user.setAge(100);
        user.setRoles(new HashSet<>(Arrays.asList(adminRole)));
        user.setPassword("admin");
        userService.createUser(user);
        alreadySetup = true;
    }

    @Transactional
    void createRoleIfNotFound(String name){
        Role role = roleDao.getRoleByName(name);
        if (role == null){
            role = new Role(name);
            roleDao.add(role);
        }
    }
}
