package com.example.dewartan.chronosoptim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by dewartan on 11/21/15.
 */
public class AddTeamActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private EditText mDescription, mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_team);
        Toolbar actionBarToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBarToolBar);
        toolbarTitle = (TextView) actionBarToolBar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Add Team");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //Added a text view in toolbar so I can manipulate app

        mName = (EditText) findViewById(R.id.input_Team_name);
        mDescription = (EditText) findViewById(R.id.input_description);
    }
    public void save(View v) {
        Team team=new Team(
            mName.getText().toString(),
            mDescription.getText().toString(),
            ""
        );
        Intent backIntent=new Intent();
        backIntent.putExtra("teamObj",team);
        setResult(RESULT_OK, backIntent);
        finish();
    }

    public void cancel(View v) {
        setResult(RESULT_CANCELED,null);
        finish();
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

}

