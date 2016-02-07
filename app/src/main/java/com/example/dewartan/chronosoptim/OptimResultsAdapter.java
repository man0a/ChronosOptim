package com.example.dewartan.chronosoptim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Prakhar on 12/5/2015.
 */
public class OptimResultsAdapter extends BaseAdapter {
    private ArrayList<String> optimList;
    private int teamSize;

    public OptimResultsAdapter(int teamSize){
        optimList=new ArrayList<>();
        this.teamSize=teamSize;
    }

    public String getItem(int i){
        return optimList.get(i);
    }
    public long getItemId(int i){
        return (long)i;
    }
    public int getCount(){
        return optimList.size();
    }

    public View getView(int index,View convertView,ViewGroup parent){
        View view=convertView;
        if(view==null) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_suggestion, parent, false);
        }
        String suggestion=getItem(index);

        // list item view formatting
//        Log.w("here",suggestion);
        String[] parts=suggestion.split(",");
        setTextOn(parts[1]+" - "+parts[2],R.id.time,view);
        setTextOn(parts[0],R.id.date,view);
        String text="Available: "+(teamSize-Integer.parseInt(parts[3]))+"/"+teamSize;
        setTextOn(text,R.id.available,view);

        view.setTag(suggestion);
        return view;
    }

    private void setTextOn(String text,int id,View view){
        TextView tw=(TextView) view.findViewById(id);
        tw.setText(text);
    }

    public void set(String optim){
        String[] suggestions=optim.substring(7).split(";");
        for(String suggestion:suggestions){
            optimList.add(suggestion);
        }
        notifyDataSetChanged();
    }
}