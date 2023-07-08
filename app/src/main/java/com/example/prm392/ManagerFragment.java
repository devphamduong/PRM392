package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.databinding.FragmentManagerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerFragment extends Fragment {
    FragmentManagerBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagerFragment newInstance(String param1, String param2) {
        ManagerFragment fragment = new ManagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            GoToLogin();
        } else {
//            setSupportActionBar(binding.toolbar);
            ArrayList<Food> foods = new ArrayList<>();
            FoodManagerAdapter adapter = new FoodManagerAdapter(foods);
            RecyclerView rec = binding.rvManagerFoods;
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            rec.setLayoutManager(layoutManager);
            rec.setAdapter(adapter);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.placeholderText.setVisibility(View.VISIBLE);
            mDatabase.child("Foods").orderByChild("enabled").equalTo(true).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    foods.clear();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.placeholderText.setVisibility(View.VISIBLE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Food food = ds.getValue(Food.class);
                        if (food != null) {
                            foods.add(food);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.placeholderText.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            binding.edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchValue = s.toString().trim();
                    Query query;
                    if (searchValue.isEmpty()) {
                        // If the search value is empty, retrieve all foods
                        query = mDatabase.child("Foods");
                    } else {
                        // Perform the search based on the name property
                        query = mDatabase.child("Foods")
                                .orderByChild("name")
                                .startAt(searchValue)
                                .endAt(searchValue + "\uf8ff");
                    }
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            foods.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Food food = ds.getValue(Food.class);
                                if (food != null && food.isEnabled()) {
                                    foods.add(food);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle any errors that occur during the search operation
                        }
                    });
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // This method is called after the text has been changed.
                    // You can perform any necessary operations here.
                }
            });
            binding.btnActionAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FoodAddActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void GoToLogin() {
        Intent intent = new Intent(getActivity().getApplicationContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }
}