package com.carbogninalberto.itf;

import java.util.logging.Logger;

public interface Logging {
    default Logger getLogger() {
        return Logger.getLogger(String.valueOf(getClass()));
    }
}
