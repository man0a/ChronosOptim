package com.example.dewartan.chronosoptim;

import java.util.ArrayList;

public interface ClientDevice{
    void uponSync(String response);
    void setLocalId(String userId);
    String getLocalId();
    void coverActions(ArrayList<String> actions);
    ArrayList<String> recoverActions();
    SyncBuffer getBuffer();
}