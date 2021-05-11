package edu.jm.rest.dao;

import edu.jm.rest.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public User add(User user) {
      entityManager.persist(user);
      return user;
   }

   @Override
   public List<User> listUsers() {
      TypedQuery<User> query=entityManager.createQuery("select distinct u from User u LEFT JOIN FETCH u.roles roles", User.class);
      return query.getResultList();
   }

   @Override
   public User updateUser(User user){
      return entityManager.merge(user);
   }

   @Override
   public void removeUser(User user){
      entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
   }

   @Override
   public User getUserById(long id){
      TypedQuery<User> query = entityManager.createQuery("select u from User u JOIN FETCH u.roles where u.id = :id",
              User.class).setParameter("id", id);
      return query.getSingleResult();
   }

   public UserDetails getUserByEmail(String email){
      TypedQuery<User> query = entityManager.createQuery("select u from User u JOIN FETCH u.roles where u.email = :email ", User.class)
              .setParameter("email", email);
      List results = query.getResultList();
      if (results.isEmpty())
         return null;
      return (User) results.get(0);
   }
}
