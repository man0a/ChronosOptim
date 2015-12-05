package com.example.dewartan.chronosoptim;

public interface ClientDevice{
    void uponSync(String response);
    void setLocalId(String userId);
    String getLocalId();
}