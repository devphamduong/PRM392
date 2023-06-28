package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.databinding.FragmentManagerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerFragment extends Fragment {
    FragmentManagerBinding binding;
    FirebaseAuth auth;
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
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            GoToLogin();
        } else {
//            setSupportActionBar(binding.toolbar);
            ArrayList<Food> foods = new ArrayList<>();
            Food food1 = new Food(R.drawable.ic_launcher_foreground, "Pork skin", "High fat");
            Food food2 = new Food(R.drawable.ic_launcher_foreground, "Chicken breast", "What Is Chicken Breast?\n" +
                    "\n" +
                    "The chicken breast is a lean cut of meat taken from the pectoral muscle on the underside of the chicken. Each whole chicken contains one chicken breast with two halves, which are typically separated during the butchering process and sold as individual breasts. Given its desirable white meat and health benefits, boneless chicken breast meat is the most expensive cut of chicken in comparison to chicken thighs, wings, and drumsticks.\n" +
                    "\n" +
                    "This versatile cut can be grilled, baked, roasted, fried, barbecued, and boiled in countless ways. No matter the cooking method, chicken should always be handled with care, and the areas, utensils, and hands that have been in contact with the raw chicken should be washed thoroughly with soap and hot water to prevent food safety risks." +
                    "Chicken Breast Nutrition Facts\n" +
                    "The chicken breast is a particularly healthy part of this already nutritious bird, as it is low in fat and a good source of protein. The majority of chicken fat is concentrated in the skin, so chicken breasts are typically sold skinless and boneless.\n" +
                    "\n" +
                    "According to the USDA, the average 4-ounce serving size of raw boneless skinless chicken breast contains approximately:\n" +
                    "\n" +
                    "110 calories\n" +
                    "26 grams of protein\n" +
                    "1 gram of fat\n" +
                    "75 milligrams of cholesterol\n" +
                    "85 milligrams of sodium\n" +
                    "\n" +
                    "In comparison, the same portion of skin-on chicken breast—like that of a rotisserie chicken—contains 172 calories, 9.3 grams of total fat, and slightly reduced levels of protein. On average, one small chicken breast contains 55 percent of a person’s recommended protein intake, based on the suggested daily values for a 2,000 calorie diet. The exact nutritional value and calorie count of an individual chicken breast can fluctuate depending on the size and whether the bird was pasture raised, free range, cage free, or caged.");
            foods.add(food1);
            foods.add(food2);
            FoodManagerAdapter adapter = new FoodManagerAdapter(foods);
            RecyclerView rec = binding.rvManagerFoods;
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            rec.setLayoutManager(layoutManager);
            rec.setAdapter(adapter);

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