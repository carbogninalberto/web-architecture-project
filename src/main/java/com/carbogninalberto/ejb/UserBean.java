package com.carbogninalberto.ejb;


import com.carbogninalberto.entity.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
@LocalBean
public class UserBean {

    @PersistenceContext(unitName = "persistenceJPA")
    EntityManager manager;

    public List<User> listUsers() {
        Query query = manager.createQuery("SELECT a FROM User a");
        return query.getResultList();
    }

    public User addUser(String email, String name, String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        manager.persist(user);

        return user;
    }

    public User getUser(String email) {
        User user = manager.find(User.class, email);
        return user;
    }

    public void deleteUser(String email) {
        User user = manager.find(User.class, email);
        manager.remove(user);
    }
}
