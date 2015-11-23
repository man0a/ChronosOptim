package com.example.dewartan.chronosoptim;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class ChannelDBAdapter extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "chronosoptim.db";
    public static final String CHANNEL_TABLE_NAME = "channel";

    public static final String CHANNEL_COLUMN_ID = "id";
    public static final String CHANNEL_COLUMN_NAME = "name";
    public static final String CHANNEL_COLUMN_DESCRIPTION = "description";


    public ChannelDBAdapter(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table channel " + "(id integer primary key, name text,description text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS channel");
        onCreate(db);
    }

    public boolean insertChannel(String name, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        db.insert("channel", null, contentValues);
        return true;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ CHANNEL_TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from channel where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CHANNEL_TABLE_NAME);
        return numRows;
    }

    public boolean updateChannel (Integer id, String description, String note, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("description", description);
        contentValues.put("note", note);
        contentValues.put("date", date);
        db.update("channel", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteChannel (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("channel", "id = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<Channel> getAllChannels()
    {
        ArrayList<Channel> array_list = new ArrayList<Channel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from channel", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String name = res.getString(res.getColumnIndex(CHANNEL_COLUMN_NAME));
            String description = res.getString(res.getColumnIndex(CHANNEL_COLUMN_DESCRIPTION));
//            int id = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_ID));
            array_list.add(new Channel(name, description));
            res.moveToNext();
        }
        return array_list;
    }
}