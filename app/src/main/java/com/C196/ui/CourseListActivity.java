package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.C196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        FloatingActionButton newCourseButton = findViewById(R.id.newCourseButton);
        newCourseButton.setOnClickListener(view -> startActivity(new Intent(CourseListActivity.this, CourseActivity.class)));
    }
}