package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Assessment;
import com.C196.entities.AssessmentType;
import com.C196.entities.Course;
import com.C196.entities.Status;
import com.C196.entities.Term;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button courseButton = findViewById(R.id.courseButton);
        Button termButton = findViewById(R.id.termButton);
        Button assessmentButton = findViewById(R.id.assessmentButton);

        createLorem();

        courseButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CourseListActivity.class)));
        termButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TermList.class)));
        assessmentButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AssessmentListActivity.class)));
    }

    private void createLorem(){

        Repository repository = new Repository(getApplication());

        repository.insert(new Assessment("Assessment for Course 1", "2022-04-13 8:00", AssessmentType.OBJECTIVE, true, false, 1));
        repository.insert(new Assessment("Assessment for Course 2", "2022-04-13 8:00", AssessmentType.PERFORMANCE, true, false, 2));
        repository.insert(new Assessment("Assessment for Course 3", "2022-04-13 8:00", AssessmentType.OBJECTIVE, true, false, 3));

        repository.insert(new Term("Term 1", "2022-01-13 8:00", "2022-04-13 8:00"));
        repository.insert(new Term("Term 2", "2022-01-13 8:00", "2022-04-13 8:00"));
        repository.insert(new Term("Term 3", "2022-01-13 8:00", "2022-04-13 8:00"));

        repository.insert(new Course("Course 1", "2022-01-13 8:00", "2022-04-13 8:00", Status.InProgress, "Rusty Shackleford", "555-555-5555", "rshackleford@nsa.gov"));
        repository.insert(new Course("Course 2", "2022-01-13 8:00", "2022-04-13 8:00", Status.InProgress, "Rusty Shackleford", "555-555-5555", "rshackleford@nsa.gov"));
        repository.insert(new Course("Course 3", "2022-01-13 8:00", "2022-04-13 8:00", Status.InProgress, "Rusty Shackleford", "555-555-5555", "rshackleford@nsa.gov"));
    }
}