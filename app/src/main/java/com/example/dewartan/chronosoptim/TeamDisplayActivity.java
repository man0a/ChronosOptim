package com.example.dewartan.chronosoptim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Prakhar on 12/4/2015.
 */

public class TeamDisplayActivity extends ClientDevice{
    private Toolbar actionBarToolBar;
    private TextView name, description, toolbarTitle;
    private Team team;
    private UserListAdapter userAdapter;
    private DbHelper dbHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_team);

        //Get the Object
        team = this.getIntent().getParcelableExtra("viewTeam");
//        Log.w("here", team.getId());

        name = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        ListView userList=(ListView) findViewById(R.id.user_list);

        SyncBuffer syncBuffer=new SyncBuffer(this);
        dbHelper=new DbHelper(this,syncBuffer);
        userAdapter=new UserListAdapter(this,team,dbHelper);
        userList.setAdapter(userAdapter);

        actionBarToolBar = (Toolbar) findViewById(R.id.empty_bar);
        setSupportActionBar(actionBarToolBar);
        toolbarTitle = (TextView) actionBarToolBar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (team != null){
            name.setText(team.getName());
            toolbarTitle.setText(team.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            description.setText(team.getDescription());
        }

        syncBuffer.send("&x=pullU");

    }

    public void uponSync(String response){
//        Log.w("here", "tdisp: " + response);

        if(!response.startsWith("=users:")){
            return;
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.user_list_item);
        String[] names=response.substring(7).split(",");
        for(String name:names){
            adapter.add(name);
        }

        AutoCompleteTextView tw=(AutoCompleteTextView)findViewById(R.id.type_user);
        tw.setAdapter(adapter);
    }
    public void coverActions(LinkedList<String> actions){}
    public LinkedList<String> recoverActions(){return new LinkedList<>();}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void add(View view){
        EditText editor=(EditText) findViewById(R.id.type_user);
        String username=editor.getText().toString();
        editor.setText("");
        if(!username.equals("")){
            userAdapter.append(username);
        }
    }

    public void delete(View view){
        View listItem=(View)view.getParent();
        String username=(String)listItem.getTag();
        userAdapter.remove(username);
    }

    public void optim(View view){
        Intent intent=new Intent(this,OptimResultsActivity.class);
        intent.putExtra("team",team);
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String time=data.getStringExtra("time");
        String times[]=time.split(" - ");
        String date=data.getStringExtra("date");
        Event event=new Event(team.getName()+" meeting","",date,times[0],times[1],"",team.getMembers());
        dbHelper.broadcast(event,team);
    }
}

