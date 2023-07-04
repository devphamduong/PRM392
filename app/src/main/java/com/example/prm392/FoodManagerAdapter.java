package com.example.prm392;

import android.annotation.SuppressLint;
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
    private ArrayList<Food> foods;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public FoodManagerAdapter(ArrayList<Food> list) {
        this.foods = list;
    }

    @NonNull
    @Override
    public FoodManagerAdapter.FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_manager, parent, false);
        return new FoodManagerAdapter.FoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodManagerAdapter.FoodHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.with(holder.itemView.getContext())
                .load(foods.get(position).getImage())
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(holder.image);
        holder.txt_name.setText(foods.get(position).getName());
        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food(foods.get(position).getId(), foods.get(position).getImage(), foods.get(position).getName(), foods.get(position).getDescription(), 1);
                Intent intent = new Intent(v.getContext(), FoodDetailsActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("food", food);
                intent.putExtra("data", data);
                v.getContext().startActivity(intent);
//                Navigation.findNavController(MainManagerFragment.binding.getRoot()).navigate(R.id.action_mainManagerFragment_to_foodDetailsFragment, data);
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food(foods.get(position).getId(), foods.get(position).getImage(), foods.get(position).getName(), foods.get(position).getDescription(), foods.get(position).getCategoryId());
                Intent intent = new Intent(v.getContext(), FoodEditActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("food", food);
                intent.putExtra("data", data);
                v.getContext().startActivity(intent);
//                Navigation.findNavController(MainManagerFragment.binding.getRoot()).navigate(R.id.action_mainManagerFragment_to_foodEditFragment, data);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodDeleteDialog dialog = FoodDeleteDialog.newInstance(foods.get(position).getName());
                dialog.setConfirmationListener(new FoodDeleteDialog.DeleteConfirmationListener() {
                    @Override
                    public void onConfirmDelete() {
                        // Perform the delete action here
                        mDatabase.child("Foods").child(foods.get(position).getId()).child("isEnabled").setValue(false, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
                                    Toast.makeText(v.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(v.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "FoodDeleteDialog");
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
