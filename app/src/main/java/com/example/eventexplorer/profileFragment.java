package com.example.eventexplorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileFragment extends Fragment {

    FirebaseAuth auth;
    ImageView profileImage;
    TextView textViewName, textViewName2, textViewAddress1, textViewAddress2, textViewLandmark, textViewTown, textViewState, textViewPinCode;
    FirebaseUser user;
    DatabaseReference userRef;
    ImageButton set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImage = view.findViewById(R.id.imageView);
        textViewName = view.findViewById(R.id.textViewName);
        textViewName2 = view.findViewById(R.id.textViewName2);
        textViewAddress1 = view.findViewById(R.id.textViewAddress1);
        textViewAddress2 = view.findViewById(R.id.textViewAddress2);
        textViewLandmark = view.findViewById(R.id.textViewLandmark);
        textViewTown = view.findViewById(R.id.textViewTown);
        textViewState = view.findViewById(R.id.textViewState);
        textViewPinCode = view.findViewById(R.id.textViewPinCode);
        profileImage = view.findViewById(R.id.imageView);
        set = view.findViewById(R.id.settings_button);


        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("User Data").child(userId);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("User Data").child(userId);
        }

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String name2 = snapshot.child("name2").getValue(String.class);
                    String address1 = snapshot.child("address1").getValue(String.class);
                    String address2 = snapshot.child("address2").getValue(String.class);
                    String landmark = snapshot.child("landmark").getValue(String.class);
                    String town = snapshot.child("town").getValue(String.class);
                    String state = snapshot.child("state").getValue(String.class);
                    String pinCode = snapshot.child("pinCode").getValue(String.class);
                    String imageURL = snapshot.child("imageURL").getValue(String.class);

                    // Load and display profile image using Glide
                    if (getContext() != null && imageURL != null) {
                        Glide.with(getContext())
                                .load(Uri.parse(imageURL))
                                .into(profileImage);
                    }

                    // Set user data to views
                    textViewName.setText(name);
                    textViewName2.setText(name2);
                    textViewAddress1.setText(address1);
                    textViewAddress2.setText(address2);
                    textViewLandmark.setText(landmark);
                    textViewTown.setText(town);
                    textViewState.setText(state);
                    textViewPinCode.setText(pinCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle settings button click
                startActivity(new Intent(getActivity(), settingActivity.class));
            }
        });
    }

}
