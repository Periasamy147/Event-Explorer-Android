package com.example.eventexplorer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventexplorer.databinding.ActivityMainBinding;

public class mainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragmentWithSlideIn(new homeFragment()); // Initial fragment

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                replaceFragmentWithSlideIn(new homeFragment());
            } else if (itemId == R.id.nav_search) {
                replaceFragmentWithSlideIn(new searchFragment());
            } else if (itemId == R.id.nav_profile) {
                replaceFragmentWithSlideIn(new profileFragment());
            }
            return true;
        });
    }

    private void replaceFragmentWithSlideIn(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack for fragment navigation
        fragmentTransaction.commit();
    }
}
