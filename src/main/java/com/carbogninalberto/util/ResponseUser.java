package com.carbogninalberto.util;

import com.carbogninalberto.entity.Utente;

import java.io.Serializable;

public class ResponseUser extends Response implements Serializable {
    Utente utente;

    public ResponseUser(String msg, Utente utente) {
        super(msg);
        this.utente = utente;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
