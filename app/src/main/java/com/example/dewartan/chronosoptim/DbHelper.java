package com.example.dewartan.chronosoptim;

import java.util.ArrayList;

import android.content.ContentValues;
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

    private SyncBuffer syncBuffer;

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        syncBuffer=((ClientDevice)context).getBuffer();
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
        ContentValues cv=event.content();
        boolean fixId=(((String)cv.get("id")).length()==0);
        // if has ID insert and exit
        db.insert(EVENT_TABLE_NAME, null, cv);
        if(fixId){
            String newId=safeInsert(db,EVENT_TABLE_NAME,cv);
            event.setId(newId);
        }
        syncBuffer.send("x=createE&" + event.postForm());
    }
    public void insert(Team team){
        insert(getWritableDatabase(), team);
    }
    public void insert(SQLiteDatabase db,Team team){
        ContentValues cv=team.content();
        boolean fixId=(((String)cv.get("id")).length()==0);
        // if has ID insert and exit
        db.insert(TEAM_TABLE_NAME, null, cv);
        if(fixId){
            String newId=safeInsert(db,TEAM_TABLE_NAME,cv);
            team.setId(newId);
        }
        syncBuffer.send("x=createT&" + team.postForm());
    }
    public String safeInsert(SQLiteDatabase db,String tableName,ContentValues cv){
        // if no ID, find it...
        String command="select rowid from "+tableName+" where id=''";
        Cursor res=db.rawQuery(command, null);
        res.moveToFirst();
        int rowId=res.getInt(0);
        res.close();
        // ...and give it "temp"+rowid
        String newId="temp"+rowId;
        cv.put("id", newId);
        db.update(tableName, cv, "id=''", null);
        return newId;
    }

    public void appendMember(Team team,String username){
        // _id,id,name,description,members
        String names=team.getMembers();
        if(names.contains(username)){
            return;
        }
        ContentValues cv=new ContentValues();
        cv.put(TEAM_COLUMNS[3],names+","+username);
        String where=TEAM_COLUMNS[0]+"='"+team.getId()+"' AND "+
                TEAM_COLUMNS[1]+"='"+team.getName()+"' AND "+
                TEAM_COLUMNS[2]+"='"+team.getDescription()+"' AND "+
                TEAM_COLUMNS[3]+"='"+team.getMembers()+"'";
        SQLiteDatabase db=getWritableDatabase();
        db.update(TEAM_TABLE_NAME, cv, where, null);
        syncBuffer.send("x=createU&id=" + team.getId() + "&name" + username);
    }

    public void delete(Event e){
        // _id,id,title,description,eventdate,starttime,endtime,location,subtitle
        String where=EVENT_COLUMNS[0] + "='" + e.getId() + "'";
        delete(where, EVENT_TABLE_NAME);
        syncBuffer.send("x=deleteE&id="+e.getId());
    }

    public void delete(Team t){
        // _id,id,name,description,members
        String where=TEAM_COLUMNS[0]+"='"+t.getId()+"'";
        delete(where, TEAM_TABLE_NAME);
        syncBuffer.send("x=deleteT&id="+t.getId());

    }

    public void removeMember(Team team,String username){
        // _id,id,name,description,members
        String names=team.getMembers();
        if(!names.contains(username)){
            return;
        }
        int index=names.indexOf(username);
        String newNames=names.substring(0,index)+names.substring(index+username.length());
        ContentValues cv=new ContentValues();
        cv.put(TEAM_COLUMNS[3],newNames);
        String where=TEAM_COLUMNS[0]+"='"+team.getId()+"' AND "+
                TEAM_COLUMNS[1]+"='"+team.getName()+"' AND "+
                TEAM_COLUMNS[2]+"='"+team.getDescription()+"' AND "+
                TEAM_COLUMNS[3]+"='"+team.getMembers()+"'";

        SQLiteDatabase db=getWritableDatabase();
        db.update(TEAM_TABLE_NAME, cv, where, null);
        syncBuffer.send("x=deleteU&id="+team.getId()+"&name="+username);
    }

    public void delete(String where,String tableName){
        // get rowid, then delete, to delete the first occurrence only
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
            teams.add(new Team(id, name, description, members));
            res.moveToNext();
        }
        return teams;
    }

    public String[] pullUsers(String teamId){
        SQLiteDatabase db=getReadableDatabase();
//        String[] selectionArgs=new String[]{"id='"+teamId+"'"};
        Cursor res=db.rawQuery("select * from team where id='"+teamId+"'",null);
        if(res.moveToFirst()){
            String userList=res.getString(3);
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

//    public void reset(){
//        onUpgrade(getWritableDatabase(), 1, 1);
//    }

}