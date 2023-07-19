package com.example.prm392;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodManagerAdapter extends RecyclerView.Adapter<FoodManagerAdapter.FoodHolder> {
    private final ArrayList<Food> foods;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public FoodManagerAdapter(ArrayList<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_manager, parent, false);
        return new FoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Food food = foods.get(position);
        Context context = holder.itemView.getContext();

        Picasso.with(context)
                .load(food.getImage())
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(holder.image);

        holder.txt_name.setText(food.getName());

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailsActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("food", food);
                intent.putExtra("data", data);
                context.startActivity(intent);
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodEditActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("food", food);
                intent.putExtra("data", data);
                context.startActivity(intent);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodDeleteDialog dialog = FoodDeleteDialog.newInstance(food.getName());
                dialog.setConfirmationListener(new FoodDeleteDialog.DeleteConfirmationListener() {
                    @Override
                    public void onConfirmDelete() {
                        // Perform the delete action here
                        mDatabase.child("Foods").child(food.getId()).child("isEnabled").setValue(false, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
                                    Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "FoodDeleteDialog");
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
        ImageView btn_details;
        ImageView btn_edit;
        ImageView btn_delete;

        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txt_name = itemView.findViewById(R.id.txt_name);
            btn_details = itemView.findViewById(R.id.btn_details);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
