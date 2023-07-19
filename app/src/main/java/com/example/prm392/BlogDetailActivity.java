package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prm392.databinding.ActivityBlogDetailBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BlogDetailActivity extends AppCompatActivity {
    ActivityBlogDetailBinding binding;
    private DatabaseReference mDatabase;

    String id;
    String title, desc, count;
    int n_count;

    Blog blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlogDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        showData();
    }

    private void showData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            blog = (Blog) bundle.getSerializable("blog");
            Glide.with(getApplicationContext()).load(blog.getImg()).into(binding.imageView3);
            binding.textView4.setText(Html.fromHtml("<font color='B7B7B7'>By </font> <font color='#000000'>" + blog.getAuthor()));
            binding.textView5.setText(blog.getTitle());
            binding.textView6.setText(blog.getDesc());
            id = blog.getId();
            title = blog.getTitle();
            desc = blog.getDesc();
            count = blog.getShareCount();
            int i_count = Integer.parseInt(count);
            n_count = i_count + 1;
        }
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String shareBody = desc;
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share Using"));

                blog.setShareCount(String.valueOf(n_count));
                mDatabase.child("Blogs").child(id).setValue(blog);
            }
        });
        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}