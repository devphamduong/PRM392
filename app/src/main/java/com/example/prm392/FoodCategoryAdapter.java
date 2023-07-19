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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodHolder> {
    private ArrayList<Food> foods;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public FoodCategoryAdapter(ArrayList<Food> list) {
        this.foods = list;
    }

    @NonNull
    @Override
    public FoodCategoryAdapter.FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_category, parent, false);
        return new FoodCategoryAdapter.FoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Food food = foods.get(position);
        Picasso.with(holder.itemView.getContext())
                .load(food.getImage())
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(holder.image);
        holder.txt_name.setText(foods.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), FoodDetailsActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("food", food);
                intent.putExtra("data", data);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class FoodHolder extends RecyclerView.ViewHolder { //đại diện cho layout row_chapter
        ImageView image;
        TextView txt_name;

        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}