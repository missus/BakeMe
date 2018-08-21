package com.example.android.bakeme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> mRecipes = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final ItemClickListener mClickListener;
    private final Context mContext;

    public RecipeAdapter(Context context, List<Recipe> recipes, ItemClickListener clickListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mRecipes = recipes;
        this.mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipe_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.mName.setText(recipe.getName());
        if (!TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(mContext).load(recipe.getImage()).placeholder(R.drawable.pastries).error(R.drawable.pastries).into(holder.mImage);
        } else {
            holder.mImage.setImageResource(R.drawable.pastries);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mName;
        public final ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mImage = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition());
        }
    }

    public Recipe getItem(int position) {
        return mRecipes.get(position);
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void clear() {
        mRecipes.clear();
        this.notifyDataSetChanged();
    }
}
