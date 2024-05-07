package com.example.eventexplorer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class eventActivity extends AppCompatActivity {

    private TextView eventNameTextView;
    private EditText numCustomersEditText;
    private TextView defaultPriceTextView, eventTotalTextView, netTotalTextView, descriptionTextView;

    private String eventName;
    private int numCustomers;
    private double eventTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Initialize views
        eventNameTextView = findViewById(R.id.textViewEventName);
        defaultPriceTextView = findViewById(R.id.default_price_textview);
        eventTotalTextView = findViewById(R.id.event_total_textview);
        netTotalTextView = findViewById(R.id.net_total_textview);
        descriptionTextView = findViewById(R.id.description_textview);
        numCustomersEditText = findViewById(R.id.num_customers_edittext);

        // Get the passed event name from the intent
        if (getIntent().hasExtra("eventName")) {
            eventName = getIntent().getStringExtra("eventName");
            eventNameTextView.setText(eventName);
        }

        // Event listener for number of customers
        numCustomersEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Update event total
                    updateEventTotal();
                    // Clear the num_customers_edittext field
                    numCustomersEditText.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    private void updateEventTotal() {
        // Get selected event price
        String[] eventPrices = getResources().getStringArray(R.array.event_prices);
        double price = Double.parseDouble(eventPrices[0]); // Assuming the default price is at index 0

        // Store event details
        eventName = eventNameTextView.getText().toString();
        try {
            numCustomers = Integer.parseInt(numCustomersEditText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            numCustomers = 0;
        }
        eventTotal = price * numCustomers;

        // Update event total TextView
        eventTotalTextView.setText("Event Total: " + eventTotal);

        // Calculate net total (for multiple events)
        // For now, net total is the sum of event totals
        // You can customize this logic based on your requirement
        double netTotal = eventTotal; // Assuming only one event for now
        // You can calculate net total for multiple events if needed
        netTotalTextView.setText("Net Total: " + netTotal);

        // Update description with event details
        String description = "Event Name: " + eventName + "\n" +
                "Number of Customers: " + numCustomers + "\n" +
                "Amount: " + eventTotal;
        descriptionTextView.setText(description);
    }
}
