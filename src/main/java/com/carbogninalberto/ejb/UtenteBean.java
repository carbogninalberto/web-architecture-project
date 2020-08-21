package com.carbogninalberto.ejb;


import com.carbogninalberto.entity.Utente;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class UtenteBean implements Serializable {

    @PersistenceContext(name = "persistenceJPA")
    EntityManager manager;

    public List<Utente> listUsers() {
        Query query = manager.createQuery("SELECT a FROM Utente a");
        return query.getResultList();
    }

    public Utente addUtente(Utente utente) {
        manager.persist(utente);

        return utente;
    }

    public Utente addUtente(String email, String name, String password) {
        Utente utente = new Utente();
        utente.setEmail(email);
        utente.setName(name);
        utente.setPassword(password);
        manager.persist(utente);

        return utente;
    }

    public Utente getUtente(String email) {
        Utente utente = manager.find(Utente.class, email);
        return utente;
    }

    public void deleteUtente(String email) {
        Utente utente = manager.find(Utente.class, email);
        manager.remove(utente);
    }
}
