package com.example.prm392;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prm392.databinding.ActivityProfileUpdateBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ProfileUpdateActivity extends AppCompatActivity {

    public static final int MY_REQUEST_CODE =10;
    ActivityProfileUpdateBinding binding;
    ImageView imgAvatar;
    EditText edt_fullname;
    Button btn_update_profile;
    ImageButton btn_back;
    private Uri mUri;
    Context context;
    DatabaseReference mDatabase;

    private ProgressDialog progressDialog;

    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent == null){
                    return;
                }
                Uri uri = intent.getData();
                mUri = uri;
                try {
                    //ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mUri);
                    imgAvatar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btn_back = binding.btnBack;
        edt_fullname = binding.edtName;
        imgAvatar = binding.imgAvatar2;
        btn_update_profile = binding.btnUpdateProfile;
        context = this;
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initListner();
        setUserInformation();

    }

    private void setUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            GoToLogin();
        }
        edt_fullname.setText(user.getDisplayName());
        mUri = user.getPhotoUrl();

        Glide.with(ProfileUpdateActivity.this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imgAvatar);
    }

    private void initListner(){
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void onClickRequestPermission(){
        if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
           openGalary();
            return;
        }

       if(ProfileUpdateActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
           openGalary();
       }else{
           String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
           ProfileUpdateActivity.this.requestPermissions(permissions, MY_REQUEST_CODE);
       }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length >0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGalary();
            }
        }
    }

    private void openGalary(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));
    }

    private void onClickUpdateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            GoToLogin();
            return;
        }
        progressDialog.show();
        String strFullName = edt_fullname.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Query query = mDatabase.child("Accounts").orderByChild("email").equalTo(user.getEmail());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        // Get the child reference and update the desired field
                                        DatabaseReference childRef = childSnapshot.getRef();
                                        childRef.child("avatar").setValue(mUri+"");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(context,"Update profile successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void GoToLogin() {
        Intent intent = new Intent(ProfileUpdateActivity.this, Login.class);
        startActivity(intent);
    }

}