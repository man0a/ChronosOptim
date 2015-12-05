package com.example.dewartan.chronosoptim;

import java.util.*;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by dewartan on 10/20/15.
 */
public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {

    private List<Team> teams = new ArrayList<>();
    private DbHelper dbHelper;

    public TeamListAdapter(DbHelper dbHelper) {
        this.dbHelper=dbHelper;
        teams=dbHelper.pullTeams();
        refresh();
    }

    public void append(Team team){
        dbHelper.insert(team);
        teams.add(team);
        notifyDataSetChanged();
    }

    public void remove(Team team){
        dbHelper.delete(team);
        teams.remove(team);
        notifyDataSetChanged();
    }

    public void reset(){
        dbHelper.reset();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public TeamListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.textView.setText(team.getName());
        holder.subView.setText(team.getDescription());
        holder.textView.setTag(team);
    }

    public Object getItem(int position) { return teams.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }


    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public TextView subView;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
            subView = (TextView) itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            Team selected = (Team) getItem(getPosition());
            Intent intent = new Intent(context, TeamDisplayActivity.class);
            intent.putExtra("viewTeam", selected);
            context.startActivity(intent);
        }
    }
}


