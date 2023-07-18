package com.example.prm392;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prm392.databinding.ActivityPasswordChangeBinding;
import com.example.prm392.databinding.ActivityProfileUpdateBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangeActivity extends AppCompatActivity {

    ActivityPasswordChangeBinding binding;
    EditText edtOldPass,edtNewPass,edt_confirmedpass;
    ImageButton btn_back;
    Button btn_changePass;
    Boolean flag;
    private FirebaseUser user;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        edtNewPass = binding.edtNewPass;
        edtOldPass = binding.edtOldPass;
        edt_confirmedpass = binding.edtConfirmPassword;
        btn_back = binding.btnBack;
        btn_changePass =binding.btnChangePass;
        progressDialog = new ProgressDialog(this);
        btn_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user==null){
                    GoToLogin();
                }
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), edtOldPass.getText().toString().trim());

// Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> reauthTask) {
                                if (!reauthTask.isSuccessful()) {
                                    Toast.makeText(PasswordChangeActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                                }else{
                                    String confirmedPass = edt_confirmedpass.getText().toString().trim();
                                    String newPassword = edtNewPass.getText().toString().trim();
                                    if(confirmedPass.equals(newPassword)){
                                        progressDialog.show();
                                        user.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> passwordTask) {
                                                        progressDialog.dismiss();
                                                        if (passwordTask.isSuccessful()) {
                                                            Toast.makeText(PasswordChangeActivity.this,"Change Password successfully",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Handle failure
                                                        Log.d("why why why",e.toString());
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(PasswordChangeActivity.this,"Wrong confirmed password",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean checkPassword(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            GoToLogin();
        }
        flag =false;
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), edtOldPass.getText().toString().trim());

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(PasswordChangeActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                            flag =true;
                        }
                    }
                });
        return flag;
    }

    private void changePassword(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String confirmedPass = edt_confirmedpass.getText().toString().trim();
        String newPassword = edtNewPass.getText().toString().trim();
        if(confirmedPass.equals(newPassword)){
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordChangeActivity.this,"Change Password successfully",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(PasswordChangeActivity.this,"Wrong confirmed password",Toast.LENGTH_SHORT).show();
        }
    }

    private void GoToLogin() {
        Intent intent = new Intent(PasswordChangeActivity.this, Login.class);
        startActivity(intent);
    }
}