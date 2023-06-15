package com.example.prm392;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.image.setImageResource(foods.get(position).getImage());
        holder.tv_name.setText(foods.get(position).getName());
//        holder.tv_description.setText(foods.get(position).getDescription());
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
            tv_name = itemView.findViewById(R.id.tv_name);
//            tv_description = itemView.findViewById(R.id.tv_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Toast.makeText(itemView.getContext(), "pos = " + pos, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
