package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.C196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssessmentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        FloatingActionButton newAssessmentButton = findViewById(R.id.newAssessmentButton);
        newAssessmentButton.setOnClickListener(view ->
                startActivity(new Intent(AssessmentList.this, AssessmentDetails.class)));

    }
}