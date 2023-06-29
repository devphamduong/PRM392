package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.databinding.ActivityFoodEditBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.noties.markwon.Markwon;

public class FoodEditActivity extends AppCompatActivity {
    ActivityFoodEditBinding binding;
    private DatabaseReference mDatabase;
    private int catogoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            Food food = (Food) bundle.getSerializable("food");
            LoadImage(food.getImage());
            binding.edtImageUrl.setText(food.getImage());
            binding.edtName.setText(food.getName());
            Markwon markwon = Markwon.create(this);
            markwon.setMarkdown(binding.edtDescription, food.getDescription());
            Spinner spinner = binding.categories;
            ArrayList<String> categories = new ArrayList<>();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            categories.add(0, "Category");
            mDatabase.child("Categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    categories.clear();
                    categories.add(0, "Category");
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        categories.add(ds.getValue().toString());
                    }
                    arrayAdapter.notifyDataSetChanged();
                    spinner.setSelection(food.getCategoryId());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position).equals("Category")) {
                    } else {
                        catogoryId = Integer.parseInt(String.valueOf(id));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            binding.edtImageUrl.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    LoadImage(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Food newFood = new Food();
                    newFood.setId(food.getId());
                    newFood.setName(binding.edtName.getText().toString());
                    newFood.setCategoryId(catogoryId);
                    newFood.setDescription(binding.edtDescription.getText().toString());
                    newFood.setImage(binding.edtImageUrl.getText().toString());
                    mDatabase.child("Foods").child(food.getId()).setValue(newFood, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(FoodEditActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(FoodEditActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            binding.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void LoadImage(String imageUrl) {
        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(binding.previewImage);
    }
}