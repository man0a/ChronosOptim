package com.example.dewartan.chronosoptim;

import java.util.*;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by dewartan on 10/20/15.
 */
public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {

    private List<Team> feeds = new ArrayList<Team>();
    private TeamDbHelper TeamDB;
    private LayoutInflater mInflater;

    public TeamListAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TeamDB = new TeamDbHelper(context);
        initHeaders();
    }

    private void initHeaders() {
        ArrayList<Team> allTeams = TeamDB.getAllTeams();
        Team test1 = new Team("Data Structures", "Cosi 21a Class");
        Team test2 = new Team("Chess Club", "For all those whom love chess");
        Team test3 = new Team("Lacrosse Events", "Sporting events concerning Brandeis Lacrosse");
        Team test4 = new Team("Patriots Fan Club", "New England Patriots Fan Club");
        Team test5 = new Team("Ultimate Frisbee Pickup", "Pickup games for Ultimate Frisbee");
        allTeams.add(test1);
        allTeams.add(test2);
        allTeams.add(test3);
        allTeams.add(test4);
        allTeams.add(test5);
        this.feeds = allTeams;
    }

    @Override
    public TeamListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Team activity = feeds.get(position);
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
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
            subView = (TextView) itemView.findViewById(R.id.description);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {}

    }



}


