package com.example.prm392;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.prm392.databinding.ActivityFoodEditBinding;

public class FoodEditActivity extends Activity {
    ActivityFoodEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);

        binding = ActivityFoodEditBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            Food food = (Food) bundle.getSerializable("food");
            binding.img.setImageResource(food.getImage());
            binding.edtName.setText(food.getName());
            binding.edtDescription.setText(food.getDescription());
        }
//        finish();
    }
}