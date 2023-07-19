package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.prm392.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Clear existing menu items
        bottomNavigationView.getMenu().clear();
        ArrayList<String> roles = new ArrayList<>();
        mDatabase.child("Roles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    roles.add(ds.getValue(String.class));
                }
                mDatabase.child("Accounts").orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Account account = dataSnapshot.getValue(Account.class);
                            switchMenuItems(bottomNavigationView, roles, account.getRoleId());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private void switchMenuItems(BottomNavigationView bottomNavigationView, ArrayList<String> roles, int roleId) {
        bottomNavigationView.getMenu().clear(); // Clear existing menu items
        switch (roles.get(roleId - 1).toLowerCase().trim()) {
            case "user":
                bottomNavigationView.inflateMenu(R.menu.bottom_menu);
                break;
            case "admin":
                bottomNavigationView.inflateMenu(R.menu.admin_bottom_menu);
                break;
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.homeFragment) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.managerFragment) {
            selectedFragment = new ManagerFragment();
        } else if (itemId == R.id.othersFragment) {
            selectedFragment = new UserManagerFragment();
        } else if (itemId == R.id.profileFragment) {
            selectedFragment = new ProfileFragment();
        }
        // It will help to replace the
        // one fragment to other.
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
        }
        return true;
    };

    private void GoToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}