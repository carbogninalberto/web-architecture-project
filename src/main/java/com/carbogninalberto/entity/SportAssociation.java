package com.carbogninalberto.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class SportAssociation implements Serializable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    private int id;
    @Column(nullable = false)
    private String name;

    @OneToOne
    private Utente utente;

    public SportAssociation() {
    }

    public SportAssociation(String name, Utente utente) {
        this.name = name;
        this.utente = utente;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
