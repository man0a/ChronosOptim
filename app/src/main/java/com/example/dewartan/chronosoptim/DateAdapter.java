package com.example.dewartan.chronosoptim;

import java.text.*;
import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by dewartan on 10/20/15.
 */
public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private List<String> title_event = new ArrayList<String>();
    private List<Event> event_info = new ArrayList<Event>();

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    public int myCliquedPosition;


    public DateAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initHeaders();
    }

    private void initHeaders() {
        List<String> getDates = getDateRange();
        List<Event> FakeData = fakeEvent();
        for(String date: getDates) {
            boolean header_added = false;
            for(Event data:FakeData) {
                Log.d("Date", date);

                if (date.equals(data.getDate()) && !header_added) {
                    header_added = true;
                    addSectionHeaderItem(date);
                    addItem(data);
                } else if(date.equals(data.getDate())) {
                    addItem(data);
                }
            }
        }
    }

    private List<Event> fakeEvent() {
        List<Event> fakeData = new ArrayList<Event>();
        Event test1 = new Event("Vertica", "Friday, Nov 20", "5:00pm", "\"Meeting Today\"", "MAD Project Meeting");
        Event test2 = new Event("SSC", "Saturday, Nov 21", "1:00pm", "\"105B NanoTwitter Project\"", "NanoTwitter");
        Event test3 = new Event("Shapiro", "Tuesday, Dec 22", "11:30am", "\"Lunch with Bob\"", "Food");
        Event test4 = new Event("Cambridge, MA", "Thursday, Dec 24", "5:00pm", "\"Interview with Company\"", "Interview");
        Event test5 = new Event("Home", "Thursday, Oct 29", "6:00pm", "\"Dinner with Fay\"", "Date Night");
        fakeData.add(test1);
        fakeData.add(test2);
        fakeData.add(test3);
        fakeData.add(test4);
        fakeData.add(test5);
        return fakeData;
    }




    private static List<String> getDateRange() {

        List<String> dates = new ArrayList<String>();


        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE,90);
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
            dates.add(dayOfWeek + ", " + (new SimpleDateFormat("MMM dd").format(cal1.getTime())) );
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public void addItem(final Event event) {
        title_event.add(event.getTitle());
        event_info.add(event);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        title_event.add(item);   //Adds date string
        event_info.add(null); //For spacing
        sectionHeader.add(title_event.size() - 1);
        notifyDataSetChanged();
    }


    public int getViewTypeCount() { return TYPE_MAX_COUNT; }

    @Override
    public DateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            View convertView;
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
            final Intent i;
            i = new Intent(context, EventView.class);
            Event eventOnDay = (Event) getItem(getPosition());
            i.putExtra("viewEvent", eventOnDay);
            context.startActivity(i);
        }

    }



}


