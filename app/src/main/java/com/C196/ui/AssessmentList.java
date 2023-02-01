package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.C196.R;
import com.C196.database.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssessmentList extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_list);

        RecyclerView assessmentListRecycler = findViewById(R.id.assessmentListRecycler);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        assessmentListRecycler.setAdapter(assessmentAdapter);
        assessmentListRecycler.setLayoutManager(new LinearLayoutManager(this));

        repository = new Repository(getApplication());
        assessmentAdapter.setmAssessments(repository.getAllAssessments());

        FloatingActionButton newAssessmentButton = findViewById(R.id.newAssessmentButton);
        newAssessmentButton.setOnClickListener(view ->
                startActivity(new Intent(AssessmentList.this, AssessmentDetails.class)));
    }

    @Override
    protected void onResume() {

        super.onResume();

        RecyclerView assessmentListRecycler = findViewById(R.id.assessmentListRecycler);

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        assessmentListRecycler.setAdapter(assessmentAdapter);
        assessmentListRecycler.setLayoutManager(new LinearLayoutManager(this));

        assessmentAdapter.setmAssessments(repository.getAllAssessments());
    }
}