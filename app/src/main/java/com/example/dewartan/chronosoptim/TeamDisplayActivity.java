package com.example.dewartan.chronosoptim;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_team);

        //Get the Object
        team = this.getIntent().getParcelableExtra("viewTeam");

        name = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        ListView userList=(ListView) findViewById(R.id.user_list);

        SyncBuffer syncBuffer=new SyncBuffer(this);
        DbHelper dbHelper=new DbHelper(this,syncBuffer);
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

    }

    public void uponSync(String response){}
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
}

