package com.example.dewartan.chronosoptim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private DbHelper dbHelper;
    private EventListAdapter adapter;
    private TeamListAdapter cAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private Toolbar actionBarToolBar;
//    private Button feedBtn, eventBtn;
    int add_event_code = 1;
    int add_team_code = 2;
    private TextView title;
//    private ItemTouchHelper itemTouchHelper;
    private Context context= this;

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

        //Setup the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Teams"));

        //RecyclerView Adapters
        adapter = new EventListAdapter(this);
        cAdapter = new TeamListAdapter(this);
        dbHelper = new DbHelper(this);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //Prevent swipe on headers
            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int position = viewHolder.getAdapterPosition();
                if (adapter.getItem(position) == null) return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {return false;}

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setMessage("Are you sure that you want to remove it?");

                    //When the user selects remove
                    alertDialogBuilder.setPositiveButton("remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Event event = (Event) adapter.getItem(position);
                            String id=dbHelper.getRowid(event.getTitle());
                            dbHelper.deleteEvent(Integer.parseInt(id));
                            adapter = new EventListAdapter(context);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                    // When the user selects cancel
                    alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearView(recyclerView, viewHolder);
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
        };



        //Setting the Itemtouch helper to the recyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        //Listener for Tabs
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        recyclerView.setAdapter(adapter);
                        break;
                    case 1:
                        recyclerView.setAdapter(cAdapter);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
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
        if(id == R.id.createEvent) {
            startActivityForResult(new Intent(this, AddEventActivity.class), add_event_code);
            return true;
        }
        if(id == R.id.createTeam) {
            startActivityForResult(new Intent(this, AddTeamActivity.class), add_team_code);
            return true;
        }
        if(id == R.id.clearDatabase) {
            eventDbHelper.clearDatabase();
            teamDbHelper.clearDatabase();
            cAdapter = new TeamListAdapter(this);
            adapter = new EventListAdapter(this);
            recyclerView.setAdapter(adapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK){
            return;
        }
        Toast.makeText(getBaseContext(), "Successfully added", Toast.LENGTH_SHORT).show();

        if (requestCode == add_event_code) {
            adapter = new EventListAdapter(this);
            recyclerView.setAdapter(adapter);
        }else if(requestCode == add_team_code) {
            cAdapter = new TeamListAdapter(this);
            recyclerView.setAdapter(cAdapter);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}

