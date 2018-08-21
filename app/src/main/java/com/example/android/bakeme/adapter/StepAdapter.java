package com.example.android.bakeme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private List<Step> mSteps = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final ItemClickListener mClickListener;
    public int mSelected = -1;

    public StepAdapter(Context context, List<Step> steps, ItemClickListener clickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mSteps = steps;
        this.mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = mSteps.get(position);
        String title = step.getShortDescription();
        if (title == null || title.isEmpty()) {
            holder.mTitle.setText(R.string.step);
        } else {
            holder.mTitle.setText(step.getShortDescription());
        }
        if (position == mSelected) {
            holder.mTitle.setBackgroundResource(R.color.colorPrimary);
        } else {
            holder.mTitle.setBackgroundResource(R.color.colorAccent);
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition());
        }
    }

    public Step getItem(int position) {
        return mSteps.get(position);
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void clear() {
        mSteps.clear();
        this.notifyDataSetChanged();
    }
}
