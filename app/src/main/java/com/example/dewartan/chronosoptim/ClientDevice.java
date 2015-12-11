package com.example.dewartan.chronosoptim;

import java.util.LinkedList;

public interface ClientDevice{
    void uponSync(String response);
    void setLocalId(String userId);
    String getLocalId();
    void coverActions(LinkedList<String> actions);
    LinkedList<String> recoverActions();
}