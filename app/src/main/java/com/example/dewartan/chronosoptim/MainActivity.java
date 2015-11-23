package com.example.dewartan.chronosoptim;




import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        //Setup Toolbar
        actionBarToolBar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) actionBarToolBar.findViewById(R.id.toolbar_title);
        title.setText("Chronos Optim");
        setSupportActionBar(actionBarToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Channels"));

        //RecyclerView Adapters
        adapter = new EventAdapter(this);
        cAdapter = new ChannelAdapter(this);

        //Database Adapters
        eventDBAdapter = new EventDBAdapter(this);
        channelDBAdapter = new ChannelDBAdapter(this);

        recyclerView.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        showToast("One");
                        recyclerView.setAdapter(adapter);
                        break;
                    case 1:
                        recyclerView.setAdapter(cAdapter);
                        showToast("Two");
                        break;
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

