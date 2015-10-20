package com.example.dewartan.chronosoptim;

import java.text.*;
import java.util.*;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by dewartan on 10/20/15.
 */
public class DateAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;


    private List<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;


    public DateAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initHeaders();
    }

    private void initHeaders() {
        List<String> getDates = getDateRange();
        GeneralEvent test = new GeneralEvent("Vertica", "Tuesday, Oct 20", "5pm", "Meeting Today", "MAD Project Meeting");
        for(String date: getDates) {
            //We can add conditionals here to check if event date is present, otherwise do not
            // add a section header
            Log.d("Date", date);
            if(date.equals(test.getDate())) {
                addSectionHeaderItem(date);
                addItem(test.getTitle());
            }
        }
    }




    private static List<String> getDateRange() {

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE,30);
        String next_month = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String current_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        List<String> dates = new ArrayList<String>();
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

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
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
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.events_layout, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.seperator_header, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
