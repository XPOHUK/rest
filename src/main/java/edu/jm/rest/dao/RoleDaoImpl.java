package edu.jm.rest.dao;

import edu.jm.rest.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> query = entityManager.createQuery("select r from Role r", Role.class);
        return query.getResultList();
    }

    @Override
    public void add(Role role){
        entityManager.persist(role);
    }

    @Override
    public Role getRoleByName(String name){
        TypedQuery<Role> query = entityManager.createQuery("select r from Role r where r.role = :name", Role.class);
        query.setParameter("name", name);
        List results = query.getResultList();
        if (results.isEmpty())
            return null;
        return (Role) results.get(0);
    }
}
