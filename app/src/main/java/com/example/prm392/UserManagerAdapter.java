package com.example.prm392;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class UserManagerAdapter extends RecyclerView.Adapter<UserManagerAdapter.UserHolder> {
    public static final int MY_REQUEST_CODE =10;
    private ArrayList<Account> accounts;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public UserManagerAdapter(ArrayList<Account> list) {
        this.accounts = list;
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_manager, parent, false);
        return new UserManagerAdapter.UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        String encodedUri = accounts.get(position).getAvatar();
        String decodedUri = Uri.decode(encodedUri);

        Uri uri = Uri.parse(accounts.get(position).getAvatar());
        Picasso.with(holder.itemView.getContext())
                .load(Uri.parse(decodedUri))
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_avatar_default)
                .into(holder.image);
        holder.txt_name.setText(accounts.get(position).getEmail());
        //Glide.with(holder.itemView.getContext()).load(uri).placeholder(R.drawable.ic_image_loading).error(R.drawable.ic_avatar_default).into(holder.image);

        Log.d("huyhuy1", Uri.parse(decodedUri)+"");

        holder.tv_enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txt_name, tv_enabled;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txt_name = itemView.findViewById(R.id.txt_name);
            tv_enabled = itemView.findViewById(R.id.tv_enable);
        }
    }
}
