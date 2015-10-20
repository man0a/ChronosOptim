package com.example.dewartan.chronosoptim;

import java.text.*;
import java.util.*;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.TextView;


/**
 * Created by dewartan on 10/20/15.
 */
public class DateAdapter extends BaseAdapter {

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
        GeneralEvent test1 = new GeneralEvent("Vertica", "Tuesday, Oct 20", "5:00pm", "\"Meeting Today\"", "MAD Project Meeting");
        GeneralEvent test2 = new GeneralEvent("SSC", "Tuesday, Oct 20", "1:00pm", "\"105B NanoTwitter Project\"", "NanoTwitter");
        GeneralEvent test3 = new GeneralEvent("Shapiro", "Tuesday, Oct 20", "11:30am", "\"Lunch with Bob\"", "Food");
        GeneralEvent test4 = new GeneralEvent("Cambridge, MA", "Saturday, Oct 24", "5:00pm", "\"Interview with Company\"", "Interview");
        GeneralEvent test5 = new GeneralEvent("Home", "Thursday, Oct 29", "6:00pm", "\"Dinner with Fay\"", "Date Night");
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
        cal.add(Calendar.DATE,30);
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


    @Override
    public int getViewTypeCount() { return TYPE_MAX_COUNT; }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getCount() {
        return title_event.size();
    }

    @Override
    public Object getItem(int position) { return title_event.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);
        Log.d("number", ""+position);
        Log.d("inserted", title_event.get(position));
//        Log.d("inserted", event_info.get(position).getDescription());

        if (convertView == null) {
            Log.d("null", "convertView was null");
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.events_layout, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    holder.subView = (TextView) convertView.findViewById(R.id.description);
                    holder.locationView = (TextView) convertView.findViewById(R.id.location);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.seperator_header, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    holder.subView = null;
                    holder.locationView = null;
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(title_event.get(position));
        if(holder.subView != null && holder.locationView != null) {
            GeneralEvent activity = (GeneralEvent) event_info.get(position);
            holder.subView.setText(activity.getSubtitle());
            holder.locationView.setText(activity.getLocation());
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public TextView subView;
        public TextView locationView;
    }


}
