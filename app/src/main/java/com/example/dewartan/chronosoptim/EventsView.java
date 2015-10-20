package com.example.dewartan.chronosoptim;

import android.app.ListActivity;
import android.os.Bundle;


public class EventsView extends ListActivity {

    private DateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DateAdapter(this);

        // Assign the adapter to this ListActivity
        setListAdapter(adapter);


    }


}
