package com.example.dewartan.chronosoptim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by dewartan on 11/21/15.
 */
public class AddEvent extends AppCompatActivity {

    private Button startTime, endTime, calendarButton, cancelButton;
    private int mYear, mMonth, mDay, mHour, mMinute;

    static final int TIME_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID2 = 2;
    static final int DATE_DIALOG = 3;

    int cur = 0;


    private TextView start_time, end_time, calendarDate, toolbarTitle;
    private Toolbar actionBarToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);


        actionBarToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBarToolBar);
        actionBarToolBar.setTitle(null);
        toolbarTitle = (TextView) actionBarToolBar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        start_time = (TextView) findViewById(R.id.input_start_time);
        end_time = (TextView) findViewById(R.id.input_end_time);
        calendarDate = (TextView) findViewById(R.id.input_date);
        addListenerOnButton();

        cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void addListenerOnButton() {
        endTime = (Button) findViewById(R.id.endtimepicker);
        startTime = (Button) findViewById(R.id.starttimepicker);
        calendarButton = (Button) findViewById(R.id.datepicker);

        calendarButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
             cur = DATE_DIALOG;

            showDialog(DATE_DIALOG);
            }
        });


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                calendarDate.setText(mMonth + "-" + mDay+ "-" + (mYear + 1));
                cur = TIME_DIALOG_ID;
                showDialog(TIME_DIALOG_ID);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                cur = TIME_DIALOG_ID2;
                showDialog(TIME_DIALOG_ID2);
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                cur = TIME_DIALOG_ID;
                return new TimePickerDialog(this, timePickerListener, mHour, mMinute, false);
            case TIME_DIALOG_ID2:
                cur = TIME_DIALOG_ID2;
                return new TimePickerDialog(this, timePickerListener, mHour, mMinute, false);
            case DATE_DIALOG:
                cur = DATE_DIALOG;
                return new DatePickerDialog(this, datePickerListener, mYear, mHour, mDay );
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            calendarDate.setText((selectedMonth+1) + "-" + selectedDay+ "-" +selectedYear);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            String textHour, textMinute;
            if(selectedMinute <= 9) {
                textHour = "0" + selectedHour;
            } else {
                textHour = selectedHour +"";
            }
            if(selectedMinute <= 9) {
                textMinute = "0" + selectedMinute ;
            } else {
                textMinute = selectedMinute + "";
            }
            if(cur == TIME_DIALOG_ID){
                start_time.setText(textHour + ":" + textMinute);
            }
            else{
                end_time.setText(textHour + ":" + textMinute);
            }
        }

        };

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //save

}


