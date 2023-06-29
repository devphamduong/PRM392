package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.databinding.ActivityFoodDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.noties.markwon.Markwon;

public class FoodDetailsActivity extends AppCompatActivity {
    ActivityFoodDetailsBinding binding;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            Food food = (Food) bundle.getSerializable("food");
            LoadImage(food.getImage());
            binding.txtName.setText(food.getName());
            mDatabase.child("Categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        binding.txtCategory.setText(Integer.parseInt(ds.getKey()) == food.getCategoryId() ? ds.getValue().toString() : "NaN");
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Markwon markwon = Markwon.create(this);
            markwon.setMarkdown(binding.txtDescription, food.getDescription());
        }
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LoadImage(String imageUrl) {
        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(binding.previewImage);
    }
}