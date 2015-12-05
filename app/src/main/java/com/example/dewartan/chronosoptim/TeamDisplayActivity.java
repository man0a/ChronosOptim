package com.example.dewartan.chronosoptim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Prakhar on 12/4/2015.
 */

public class TeamDisplayActivity extends AppCompatActivity {
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
        userAdapter=new UserListAdapter(this,team);
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
        userAdapter.append(username);
    }

    public void delete(View view){
        String username=(String)view.getTag();
        userAdapter.remove(username);
    }
}

