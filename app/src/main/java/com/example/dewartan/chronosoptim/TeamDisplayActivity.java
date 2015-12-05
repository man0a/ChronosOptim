package com.example.dewartan.chronosoptim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Prakhar on 12/4/2015.
 */

public class TeamDisplayActivity extends AppCompatActivity {
    private Toolbar actionBarToolBar;
    TextView name, description, toolbarTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_team);

        //Get the Object
        Team team = this.getIntent().getParcelableExtra("viewTeam");

        name = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

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
//            date.setText(EventDate.getDayOfWeek(eventOnDay));
//            subtitle.setText(eventOnDay.subtitle());
//            time.setText(eventOnDay.getStartTime()+"-"+eventOnDay.getEndTime());
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


}

