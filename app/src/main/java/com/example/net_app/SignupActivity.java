package com.example.net_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private EditText editTextName, editTextMajor, editTextMinor, editTextHometown, editTextInterests, editTextSiblings, editTextColor, editTextFavoriteShow, editTextFavoriteWeather;
    private Spinner spinnerClassification;
    private Button signUpButton;

    // Define the classification array directly
    private String[] classificationArray = {"Freshman", "Sophomore", "Junior", "Senior"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_signup);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Link the input fields and spinner to the layout
        editTextName = findViewById(R.id.editTextName);
        spinnerClassification = findViewById(R.id.spinnerClassification);
        editTextMajor = findViewById(R.id.editTextMajor);
        editTextMinor = findViewById(R.id.editTextMinor);
        editTextHometown = findViewById(R.id.editTextHometown);
        editTextInterests = findViewById(R.id.editTextInterest);
        editTextSiblings = findViewById(R.id.editTextSiblings);
        editTextColor = findViewById(R.id.editTextFavoriteColor);
        editTextFavoriteShow = findViewById(R.id.editTextFavoriteShow);
        editTextFavoriteWeather = findViewById(R.id.editTextFavoriteWeather);
        signUpButton = findViewById(R.id.buttonSignUp);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> classificationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classificationArray);
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(classificationAdapter);

        signUpButton.setOnClickListener(view -> {
            addUserDetailsToFirestore();
        });
    }

    private void addUserDetailsToFirestore() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", editTextName.getText().toString().trim());
        user.put("classification", spinnerClassification.getSelectedItem().toString());
        user.put("major", editTextMajor.getText().toString().trim());
        user.put("minor", editTextMinor.getText().toString().trim());
        user.put("hometown", editTextHometown.getText().toString().trim());
        user.put("interests", editTextInterests.getText().toString().trim());
        user.put("siblings", editTextSiblings.getText().toString().trim());
        user.put("favoriteColor", editTextColor.getText().toString().trim());
        user.put("favoriteShow", editTextFavoriteShow.getText().toString().trim());
        user.put("favoriteWeather", editTextFavoriteWeather.getText().toString().trim());

        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SignupActivity.this, "Details added to Firestore with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Optional: finish the current activity to prevent going back to it from the homepage
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Error adding details to Firestore", Toast.LENGTH_SHORT).show();
                    Log.e("SignupActivity", "Error adding document", e);
                });
    }
}
