package com.example.dewartan.chronosoptim;

import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by dewartan on 10/20/15.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private DbHelper dbHelper;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> titleEvent;
    private ArrayList<Event> events;
    private TreeSet<Integer> sectionHeader;

    private ArrayList<Event> dbEvents;

    public EventListAdapter(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        dbEvents = dbHelper.pullEvents();
        refresh();
    }

    private void initHeaders() {
        // generate list of headers and events

        titleEvent=new ArrayList<>();
        events=new ArrayList<>();
        sectionHeader=new TreeSet<>();
        Calendar cal = new GregorianCalendar();
        while(EventDate.beforeMax(cal)) {

            boolean headerAdded = false;
            for(Event event:dbEvents) {

                if (EventDate.matches(cal,event)){
                    if (!headerAdded) {
                        headerAdded=true;
                        addHeader(EventDate.format(cal));// Saturday, 11/11
                    }
                    addEvent(event);
                }
            }
            cal.add(Calendar.DATE, 1);
        }
    }

    public void addEvent(Event event){
        if(!EventDate.beforeMax(event)){
            return;
        }

        titleEvent.add(event.getTitle());
        events.add(event);
        notifyDataSetChanged();
    }

    public void addHeader(String date) {
        String header=EventDate.pretty(date);
        titleEvent.add(header);   //Adds date string
        events.add(null); //For spacing
        sectionHeader.add(titleEvent.size() - 1);
        notifyDataSetChanged();
    }

    public void append(Event event){
        dbHelper.insert(event);
        if(!EventDate.beforeMax(event)){
            return;
        }
        dbEvents.add(event);
        refresh();
    }

    public void remove(Event event){
        dbHelper.delete(event);
        if(!EventDate.beforeMax(event)){
            return;
        }
        dbEvents.remove(event);
        refresh();
    }

    public void reset(){
        dbEvents=dbHelper.pullEvents();
        refresh();
    }
    public void refresh(){
        initHeaders();
    }

    public void setId(String rotten,String fresh){
        dbHelper.updateId(rotten, fresh, "event");
        for(Event event:dbEvents){
            if(event.getId().equals(rotten)){
                event.setId(fresh);
                return;
            }
        }
    }

    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ITEM:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_layout, parent, false);
                break;
            case TYPE_SEPARATOR:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperator_header, parent, false);
                break;
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int rowType = getItemViewType(position);
        switch (rowType) {
            case TYPE_ITEM:
                holder.textView.setText(titleEvent.get(position));
                Event event = events.get(position);
                holder.subView.setText(event.getSubtext());
                holder.locationView.setText(event.getLocation());
                holder.textView.setTag(event);
                break;
            case TYPE_SEPARATOR:
                holder.textView.setText(titleEvent.get(position));
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }


    public Object getItem(int position) { return events.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return titleEvent.size();
    }


    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public TextView subView;
        public TextView locationView;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            subView = (TextView) itemView.findViewById(R.id.description);
            locationView = (TextView) itemView.findViewById(R.id.location);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Event eventOnDay = (Event) getItem(getPosition());
            if(eventOnDay != null) {
                Intent i = new Intent(context, EventDisplayActivity.class);
                i.putExtra("viewEvent", eventOnDay); //Places the object into the key value pair
                context.startActivity(i);
            }
        }

    }



}