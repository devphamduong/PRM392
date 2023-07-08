package com.example.prm392;

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

import com.example.prm392.databinding.ActivityFoodAddBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAddActivity extends AppCompatActivity {
    ActivityFoodAddBinding binding;
    private DatabaseReference mDatabase;
    private int catogoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = mDatabase.child("Foods").push().getKey();
                Food food = new Food(key, binding.edtImageUrl.getText().toString().trim(), binding.edtName.getText().toString().trim(), Float.parseFloat(binding.edtCalories.getText().toString().trim()), Float.parseFloat(binding.edtCarbs.getText().toString().trim()), Float.parseFloat(binding.edtFat.getText().toString().trim()), Float.parseFloat(binding.edtProtein.getText().toString().trim()), binding.edtDescription.getText().toString().trim(), catogoryId);
                mDatabase.child("Foods").child(key).setValue(food, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            Toast.makeText(FoodAddActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FoodAddActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
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

    private void LoadImage(String imageUrl) {
        Picasso.with(getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_error_loading)
                .into(binding.previewImage);
    }
}