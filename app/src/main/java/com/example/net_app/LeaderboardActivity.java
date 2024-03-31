package com.example.net_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private ListView leaderboardListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_page);

        leaderboardListView = findViewById(R.id.leaderboardListView);

        List<Student> students = new ArrayList<>();
        students.add(new Student("John", 95));
        students.add(new Student("Emma", 88));
        students.add(new Student("Liam", 92));
        students.add(new Student("Olivia", 90));
        students.add(new Student("James", 85));

        // Sort students by score
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return s2.getScore() - s1.getScore();
            }
        });

        List<String> leaderboardEntries = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            leaderboardEntries.add((i + 1) + ". " + students.get(i).getName() + " - " + students.get(i).getScore());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, leaderboardEntries);
        leaderboardListView.setAdapter(adapter);
    }

    private class Student {
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
}



}
