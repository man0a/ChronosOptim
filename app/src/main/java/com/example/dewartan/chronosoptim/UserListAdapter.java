package com.example.dewartan.chronosoptim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Prakhar on 12/5/2015.
 */
public class UserListAdapter extends BaseAdapter {
    private Team team;
    private ArrayList<String> userList;
    private DbHelper dbHelper;

    public UserListAdapter(Context context,Team team){
        this.dbHelper=new DbHelper(context);
        this.team=team;
        String[] users=dbHelper.pullUsers(team.getId());
        userList=new ArrayList<>();
        for(String user:users){
            userList.add(user);
        }
    }

    public String getItem(int i){
        return userList.get(i);
    }
    public long getItemId(int i){
        return (long)i;
    }
    public int getCount(){
        return userList.size();
    }
    public View getView(int index,View convertView,ViewGroup parent){
        View view=convertView;
        if(view==null) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view, parent, false);
        }
        String uname=getItem(index);
        TextView tw=(TextView) view.findViewById(R.id.uname);
        tw.setText(uname);
        tw.setTag(uname);
        return view;
    }

    public void append(String uname){
        dbHelper.appendMember(team,uname);
        userList.add(uname);
        notifyDataSetChanged();
    }

    public void remove(String uname){
        dbHelper.removeMember(team,uname);
        userList.remove(uname);
        notifyDataSetChanged();
    }
}