package com.example.dewartan.chronosoptim;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.parse.ParseAnalytics;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class MainActivity extends ClientDevice implements RecyclerView.OnItemTouchListener {

    private SyncBuffer syncBuffer;
    private DbHelper dbHelper;
    private EventListAdapter evAdapter;
    private TeamListAdapter teamAdapter;
    private RecyclerView recyclerView;
    public static final int add_event_code = 1;
    public static final int add_team_code = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        // SyncBuffer
        syncBuffer=new SyncBuffer(this);

        // RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // Toolbar
        Toolbar actionBarToolBar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) actionBarToolBar.findViewById(R.id.toolbar_title);
        title.setText(this.getString(R.string.app_name));
        setSupportActionBar(actionBarToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(this.getString(R.string.events)));
        tabLayout.addTab(tabLayout.newTab().setText(this.getString(R.string.teams)));

        // DbHelper and RecyclerView adapters
        dbHelper=new DbHelper(this,syncBuffer);
        evAdapter = new EventListAdapter(dbHelper);
        teamAdapter = new TeamListAdapter(dbHelper);
//        resetAppData();
        recyclerView.setAdapter(evAdapter);
        // Swipe-to-delete functionality
        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                final int position = viewHolder.getAdapterPosition();
                if(viewHolder instanceof EventListAdapter.ViewHolder){
                    // swiped event view
                    if (evAdapter.getItem(position) == null) return 0;// prevent swipe on section headers
                }else if(viewHolder instanceof TeamListAdapter.ViewHolder){
                    // swiped team view
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {return false;}

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Are you sure that you want to remove it?");

                //When the user selects remove
                alertDialogBuilder.setPositiveButton("remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(viewHolder instanceof EventListAdapter.ViewHolder){
                            Object obj=((EventListAdapter.ViewHolder) viewHolder).textView.getTag();

                            evAdapter.remove((Event)obj);
                        }else if(viewHolder instanceof TeamListAdapter.ViewHolder){
                            Object obj=((TeamListAdapter.ViewHolder) viewHolder).textView.getTag();
                            teamAdapter.remove((Team)obj);
                        }
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
                        recyclerView.setAdapter(evAdapter);
                        break;
                    case 1:
                        recyclerView.setAdapter(teamAdapter);
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
    protected void onResume() {
        super.onResume();
        syncBuffer.sync();
        evAdapter.refresh();
        teamAdapter.refresh();
    }

    @Override
    protected void onPause(){
        super.onPause();
        syncBuffer.save();
    }

    public void uponSync(String response){
        // callback for SyncBuffer
//        Log.w("here", "resp: " + response);
        if(response.startsWith("+user:") || response.equals(":)")){
            return;
        }
        if(response.startsWith("+event:")){
            String[] ids=response.substring(7).split(",");
            evAdapter.setId(ids[0], ids[1]);
        }else if(response.startsWith("+team:")){
            String[] ids=response.substring(6).split(",");
            teamAdapter.setId(ids[0], ids[1]);
        }

    }

    public void coverActions(LinkedList<String> actions){
        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
        Set<String> set=new HashSet<>();
        for(int i=0;i<actions.size();i++){
            set.add(actions.get(i) + "_" + i);
        }
        editor.putStringSet("actionBuffer", set);
        editor.commit();
    }
    public LinkedList<String> recoverActions(){
        LinkedList<String> actions=new LinkedList<>();
        if(getLocalId().equals("!")){
            actions.offer("client=!");
        }

        SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set=settings.getStringSet("actionBuffer", null);
        if(set==null || set.isEmpty()){
            return actions;
        }
        // create and fluff array list
        for(int i=0;i<set.size();i++){
            actions.offer(null);
        }
        // now add actions back, in order
        for(String action:set){
            int underScoreIndex=action.lastIndexOf("_");
            int index=Integer.parseInt(action.substring(underScoreIndex+1));
            actions.set(index, action.substring(0, underScoreIndex));
        }
        return actions;
    }

    private void resetAppData(){
        dbHelper.reset();
        evAdapter.reset();
        teamAdapter.reset();
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
        if(id == R.id.changeAlias) {
            startActivity(new Intent(this, ChangeNameActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK){
            return;
        }
        switch(requestCode){
            case add_event_code:
                Event event=data.getExtras().getParcelable("eventObj");
                evAdapter.append(event);
                break;
            case add_team_code:
                Team team=data.getExtras().getParcelable("teamObj");
                teamAdapter.append(team);
                break;
//            case clear database
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

