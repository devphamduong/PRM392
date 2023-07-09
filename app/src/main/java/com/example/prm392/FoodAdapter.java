package com.example.prm392;

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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {
    private ArrayList<Food> foods;

    public FoodAdapter(ArrayList<Food> list) {
        this.foods = list;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food, parent, false);
        return new FoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load(foods.get(position).getImage())
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(holder.image);
        holder.tv_name.setText(foods.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food(foods.get(position).getId(), foods.get(position).getImage(), foods.get(position).getName(), foods.get(position).getCalories(), foods.get(position).getCarbs(), foods.get(position).getFat(), foods.get(position).getProtein(), foods.get(position).getDescription(), foods.get(position).getCategoryId());
                Intent intent = new Intent(v.getContext(), FoodDetailsActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("food", food);
                intent.putExtra("data", data);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class FoodHolder extends RecyclerView.ViewHolder { //đại diện cho layout row_chapter
        ImageView image;
        TextView tv_name;
        TextView tv_description;

        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.txt_name);
        }
    }
}
