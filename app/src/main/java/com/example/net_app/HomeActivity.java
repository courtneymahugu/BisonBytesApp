package com.example.net_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView lvScavengerHuntItems;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvScavengerHuntItems = findViewById(R.id.lvScavengerHuntItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvScavengerHuntItems.setAdapter(itemsAdapter);

        fetchScavengerHuntItems();
    }

    private void fetchScavengerHuntItems() {
        // Fetch from backend. This is pseudo-code, replace with actual backend call.
        // Example with static data:
        List<String> items = Arrays.asList(
                "Someone whose name starts with C",
                "Someone from Texas",
                "Someone whose favorite color is blue",
                "Someone who loves 'How to Get Away with Murder'",
                "Someone who has no siblings"
        );

        // Update the adapter with the items fetched
        itemsAdapter.clear();
        itemsAdapter.addAll(items);
        itemsAdapter.notifyDataSetChanged();
    }
}


