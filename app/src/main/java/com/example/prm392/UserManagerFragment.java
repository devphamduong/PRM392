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

import com.example.prm392.databinding.FragmentUserManagerBinding;
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
 * Use the {@link UserManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserManagerFragment extends Fragment {
    FragmentUserManagerBinding binding;

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

    public UserManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OthersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserManagerFragment newInstance(String param1, String param2) {
        UserManagerFragment fragment = new UserManagerFragment();
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
        binding = FragmentUserManagerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            GoToLogin();
        } else {
            ArrayList<Account> accounts = new ArrayList<>();
            UserManagerAdapter adapter = new UserManagerAdapter(accounts);
            RecyclerView rec= binding.rvManagerUsers;
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            rec.setLayoutManager(layoutManager);
            rec.setAdapter(adapter);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.placeholderText.setVisibility(View.VISIBLE);

            mDatabase.child("Accounts").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    accounts.clear();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.placeholderText.setVisibility(View.VISIBLE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Account account = ds.getValue(Account.class);
                        if (account != null) {
                            accounts.add(account);
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
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    String searchValue = s.toString().trim();
                    Query query;
                    if (searchValue.isEmpty()) {
                        // If the search value is empty, retrieve all foods
                        query = mDatabase.child("Accounts");
                    } else {
                        // Perform the search based on the name property
                        query = mDatabase.child("Accounts")
                                .orderByChild("email")
                                .startAt(searchValue)
                                .endAt(searchValue + "\uf8ff");
                    }
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            accounts.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Account account = ds.getValue(Account.class);
                                if (account != null) {
                                    accounts.add(account);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void afterTextChanged(Editable editable) {

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