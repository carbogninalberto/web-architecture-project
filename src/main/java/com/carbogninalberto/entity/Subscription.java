package com.carbogninalberto.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class Subscription implements Serializable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    private int sid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String last;
    @Column(nullable = false)
    private Date born;
    @Column(nullable = false)
    private long timestamp;
    @Column(nullable = false)
    private float price;

    @ManyToOne
    private SportAssociation association;
    @ManyToOne
    private Utente utente;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public SportAssociation getAssociation() {
        return association;
    }

    public void setAssociation(SportAssociation association) {
        this.association = association;
    }
}
