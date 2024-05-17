package com.example.eventexplorer;

import android.content.Intent;
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

        // Initialize the initial fragment
        if (savedInstanceState == null) {
            replaceFragmentWithSlideIn(new homeFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home && !(getCurrentFragment() instanceof homeFragment)) {
                replaceFragmentWithSlideIn(new homeFragment());
            } else if (itemId == R.id.nav_search && !(getCurrentFragment() instanceof searchFragment)) {
                replaceFragmentWithSlideIn(new searchFragment());
            } else if (itemId == R.id.nav_profile && !(getCurrentFragment() instanceof profileFragment)) {
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

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frameLayout);
    }

    @Override
    public void onBackPressed() {
        // Get the fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Get the current fragment
        Fragment currentFragment = getCurrentFragment();

        // Check if the current fragment is not the HomeFragment
        if (!(currentFragment instanceof homeFragment)) {
            // Navigate to the home fragment
            replaceFragmentWithSlideIn(new homeFragment());

            // Select the home icon in the bottom navigation view
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else {
            // If it is the home fragment, close the app
            finish();
        }

        // Call the superclass implementation of onBackPressed()
        super.onBackPressed();
    }

    private void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        // Clear the activity stack and start the new activity as a new task
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
