package anonymousme.gitstar.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import anonymousme.gitstar.Activities.ProjectView;
import anonymousme.gitstar.Models.Project;
import anonymousme.gitstar.R;

/**
 * Created by affan on 7/4/17.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder>{

    private List<Project> mDataset;
    private Context context;

    public ProjectAdapter(Context context) {
        this.context = context;
        mDataset = new ArrayList<>();
    }

    public void add(Project item) {
        mDataset.add(item);
        notifyItemInserted(mDataset.indexOf(item));
    }

    public void remove(Project item) {
        mDataset.remove(item);
        notifyItemInserted(mDataset.indexOf(item));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_projects_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Project data = mDataset.get(position);

        holder.title.setText(data.getTitle().toUpperCase());
        holder.star_count.setText(data.getStars()+"");
        holder.fork_count.setText(data.getForks()+"");
        holder.desc_item.setText(data.getDescription());

        holder.desc_item.setSelected(true);
        holder.desc_item.setFocusable(true);
        holder.desc_item.setFocusableInTouchMode(true);

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, ProjectView.class);
                myIntent.putExtra("url",data.getUrl());
                myIntent.putExtra("title", data.getTitle());
                context.startActivity(myIntent);
            }
        });


    }

    public void randomize(){
        Collections.shuffle(mDataset);

    }
    public void clear(){
        mDataset.clear();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView star_count;
        public TextView fork_count;
        public TextView desc_item;
        public CardView main_view;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.name);
            star_count = (TextView) v.findViewById(R.id.star_num);
            fork_count = (TextView) v.findViewById(R.id.fork_num);
            desc_item = (TextView) v.findViewById(R.id.desc);

            main_view = (CardView) v.findViewById(R.id.card_view);

        }
    }
}
