package com.example.prm392;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    ArrayList<Blog> list;

    public BlogAdapter(ArrayList<Blog> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void filterList(ArrayList<Blog> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Blog blog = list.get(position);
        holder.title.setText(blog.getTitle());
        holder.date.setText(blog.getDate());
        holder.shareCount.setText(blog.getShareCount() + " Shared");
        holder.author.setText(blog.getAuthor());

        Glide.with(holder.author.getContext()).load(blog.getImg()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog blog = new Blog(list.get(position).getId(), list.get(position).getTitle(), list.get(position).getDesc(), list.get(position).getAuthor(), list.get(position).getDate(), list.get(position).getImg(), list.get(position).getShareCount(), list.get(position).getTimestamp());
                Intent intent = new Intent(v.getContext(), BlogDetailActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("blog", blog);
                intent.putExtra("data", data);
                v.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.author.getContext());
                builder.setTitle("What you want to do?");
                builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog updateDialog = new Dialog(v.getContext());
                        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        updateDialog.setCancelable(true);
                        updateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        updateDialog.setContentView(R.layout.activity_blog_update);
                        updateDialog.show();

                        EditText title = updateDialog.findViewById(R.id.txt_title);
                        EditText desc = updateDialog.findViewById(R.id.txt_description);
                        EditText author = updateDialog.findViewById(R.id.txt_author);

                        title.setText(blog.getTitle());
                        desc.setText(blog.getDesc());
                        author.setText(blog.getAuthor());

                        TextView dialogButton = updateDialog.findViewById(R.id.btn_publish);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (title.getText().toString().equals("")) {
                                    title.setError("Field is Required!!");
                                } else if (desc.getText().toString().equals("")) {
                                    desc.setError("Field is Required!!");
                                } else if (author.getText().toString().equals("")) {
                                    author.setError("Field is Required!!");
                                } else {
                                    blog.setTitle(title.getText().toString());
                                    blog.setDesc(desc.getText().toString());
                                    blog.setAuthor(author.getText().toString());
                                    FirebaseDatabase.getInstance().getReference().child("Blogs").child(blog.getId()).setValue(blog, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Context BlogDetailActivity;
                                            if (error == null) {
                                                Toast.makeText(v.getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(v.getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                        });
                    }
                });
                builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builders = new AlertDialog.Builder(holder.author.getContext());
                        builders.setTitle("Are you sure to Delete it?");
                        builders.setPositiveButton("Yes! I am Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Blogs").child(blog.getId()).removeValue();
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialogs = builders.create();
                        dialogs.show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView date, title, decs, author, shareCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_blog);
            date = itemView.findViewById(R.id.txt_date);
            title = itemView.findViewById(R.id.txt_title);
            decs = itemView.findViewById(R.id.txt_description);
            author = itemView.findViewById(R.id.txt_author);
            shareCount = itemView.findViewById(R.id.txt_share);
        }
    }
}
