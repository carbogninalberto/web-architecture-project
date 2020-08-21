package com.carbogninalberto.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Utente implements Serializable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    @Column(nullable = false)
    private int uid;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean admin;
    @Column(nullable = false)
    private long timestamp;

    public Utente() {
        this.timestamp = (int) System.currentTimeMillis();
        this.admin = false;
    }
    public Utente(int userId, boolean admin) {
        this.uid = userId;
        this.timestamp = (int) System.currentTimeMillis();
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}