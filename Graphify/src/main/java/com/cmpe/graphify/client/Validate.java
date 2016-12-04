package com.cmpe.graphify.client;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 28-11-2016.
 */
@XmlRootElement
public class Validate {
    private boolean isValid;
    private String message;

    public Validate(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String isMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Validate{" +
                "isValid=" + isValid +
                ", message='" + message + '\'' +
                '}';
    }
}
