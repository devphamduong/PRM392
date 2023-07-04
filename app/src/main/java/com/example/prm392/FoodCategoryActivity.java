package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.databinding.ActivityFoodCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodCategoryActivity extends AppCompatActivity {
    ActivityFoodCategoryBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            GoToLogin();
        } else {
            Intent intent = getIntent();
            int categoryId = intent.getIntExtra("categoryId", 0);
            ArrayList<Food> foods = new ArrayList<>();
            FoodCategoryAdapter adapter = new FoodCategoryAdapter(foods);
            RecyclerView rec = binding.rvCategoryFoods;
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rec.setLayoutManager(layoutManager);
            rec.setAdapter(adapter);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.placeholderText.setVisibility(View.VISIBLE);
            mDatabase.child("Foods").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    foods.clear();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.placeholderText.setVisibility(View.VISIBLE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Food food = ds.getValue(Food.class);
                        if (food.getIsEnabled() && food.getCategoryId() == categoryId) {
                            foods.add(food);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.placeholderText.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void GoToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}