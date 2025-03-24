package com.kailawn.recipe.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kailawn.recipe.DetailedActivity;
import com.kailawn.recipe.Model.Post_item_model;
import com.kailawn.recipe.R;

import java.util.ArrayList;
import java.util.List;

public class Post_item_adapter extends RecyclerView.Adapter<Post_item_adapter.ViewHolder> {
    private final Context context;
    private List<Post_item_model> listData; // Main list for displaying data
    private List<Post_item_model> listDataFull; // Full list for filtering

    public Post_item_adapter(Context context, List<Post_item_model> listData) {
        this.context = context;
        this.listData = listData;
        this.listDataFull = new ArrayList<>(listData); // Initialize the full list
    }

    @NonNull
    @Override
    public Post_item_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Post_item_adapter.ViewHolder holder, int position) {
        Post_item_model post_item_model = listData.get(position);
        holder.chawhmeh_hming.setText(post_item_model.getChawhmeh_hming());
        holder.desc.setText(post_item_model.getDesc());

        holder.cardView.setCardElevation(10f); // Elevation for CardView

        // Set random height for the image
        ViewGroup.LayoutParams params = holder.image.getLayoutParams();
        params.height = (int) (300 + Math.random() * 300); // Random height between 300px and 600px
        holder.image.setLayoutParams(params);

        // Load image using Glide
        Glide.with(context)
                .load(post_item_model.getImage())
                .into(holder.image);

        // Handle item click
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class)
                        .putExtra("id", post_item_model.getId())
                        .putExtra("chawhmeh_hming", post_item_model.getChawhmeh_hming())
                        .putExtra("siam_dan1", post_item_model.getSiam_dan1())
                        .putExtra("siam_dan2", post_item_model.getSiam_dan2())
                        .putExtra("image_url", post_item_model.getImage())
                        .putExtra("desc", post_item_model.getDesc());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    // Filter method to update the list based on search query
    public void filterList(String query) {
        query = query.toLowerCase().trim();
        listData.clear();
        if (query.isEmpty()) {
            listData.addAll(listDataFull); // If query is empty, show the full list
        } else {
            for (Post_item_model item : listDataFull) {
                if (item.getChawhmeh_hming().toLowerCase().contains(query) ||
                        item.getDesc().toLowerCase().contains(query)) {
                    listData.add(item); // Add items that match the query
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter of data changes
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chawhmeh_hming;
        TextView desc;
        ImageView image;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chawhmeh_hming = itemView.findViewById(R.id.ch_hming_id);
            desc = itemView.findViewById(R.id.desc_id);
            image = itemView.findViewById(R.id.image_id);
            cardView = itemView.findViewById(R.id.cardView_id);
        }
    }
}