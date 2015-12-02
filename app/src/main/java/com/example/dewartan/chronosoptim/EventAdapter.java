package com.example.dewartan.chronosoptim;

import java.text.*;
import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private List<String> title_event = new ArrayList<String>();
    private List<Event> event_info = new ArrayList<Event>();
    private List<String> dateRange = new ArrayList<String>();

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    public int myCliquedPosition;


    public EventAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        eventDB = new EventDBAdapter(context);
        initHeaders();
    }

    private void initHeaders() {
        dateRange = getDateRange(); //Grabs the next 90 days
        ArrayList<Event> dbEvents = eventDB.getAllEvents();
        Event test1 = new Event("Vertica", "Wednesday, Dec 30-2015", "17:00", "18:00", "Meeting, We will go over the different views that need fixing and additionally, go over the backend server stuff", "MAD Project Meeting", "Fix views on the events page");
        Event test2 = new Event("SSC", "Saturday, Dec 12-2015", "13:00", "14:00", "We need to finalize the columns in the migration table and different routes for calling CRUD operations", "NanoTwitter", "105B NanoTwitter Project");
        Event test3 = new Event("Shapiro", "Tuesday, Dec 22-2015","11:30", "12:30", "Meet with bob to discuss the different internet plans Comcast has to offer for the apartment" , "Food", "Lunch with Bob");
        Event test4 = new Event("Cambridge, MA", "Thursday, Dec 24-2015", "18:00", "19:00", "Prepare for interview with company x, Things to do: research products, pratice questions, and iron clothes " , "Interview", "Interview with Company");
        Event test5 = new Event("Home", "Friday, Dec 31-2015", "18:00", "19:00", "Bring korean pot, Things to grab at Shaws: Chocolate & Flowers ", "Date Night", "Dinner with Fay");
        dbEvents.add(test1);
        dbEvents.add(test2);
        dbEvents.add(test3);
        dbEvents.add(test4);
        dbEvents.add(test5);

        for(String date: dateRange) {
            boolean header_added = false;
            for(Event data:dbEvents) {
                Log.d("Date", date);

                if (date.equals(data.getDate()) && !header_added) {
                    header_added = true;
                    addSectionHeaderItem(date); //Adds in date
                    addItem(data);
                } else if(date.equals(data.getDate())) {
                    addItem(data);
                }
            }
        }
    }


    private static List<String> getDateRange() {

        List<String> dates = new ArrayList<String>();

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 90); //Get the next three months
        String next_month = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String current_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date first_date = null;
        Date second_date = null;

        try {
            first_date = df1.parse(current_date);
            second_date = df1.parse(next_month);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(first_date);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(second_date);

        while (!cal1.after(cal2)) {
            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.US).format(cal1.getTime());
            dates.add(dayOfWeek + ", " + (new SimpleDateFormat("MMM dd").format(cal1.getTime())) +"-" + (cal1.get(Calendar.YEAR)) );
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public  void remove(int event) {event_info.remove(event);}

    public  void remove(Event event) {event_info.remove(event);}

    public boolean contains(Event event) {
        return event_info.contains(event);
    }

    public void addItem(final Event event) {

        if(dateRange.contains(event.getDate()) && !title_event.contains(event.getDate())) {
            title_event.add(event.getDate());
            event_info.add(null); //For spacing
            sectionHeader.add(title_event.size() - 1);
            notifyDataSetChanged();
        } else {
            title_event.add(event.getTitle());
            event_info.add(event);
            notifyDataSetChanged();
        }
    }

    public void addSectionHeaderItem(final String item) {
        title_event.add(item);   //Adds date string
        event_info.add(null); //For spacing
        sectionHeader.add(title_event.size() - 1);
        notifyDataSetChanged();
    }


    public int getViewTypeCount() { return TYPE_MAX_COUNT; }

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
                    holder.textView.setText(title_event.get(position));
                    Event activity = event_info.get(position);
                    holder.subView.setText(activity.getSubtitle());
                    holder.locationView.setText(activity.getLocation());
                    break;
                case TYPE_SEPARATOR:
                    holder.textView.setText(title_event.get(position));
                    break;
            }
    }


    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }


    public Object getItem(int position) { return event_info.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return title_event.size();
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
                i = new Intent(context, IndividualEventView.class);
                i.putExtra("viewEvent", eventOnDay);
                context.startActivity(i);
            }
        }



    }



}


