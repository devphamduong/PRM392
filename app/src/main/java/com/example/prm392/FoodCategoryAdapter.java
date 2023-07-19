package com.example.prm392;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodHolder> {
    private final ArrayList<Food> foods;
    private final Context context;

    public FoodCategoryAdapter(ArrayList<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_category, parent, false);
        return new FoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Food food = foods.get(position);

        Picasso.with(context)
                .load(food.getImage())
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(holder.image);

        holder.txt_name.setText(food.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailsActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("food", food);
                intent.putExtra("data", data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    static class FoodHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txt_name;

        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}
