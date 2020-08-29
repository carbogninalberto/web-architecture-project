package com.carbogninalberto.ejb;

import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.UserInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

@Stateless
@LocalBean
public class UtenteBean implements Serializable, Logging {

    @PersistenceContext(name = "persistenceJPA")
    EntityManager manager;

    /**
     * List of all users
     * @return
     */
    public List<Utente> listUsers() {
        Query query = manager.createQuery("SELECT a FROM Utente a");
        return query.getResultList();
    }

    /**
     * add user giving the User object
     * @param utente
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public Utente addUtente(Utente utente) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // generating the salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // generating hashing
        byte[] hashedPassword = generateHash(utente.getPassword(), salt);

        // updating salt param
        utente.setSalt(Base64.getEncoder().encodeToString(salt));
        // updating password param
        utente.setPassword(Base64.getEncoder().encodeToString(hashedPassword));

        manager.persist(utente);

        return utente;
    }

    /**
     * addUser giving some params.
     * @param email
     * @param name
     * @param password
     * @return
     */
    public Utente addUtente(String email, String name, String password) {
        Utente utente = new Utente();
        utente.setEmail(email);
        utente.setName(name);
        utente.setPassword(password);
        manager.persist(utente);

        return utente;
    }

    /**
     * get User given unique field email
     * @param email
     * @return
     */
    public Utente getUtente(String email) {
        Utente utente = (Utente) manager.createQuery("SELECT t FROM Utente t WHERE t.email = :email").setParameter("email", email).getSingleResult();
        return utente;
    }

    /**
     * Delete User given unique field email
     * @param email
     */
    public void deleteUtente(String email) {
        Utente utente = manager.find(Utente.class, email);
        manager.remove(utente);
    }

    /**
     * Generate Hash Given Password and salt
     * @param password
     * @param salt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private byte[] generateHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65546, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
    }

    /**
     * Return false if the inserted password is not matching
     * @param utente
     * @return
     */
    public UserInfo checkPasswordUtente(Utente utente) {
        UserInfo result = new UserInfo(false);

        // getting user in the database
        Utente tmpUtente = getUtente(utente.getEmail());
        result.setUtente(tmpUtente);
        // check if a user was found
        if (tmpUtente != null) {
            try {
                // rebuilding hash for checking the password match
                byte[] salt = Base64.getDecoder().decode(tmpUtente.getSalt());
                byte[] hashedPassword = generateHash(utente.getPassword(), salt);
                result.setLogged(Base64.getEncoder().encodeToString(hashedPassword).equals(tmpUtente.getPassword()));
            } catch (Exception e) {
                getLogger().warning("Exception: " + e.getMessage());
            }
        }

        return result;
    }
}
