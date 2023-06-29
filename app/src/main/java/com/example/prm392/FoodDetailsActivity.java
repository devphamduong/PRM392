package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.databinding.ActivityFoodDetailsBinding;
import com.squareup.picasso.Picasso;

import io.noties.markwon.Markwon;

public class FoodDetailsActivity extends AppCompatActivity {
    ActivityFoodDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailsBinding.inflate(getLayoutInflater());
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
            binding.txtName.setText(food.getName());
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
}