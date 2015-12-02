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
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private static EventDBAdapter eventDB;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> titleEvent = new ArrayList<>();
    private ArrayList<Event> eventInfo = new ArrayList<>();


    private TreeSet<Integer> sectionHeader = new TreeSet<>();


    public EventAdapter(Context context) {
        eventDB = new EventDBAdapter(context);
        initHeaders();
    }

    private void initHeaders() {
        ArrayList<Event> dbEvents = eventDB.getAllEvents();
        Event test1 = new Event("Vertica", "12-12-2015", "17:00", "18:00", "Meeting, We will go over the different views that need fixing and additionally, go over the backend server stuff", "MAD Project Meeting", "Fix views on the events page");
        Event test2 = new Event("SSC", "12-12-2015", "13:00", "14:00", "We need to finalize the columns in the migration table and different routes for calling CRUD operations", "NanoTwitter", "105B NanoTwitter Project");
        Event test3 = new Event("Shapiro", "12-22-2015","11:30", "12:30", "Meet with bob to discuss the different internet plans Comcast has to offer for the apartment" , "Food", "Lunch with Bob");
        Event test4 = new Event("Cambridge, MA", "12-24-2015", "18:00", "19:00", "Prepare for interview with company x, Things to do: research products, pratice questions, and iron clothes " , "Interview", "Interview with Company");
        Event test5 = new Event("Home", "12-31-2015", "18:00", "19:00", "Bring korean pot, Things to grab at Shaws: Chocolate & Flowers ", "Date Night", "Dinner with Fay");
        dbEvents.add(test1);
        dbEvents.add(test2);
        dbEvents.add(test3);
        dbEvents.add(test4);
        dbEvents.add(test5);

        Calendar cal = new GregorianCalendar();

        while(EventDate.beforeMax(cal)) {

            boolean headerAdded = false;
            for(Event event:dbEvents) {

                if (EventDate.matches(cal,event)){
                    if (!headerAdded) {
                        headerAdded=true;
                        addHeader(EventDate.convert(cal)); //Adds in date
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
        eventInfo.add(event);
        notifyDataSetChanged();
    }

    public void addHeader(String date) {
        String header=EventDate.pretty(date);
        titleEvent.add(header);   //Adds date string
        eventInfo.add(null); //For spacing
        sectionHeader.add(titleEvent.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
                Event activity = eventInfo.get(position);
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


    public Object getItem(int position) { return eventInfo.get(position); }

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
                i.putExtra("viewEvent", eventOnDay);
                context.startActivity(i);
            }
        }

    }



}