package com.example.dewartan.chronosoptim;

import android.content.Context;
import android.util.Log;
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

    public UserListAdapter(Context context,Team team,DbHelper dbHelper){
        this.dbHelper=dbHelper;
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
        String username=getItem(index);
        TextView tw=(TextView) view.findViewById(R.id.uname);
        tw.setText(username);
        view.setTag(username);
        return view;
    }

    public void append(String username){
        dbHelper.appendMember(team,username);
        userList.add(username);
        notifyDataSetChanged();
    }

    public void remove(String username){
        dbHelper.removeMember(team,username);
        userList.remove(username);
        notifyDataSetChanged();
    }
}