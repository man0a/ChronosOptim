package com.example.dewartan.chronosoptim;

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
    private TeamDbHelper teamDB;
    private Button cancel, save;
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

        teamDB = new TeamDbHelper(this);

        mName = (EditText) findViewById(R.id.input_Team_name);
        mDescription = (EditText) findViewById(R.id.input_description);

        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamDB.insertTeam(
                        mName.getText().toString(),
                        mDescription.getText().toString()
                );
                setResult(RESULT_OK);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

