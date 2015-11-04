package com.example.dewartan.chronosoptim;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by dewartan on 10/29/15.
 */
public class EventView extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_event);

        Event eventOnDay = this.getIntent().getParcelableExtra("viewEvent");

        TextView testing = (TextView) findViewById(R.id.test123);
        if (eventOnDay != null){
            testing.setText(eventOnDay.getTitle());
        }

    }


}

