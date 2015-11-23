package com.example.dewartan.chronosoptim;




import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, View.OnClickListener, ActionMode.Callback {

    private EventDBAdapter eventDBAdapter;
    private ChannelDBAdapter channelDBAdapter;
    private static EventAdapter adapter;
    private ChannelAdapter cAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Toolbar actionBarToolBar;
    private Button feedBtn, eventBtn;
    int add_event_code = 1;
    int add_channel_code = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        actionBarToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBarToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        adapter = new EventAdapter(this);
        cAdapter = new ChannelAdapter(this);

        eventDBAdapter = new EventDBAdapter(this);
        channelDBAdapter = new ChannelDBAdapter(this);
        ArrayList<Event> databaseEvents = eventDBAdapter.getAllEvents();

        recyclerView.setAdapter(adapter);

        feedBtn = (Button) findViewById(R.id.showfeeds);
        eventBtn = (Button) findViewById(R.id.showeventslist);

        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(cAdapter);

            }
        });

        eventBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(adapter);
            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.create_event) {
            startActivityForResult(new Intent(this, AddEvent.class), add_event_code);
            return true;
        }

        if(id == R.id.create_channel) {
            startActivityForResult(new Intent(this, AddChannel.class), add_channel_code);
            return true;
        }

        if(id == R.id.clear_database) {
            eventDBAdapter.clearDatabase();
            channelDBAdapter.clearDatabase();
            cAdapter = new ChannelAdapter(this);
            adapter = new EventAdapter(this);
            recyclerView.setAdapter(adapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == add_event_code) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), "Sucessfully added", Toast.LENGTH_SHORT).show();
                adapter = new EventAdapter(this);
                recyclerView.setAdapter(adapter);

            }
        }
        if (requestCode == add_channel_code) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), "Sucessfully added", Toast.LENGTH_SHORT).show();
                Log.d("worked", "cjknclkjn");
                cAdapter = new ChannelAdapter(this);
                recyclerView.setAdapter(cAdapter);
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Event eventOnDay = (Event) adapter.getItem(item.getItemId());
        Intent i = new Intent(getBaseContext(), IndividualEventView.class);
        i.putExtra("viewEvent", eventOnDay);
        Toast.makeText(getBaseContext(), eventOnDay.getTitle(), Toast.LENGTH_SHORT).show();
        startActivity(i);
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

