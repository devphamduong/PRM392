package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.databinding.ActivityFoodEditBinding;
import com.squareup.picasso.Picasso;

public class FoodEditActivity extends AppCompatActivity {
    ActivityFoodEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodEditBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            Food food = (Food) bundle.getSerializable("food");
            Picasso.with(getApplicationContext())
                    .load(food.getImage())
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.ic_error_loading)
                    .into(binding.img);
            binding.edtName.setText(food.getName());
            binding.edtDescription.setText(food.getDescription());
        }
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}