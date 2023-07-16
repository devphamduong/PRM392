package com.example.prm392;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.databinding.ActivityFoodDetailsBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.noties.markwon.Markwon;

public class FoodDetailsActivity extends AppCompatActivity {
    ActivityFoodDetailsBinding binding;
    private DatabaseReference mDatabase;
    private PieChart nutrientChart;
    ArrayList<String> labels;

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
                        if (Integer.parseInt(ds.getKey()) == food.getCategoryId()) {
                            binding.txtCategory.setText(Integer.parseInt(ds.getKey()) == food.getCategoryId() ? ds.getValue().toString() : "NaN");
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            // Initialize the PieChart
            nutrientChart = binding.nutrientChart;
            nutrientChart.getDescription().setEnabled(false);

            // Calculate nutrients
            float totalCalories = food.getCalories();
            float proteinWeight = food.getProtein();
            float fatWeight = food.getFat();
            float carbsWeight = food.getCarbs();
            float totalNutrientsWeight = proteinWeight + fatWeight + carbsWeight;
            float proteinPercentage = (proteinWeight / totalNutrientsWeight) * 100;
            float fatPercentage = (fatWeight / totalNutrientsWeight) * 100;
            float carbsPercentage = (carbsWeight / totalNutrientsWeight) * 100;

            // Create an ArrayList of PieEntry objects to hold nutrient data
            ArrayList<PieEntry> entries = new ArrayList<>();
            labels = new ArrayList<>();
            if (proteinWeight > 0) {
                String proteinCaption = String.format(Locale.getDefault(), "%.1f%%", proteinPercentage);
                entries.add(new PieEntry(proteinPercentage, proteinCaption));
                labels.add(String.format(Locale.getDefault(), "Protein\n%.1f%% (" + (proteinWeight == (int) proteinWeight ? "%.0fg" : "%.2fg") + ")", proteinPercentage, proteinWeight));
            }

            if (fatWeight > 0) {
                String fatCaption = String.format(Locale.getDefault(), "%.1f%%", fatPercentage);
                entries.add(new PieEntry(fatPercentage, fatCaption));
                labels.add(String.format(Locale.getDefault(), "Fat\n%.1f%% (" + (fatWeight == (int) fatWeight ? "%.0fg" : "%.2fg") + ")", fatPercentage, fatWeight));
            }

            if (carbsWeight > 0) {
                String carbsCaption = String.format(Locale.getDefault(), "%.1f%%", carbsPercentage);
                entries.add(new PieEntry(carbsPercentage, carbsCaption));
                labels.add(String.format(Locale.getDefault(), "Carbs\n%.1f%% (" + (carbsWeight == (int) carbsWeight ? "%.0fg" : "%.2fg") + ")", carbsPercentage, carbsWeight));
            }

            // Create a PieDataSet with the entries
            PieDataSet dataSet = new PieDataSet(entries, null);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(12f);
            dataSet.setDrawValues(false);

            // Create a PieData object with the dataSet
            PieData data = new PieData(dataSet);
            nutrientChart.setData(data);
            nutrientChart.setEntryLabelColor(Color.BLACK);

            // Set the center text for total calories
            String centerText = String.format(Locale.getDefault(), "Calories\n%.0f", totalCalories);
            nutrientChart.setCenterText(centerText);
            nutrientChart.setCenterTextSize(16f);

            // Update the chart view
            nutrientChart.setData(data);
            nutrientChart.getDescription().setEnabled(false);
            nutrientChart.getLegend().setEnabled(false);
//            nutrientChart.setDrawEntryLabels(false);
            nutrientChart.setHoleColor(Color.TRANSPARENT);
            nutrientChart.setTransparentCircleColor(Color.TRANSPARENT);
            nutrientChart.setTransparentCircleAlpha(0);
            nutrientChart.setRotationEnabled(false);
            nutrientChart.animateXY(500, 500);
            nutrientChart.invalidate();

            // Create the legend
            createLegend(dataSet.getColors(), dataSet.getEntryCount());

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

    private void createLegend(List<Integer> colors, int entryCount) {
        binding.legendLayout.removeAllViews();

        for (int i = 0; i < entryCount; i++) {
            PieEntry entry = (PieEntry) nutrientChart.getData().getDataSet().getEntryForIndex(i);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(8, 0, 8, 0);

            LinearLayout legendItemLayout = new LinearLayout(this);
            legendItemLayout.setLayoutParams(layoutParams);
            legendItemLayout.setOrientation(LinearLayout.HORIZONTAL);

            ImageView icon = new ImageView(this);
            icon.setImageResource(R.drawable.ic_rectangle);
            icon.setColorFilter(colors.get(i));
            legendItemLayout.addView(icon);

            TextView label = new TextView(this);
            String labelText = labels.get(i);
            if (labelText.startsWith("Protein") || labelText.startsWith("Fat") || labelText.startsWith("Carbs")) {
                SpannableStringBuilder spannableLabel = new SpannableStringBuilder(labelText);
                spannableLabel.setSpan(new StyleSpan(Typeface.BOLD), 0, labelText.indexOf("\n"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                label.setText(spannableLabel);
            } else {
                label.setText(labelText);
            }
            legendItemLayout.addView(label);


            binding.legendLayout.addView(legendItemLayout);
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