package com.example.prm392;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ArrayList<Food> foods = new ArrayList<>();
        Food food1 = new Food(R.drawable.ic_launcher_foreground, "Pork skin", "High fat");
        Food food2 = new Food(R.drawable.ic_launcher_foreground, "Chicken breast", "High protein but low on fat");
        foods.add(food1);
        foods.add(food2);
        FoodAdapter adapter = new FoodAdapter(foods);
        RecyclerView rec = findViewById(R.id.rv_foods);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rec.setLayoutManager(layoutManager);
        rec.setAdapter(adapter);

//        ((Button) findViewById(R.id.btn_show_all)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}