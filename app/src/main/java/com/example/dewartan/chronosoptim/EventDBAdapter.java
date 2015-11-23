package com.example.dewartan.chronosoptim;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class EventDBAdapter extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Events.db";
    public static final String EVENT_TABLE_NAME = "event";

    public static final String EVENT_COLUMN_ID = "id";
    public static final String EVENT_COLUMN_TITLE = "title";
    public static final String EVENT_COLUMN_DESCRIPTION = "description";
    public static final String EVENT_COLUMN_EVENTDATE = "eventdate";

    public static final String EVENT_COLUMN_STARTTIME= "starttime";
    public static final String EVENT_COLUMN_ENDTIME = "endtime";
    public static final String EVENT_COLUMN_LOCATION = "location";


    public EventDBAdapter(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table event " +
                "(id integer primary key, title text, description text, eventdate text, starttime text, endtime text, location text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS event");
        onCreate(db);
    }


    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ EVENT_TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    public boolean insertEvent(String description, String starttime, String location, String endtime, String title, String eventdate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("eventdate", eventdate);
        contentValues.put("starttime", starttime);
        contentValues.put("endtime", endtime);
        contentValues.put("location", location);
        db.insert("event", null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from event where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EVENT_TABLE_NAME);
        return numRows;
    }

    public boolean updateEvent (Integer id, String description, String starttime, String location, String endtime, String title, String eventdate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("eventdate", eventdate);
        contentValues.put("starttime", starttime);
        contentValues.put("endtime", endtime);
        contentValues.put("location", location);
        db.update("event", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteEvent (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("event", "id = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<Event> getAllEvents()
    {
        ArrayList<Event> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from event", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String title = res.getString(res.getColumnIndex(EVENT_COLUMN_TITLE));
            String eventdate = res.getString(res.getColumnIndex(EVENT_COLUMN_EVENTDATE));
            String location = res.getString(res.getColumnIndex(EVENT_COLUMN_LOCATION));
            String starttime = res.getString(res.getColumnIndex(EVENT_COLUMN_STARTTIME));
            String endtime = res.getString(res.getColumnIndex(EVENT_COLUMN_ENDTIME));
            String description = res.getString(res.getColumnIndex(EVENT_COLUMN_DESCRIPTION));
            array_list.add(new Event(location, eventdate, starttime, endtime, description, title));
            res.moveToNext();
        }
        return array_list;
    }
}