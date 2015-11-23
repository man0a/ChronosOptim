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
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * Created by dewartan on 10/20/15.
 */
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

    private List<Channel> feeds = new ArrayList<Channel>();


    private LayoutInflater mInflater;



    public ChannelAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initHeaders();
    }

    private void initHeaders() {
        this.feeds = fakeEvent();
    }

    private List<Channel> fakeEvent() {
        List<Channel> fakeData = new ArrayList<Channel>();
        Channel test1 = new Channel("Data Structures", "Cosi 21a Class");
        Channel test2 = new Channel("Chess Club", "For all those whom love chess");
        Channel test3 = new Channel("Lacrosse Events", "Sporting events concerning Brandeis Lacrosse");
        Channel test4 = new Channel("Patriots Fan Club", "New England Patriots Fan Club");
        Channel test5 = new Channel("Ultimate Frisbee Pickup", "Pickup games for Ultimate Frisbee");
        fakeData.add(test1);
        fakeData.add(test2);
        fakeData.add(test3);
        fakeData.add(test4);
        fakeData.add(test5);
        return fakeData;
    }

//
//    public void addItem(final Event event) {
//        title_event.add(event.getTitle());
//        event_info.add(event);
//        notifyDataSetChanged();
//    }

    @Override
    public ChannelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_view, parent, false);
        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel activity = feeds.get(position);
        holder.textView.setText(activity.getName());
        holder.subView.setText(activity.getDescription());
    }




    public Object getItem(int position) { return feeds.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }


    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public TextView subView;
        public CheckBox checkBox;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
            subView = (TextView) itemView.findViewById(R.id.description);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

//        @Override
//        public void onClick(View v) {
//            final Intent i;
//            i = new Intent(context, IndividualEventView.class);
//            Event eventOnDay = (Event) getItem(getPosition());
//            i.putExtra("viewEvent", eventOnDay);
//            context.startActivity(i);
//        }

    }



}


