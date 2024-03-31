package com.example.net_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    public static class Student {
        private String name;
        private int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    private RecyclerView leaderboardRecyclerView;
    private ArrayList<Student> studentList;
//    private StudentAdapter studentAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your student data (e.g., from a database or hardcoded list)
        studentList = new ArrayList<>();
        studentList.add(new Student("John", 90));
        studentList.add(new Student("Alice", 85));
        studentList.add(new Student("Bob", 80));
        // Add more students as needed

        // Initialize the adapter
// Initialize the adapter
//        Object studentAdapter = new StudentAdapter(studentList);

// Set the adapter to the RecyclerView
        Object studentAdapter = studentList;
        leaderboardRecyclerView.setAdapter((RecyclerView.Adapter) studentAdapter);

    }
}
