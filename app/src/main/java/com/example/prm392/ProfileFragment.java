package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.prm392.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            GoToLogin();
        } else {
            binding.tvEmail.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.avatar_default).into(binding.imgAvatar);
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
