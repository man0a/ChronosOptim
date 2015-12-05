package com.example.dewartan.chronosoptim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dewartan on 11/21/15.
 */
public class AddEventActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;

    static final int TIME_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID2 = 2;
    static final int DATE_DIALOG = 3;

    int cur = 0;
    private TextView start_time, end_time, calendarDate, toolbarTitle;
    private EditText inputTitle, inputLocation, inputDescription, inputSubtitle;

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

        inputTitle = (EditText) findViewById(R.id.input_title);
        inputLocation = (EditText)  findViewById(R.id.input_location);
        inputDescription = (EditText) findViewById(R.id.input_description);
        inputSubtitle = (EditText) findViewById(R.id.input_subtitle);
    }

    public void cancel(View v) {
        setResult(RESULT_CANCELED,null);
        finish();
    }

    public void save(View v){
        Date selectedDate=EventDate.parse(mMonth + "-" + mDay + "-" + mYear);

        Event event=new Event(
                inputTitle.getText().toString(),
                inputDescription.getText().toString(),
                EventDate.format(selectedDate),
                start_time.getText().toString(),
                end_time.getText().toString(),
                inputLocation.getText().toString(),
                inputSubtitle.getText().toString());
        Intent backIntent = new Intent();
        backIntent.putExtra("eventObj",event);
        setResult(RESULT_OK, backIntent);
        finish();
    }


    public void launchCal(View v){
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        calendarDate.setText(mMonth+1 + "-" + mDay+ "-" + mYear);

        cur = DATE_DIALOG;

        showDialog(DATE_DIALOG);
    }

    public void launchStart(View v) {
        cur = TIME_DIALOG_ID;
        showDialog(TIME_DIALOG_ID);
    }

    public void launchEnd(View v) {
        cur = TIME_DIALOG_ID2;
        showDialog(TIME_DIALOG_ID2);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                cur = TIME_DIALOG_ID;
                return new TimePickerDialog(this, timePickerListener, mHour, mMinute, true);
            case TIME_DIALOG_ID2:
                cur = TIME_DIALOG_ID2;
                return new TimePickerDialog(this, timePickerListener, mHour, mMinute, true);
            case DATE_DIALOG:
                cur = DATE_DIALOG;
                return new DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay );
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            mDay = selectedDay;
            mYear= selectedYear;
            mMonth = selectedMonth+1;
            calendarDate.setText(mMonth + "-" + mDay+ "-" + mYear);
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

}


