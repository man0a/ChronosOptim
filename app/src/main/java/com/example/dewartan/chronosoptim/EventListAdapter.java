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

    private static DbHelper eventDB;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> titleEvent = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();


    public EventListAdapter(Context context) {
        eventDB = new DbHelper(context);
        initHeaders();
    }

    private void initHeaders() {
        ArrayList<Event> dbEvents = eventDB.pull();

        // generate list of headers and events
        Calendar cal = new GregorianCalendar();
        while(EventDate.beforeMax(cal)) {

            boolean headerAdded = false;
            for(Event event:dbEvents) {

                if (EventDate.matches(cal,event)){
                    if (!headerAdded) {
                        headerAdded=true;
                        addHeader(EventDate.convert(cal));// Saturday, 11/11
                    }
                    addItem(event);
                }
            }
            cal.add(Calendar.DATE, 1);
        }
    }

    public void addItem(Event event){
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
                Event activity = events.get(position);
                holder.subView.setText(activity.getSubtitle());
                holder.locationView.setText(activity.getLocation());
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
                final Intent i;
                i = new Intent(context, DetailActivity.class);
                i.putExtra("viewEvent", eventOnDay); //Places the object into the key value pair
                context.startActivity(i);
            }
        }

    }



}