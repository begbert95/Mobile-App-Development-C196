package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.C196.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button courseButton = findViewById(R.id.courseButton);
        Button termButton = findViewById(R.id.termButton);
        Button assessmentButton = findViewById(R.id.assessmentButton);

        courseButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CourseListActivity.class)));
        termButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TermListActivity.class)));
        assessmentButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AssessmentListActivity.class)));
    }
}