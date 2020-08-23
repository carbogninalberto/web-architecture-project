package com.carbogninalberto.util;

import com.carbogninalberto.entity.Utente;

public class UserInfo {
    private boolean logged;
    private Utente utente;

    public UserInfo(boolean logged) {
        this.logged = logged;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
