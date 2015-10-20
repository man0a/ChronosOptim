package com.example.dewartan.chronosoptim;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class EventsView extends ListActivity {

    private DateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DateAdapter(this);
        // Assign the adapter to this ListActivity
        setListAdapter(adapter);

        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (adapter.getItemViewType(position) == 0) {  //Excludes section header from being clickable
                    Toast.makeText(getBaseContext(), "Id = -1: " + id, Toast.LENGTH_SHORT).show();
                    adapter.myCliquedPosition = position;
                    Log.d("debug", "my pos in activity" + adapter.myCliquedPosition);
                    adapter.notifyDataSetInvalidated();
                }
            }
        });
    }




}
