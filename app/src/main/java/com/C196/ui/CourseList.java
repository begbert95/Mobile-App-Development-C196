package com.C196.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.C196.R;
import com.C196.database.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseList extends AppCompatActivity {

    private Repository repository;
    private CourseAdapter courseAdapter;
    RecyclerView courseListRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        courseAdapter = new CourseAdapter(this);

        courseListRecycler = findViewById(R.id.courseListAssessmentRecycler);
        courseListRecycler.setAdapter(courseAdapter);
        courseListRecycler.setLayoutManager(new LinearLayoutManager(this));

        repository = new Repository(getApplication());
        courseAdapter.setmCourses(repository.getAllCourses());

        FloatingActionButton newCourseButton = findViewById(R.id.newCourseButton);
        newCourseButton.setOnClickListener(view ->
                startActivity(new Intent(CourseList.this, CourseDetails.class)));
    }

    @Override
    protected void onResume() {

        super.onResume();

        courseListRecycler.setAdapter(courseAdapter);
        courseListRecycler.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setmCourses(repository.getAllCourses());
    }
}