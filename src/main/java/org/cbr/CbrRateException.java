package org.cbr;


public class CbrRateException extends Exception {

    public CbrRateException(String message, String... date) {
        super(String.format(message, date));
    }
}
