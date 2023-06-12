package com.application.easycook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.application.easycook.databinding.ActivityHomeBinding;

public class PantryActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
//        replaceFragment(new PantryFragment());
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.navigation_home:
//                    replaceFragment(new HomeFragment());
//                    break;
//                case R.id.navigation_riceps:
//                    replaceFragment(new RecipeFragment());
//                    break;
//
//
//            }
//
//
//            return true;
//        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_layout,fragment);
        fragmentTransaction.commit();
    }
}