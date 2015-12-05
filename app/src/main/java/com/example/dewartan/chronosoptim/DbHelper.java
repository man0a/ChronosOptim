package com.example.dewartan.chronosoptim;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ChronosOptim.db";
    public static final int DATABASE_VERSION = 1;
    public static final String EVENT_TABLE_NAME = "event";
    public static final String TEAM_TABLE_NAME = "team";

    private static final String[] EVENT_COLUMNS = new String[]{"id","title","description","eventdate","starttime","endtime","location","subtitle"};
    private static final String[] TEAM_COLUMNS = new String[]{"id","name","description","members"};

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table event ( id text, title text, description text, eventdate text, starttime text, endtime text, location text, subtitle text)");
        db.execSQL("create table team ( id text, name text, description text, members text)");

        insert(db,new Event("MAD Project Meeting", "Vertica", "12-12-2015", "17:00", "18:00", "Meeting, We will go over the different views that need fixing and additionally, go over the backend server stuff", "Fix views on the events page"));
        insert(db,new Event("NanoTwitter", "SSC", "12-12-2015", "13:00", "14:00", "We need to finalize the columns in the migration table and different routes for calling CRUD operations", "105B NanoTwitter Project"));
        insert(db,new Event("Food", "Shapiro", "12-22-2015", "11:30", "12:30", "Meet with bob to discuss the different internet plans Comcast has to offer for the apartment", "Lunch with Bob"));
        insert(db,new Event("Interview", "Cambridge, MA", "12-24-2015", "18:00", "19:00", "Prepare for interview with company x, Things to do: research products, pratice questions, and iron clothes ", "Interview with Company"));
        insert(db, new Event("Date Night", "Home", "12-31-2015", "18:00", "19:00", "Bring korean pot, Things to grab at Shaws: Chocolate & Flowers ", "Dinner with Fay"));

        insert(db, new Team("Data Structures", "Cosi 21a Class", "XiIccSTdEq,hgbigJEsZr,Yy5uFVA51l"));
        insert(db, new Team("Patriots Fan Club", "New England Patriots Fan Club", "XiIccSTdEq,hgbigJEsZr,JFxq3s4iaU"));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS event");
        db.execSQL("DROP TABLE IF EXISTS team");
        onCreate(db);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ EVENT_TABLE_NAME);
        db.execSQL("DELETE FROM " + TEAM_TABLE_NAME);
    }

    public void insert(Event event){
        insert(getWritableDatabase(), event);
    }
    public void insert(SQLiteDatabase db,Event event){
        db.insert("event", null, event.content());
    }
    public void insert(Team team){
        insert(getWritableDatabase(), team);
    }
    public void insert(SQLiteDatabase db,Team team){
        db.insert("team", null, team.content());
    }

    public void delete(Event e){
        // _id,id,title,description,eventdate,starttime,endtime,location,subtitle
        String where=EVENT_COLUMNS[0]+"='"+e.getId()+"' AND "+
                EVENT_COLUMNS[1]+"='"+e.getTitle()+"' AND "+
                EVENT_COLUMNS[2]+"='"+e.getDescription()+"' AND "+
                EVENT_COLUMNS[3]+"='"+e.getDate()+"' AND "+
                EVENT_COLUMNS[4]+"='"+e.getStartTime()+"' AND "+
                EVENT_COLUMNS[5]+"='"+e.getEndTime()+"' AND "+
                EVENT_COLUMNS[6]+"='"+e.getLocation()+"' AND "+
                EVENT_COLUMNS[7]+"='"+e.getSubtitle()+"'";
        delete(where, EVENT_TABLE_NAME);
    }

    public void delete(Team t){
        // _id,id,name,description,members
        String where=TEAM_COLUMNS[0]+"='"+t.getId()+"' AND "+
                TEAM_COLUMNS[1]+"='"+t.getName()+"' AND "+
                TEAM_COLUMNS[2]+"='"+t.getDescription()+"' AND "+
                TEAM_COLUMNS[3]+"='"+t.getMembers()+"'";
        delete(where,TEAM_TABLE_NAME);

    }

    public void delete(String where,String tableName){
        SQLiteDatabase db=getWritableDatabase();
        // which, where, whereArgs, groupBy, having, orderBy, limit
        Cursor cursor = db.query(tableName, new String[]{"rowid"}, where, null, null, null, null, "1");
        if(cursor.moveToFirst()){
            int rowid=cursor.getInt(0);
            db.delete(tableName, "rowid=" + rowid, null);
        }
    }

    public ArrayList<Event> pullEvents(){
        ArrayList<Event> events = new ArrayList<>();
        Cursor res=query(EVENT_TABLE_NAME);

        while(!res.isAfterLast()){
            String id = res.getString(0);
            String title = res.getString(1);
            String description = res.getString(2);
            String eventDate = res.getString(3);
            String startTime = res.getString(4);
            String endTime = res.getString(5);
            String location = res.getString(6);
            String subtitle = res.getString(7);
            events.add(new Event(id, title, location, eventDate, startTime, endTime, description, subtitle));
            res.moveToNext();
        }
        return events;
    }

    public ArrayList<Team> pullTeams(){
        ArrayList<Team> teams = new ArrayList<>();
        Cursor res=query(TEAM_TABLE_NAME);

        while(!res.isAfterLast()){
            String id=res.getString(0);
            String name=res.getString(1);
            String description = res.getString(2);
            String members = res.getString(3);
            teams.add(new Team(name, description, members));
            res.moveToNext();
        }
        return teams;
    }

    public String[] pullUsers(String teamId){
        SQLiteDatabase db=getReadableDatabase();
        String[] selectionArgs=new String[]{"id='"+teamId+"'"};
        Cursor res=db.rawQuery("select * from team",selectionArgs);
        if(res.moveToFirst()){
            String userList=res.getString(4);
            return userList.split(",");
        }else{
            return null;
        }
    }

    public Cursor query(String tableName){
        SQLiteDatabase db=getReadableDatabase();
        Cursor res=db.rawQuery("select * from "+tableName, null );
        res.moveToFirst();
        return res;
    }

    public void reset(){
        onUpgrade(getWritableDatabase(),1,1);
    }
}