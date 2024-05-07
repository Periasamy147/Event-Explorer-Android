package com.example.eventexplorer;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class homeFragment extends Fragment {

    private TextView event1TextView, event2TextView, event3TextView, event4TextView, event5TextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize event TextViews
        event1TextView = rootView.findViewById(R.id.event1);
        event2TextView = rootView.findViewById(R.id.event2);
        event3TextView = rootView.findViewById(R.id.event3);
        event4TextView = rootView.findViewById(R.id.event4);
        event5TextView = rootView.findViewById(R.id.event5);

        // Set click listeners for event TextViews
        event1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event1TextView.getText().toString());
            }
        });

        event2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event2TextView.getText().toString());
            }
        });

        event3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event3TextView.getText().toString());
            }
        });

        event4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event4TextView.getText().toString());
            }
        });

        event5TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event5TextView.getText().toString());
            }
        });

        return rootView;
    }

    private void redirectToEventActivity(String eventName) {
        Intent intent = new Intent(getActivity(), eventActivity.class);
        intent.putExtra("eventName", eventName); // Pass event name to EventActivity
        startActivity(intent);
    }
}
