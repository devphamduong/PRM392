package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextInputEditText edt_userName, edt_email, edt_password, edt_confirmPassword;
    ProgressBar progressBar;
    Button btn_register;
    TextView txt_loginNow, txt_passwordMismatch;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            GoToLogin();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        edt_userName = binding.edtUserName;
        edt_email = binding.edtEmail;
        edt_password = binding.edtPassword;
        edt_confirmPassword = binding.edtConfirmPassword;
        progressBar = binding.progressBar;
        btn_register = binding.btnRegister;
        txt_loginNow = binding.txtLoginNow;
        txt_passwordMismatch = binding.passwordMismatch;

        txt_loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLogin();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String userName = edt_userName.getText().toString().trim(), email = edt_email.getText().toString().trim(), password = edt_password.getText().toString().trim(), confirmPassword = edt_confirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else if (TextUtils.isEmpty(password) && password.length() < 6) {
                    Toast.makeText(Register.this, "Enter password, at least 6 characters", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else if (TextUtils.isEmpty(confirmPassword) && confirmPassword.length() < 6) {
                    Toast.makeText(Register.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    if (password.equals(confirmPassword)) {
                        txt_passwordMismatch.setVisibility(View.GONE);
                        SignUp(userName, email, password);
                    } else {
                        txt_passwordMismatch.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                }
            }
        });
    }

    public void SignUp(String userName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            String key = mDatabase.child("Accounts").push().getKey();
                            Account account = new Account(key, "https://winaero.com/blog/wp-content/uploads/2015/05/user-200.png", userName, email, password, 2);
                            mDatabase.child("Accounts").child(key).setValue(account, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        Toast.makeText(Register.this, "Account created", Toast.LENGTH_SHORT).show();
                                        GoToLogin();
                                    } else {
                                        Toast.makeText(Register.this, "Account already exists. Try logging in", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d("SIGNUP", task.getException().toString());
                        }
                    }
                });
    }

    private void GoToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}