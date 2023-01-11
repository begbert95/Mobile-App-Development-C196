package com.C196.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Assessment;
import com.C196.entities.AssessmentType;
import com.C196.entities.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssessmentDetails extends AppCompatActivity {

    EditText assessmentTitleEdit;
    EditText assessmentEndEdit;


    Spinner assessmentTypeDropdown;
    Spinner assessmentCourseDropdown;

    CheckBox completedCheck;
    CheckBox passedCheck;

    Button assessmentSaveButton;
    Button assessmentCancelButton;

    DatePickerDialog.OnDateSetListener assessmentEndDate;
    final Calendar calendarEnd = Calendar.getInstance();

    /*int id;
    String title, start, end;*/

    Assessment assessment;

    Repository repository = new Repository(getApplication());
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        setupViews();


        try {
            assessment = new Assessment(
                    getIntent().getIntExtra("id", -1),
                    getIntent().getStringExtra("title"),
                    getIntent().getStringExtra("end"),
                    AssessmentType.valueOf(getIntent().getStringExtra("type")),
                    getIntent().getBooleanExtra("completed", false),
                    getIntent().getBooleanExtra("passed", false),
                    getIntent().getIntExtra("courseID", 0)
            );
        } catch (Exception e) {
            assessment = new Assessment();
        }


        if (assessment.getId() > 0) {
            populateAssessmentData();
        }

        setupListeners();


        assessmentEndDate = (view, year, monthOfYear, dayOfMonth) -> {

            calendarEnd.set(Calendar.YEAR, year);
            calendarEnd.set(Calendar.MONTH, monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            assessmentEndEdit.setText(sdf.format(calendarEnd.getTime()));
        };
    }


    private void populateAssessmentData() {
        assessmentTitleEdit.setText(assessment.getTitle());
        assessmentEndEdit.setText(assessment.getEndDate());
        completedCheck.setChecked(assessment.isCompleted());
        passedCheck.setChecked((assessment.isPassed()));

        //Assessment Type
        ArrayAdapter<AssessmentType> assessmentTypeArrayAdapter =
                new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, AssessmentType.values());
        assessmentTypeDropdown.setAdapter(assessmentTypeArrayAdapter);
        assessmentTypeDropdown.setSelection(assessmentTypeArrayAdapter.getPosition(assessment.getType()));


        //Course List
        ArrayList<String> courseNameArray = new ArrayList<>();

        for (Course c : repository.getAllCourses())
            courseNameArray.add(c.getTitle());

        ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, courseNameArray);

        assessmentCourseDropdown.setAdapter(courseArrayAdapter);
        assessmentCourseDropdown.setSelection(courseArrayAdapter.getPosition(assessment.getTitle()));
    }


    private void setupListeners() {

        //Date picker
        assessmentEndEdit.setOnClickListener(view -> {
            try {
                calendarEnd.setTime(sdf.parse(assessmentEndEdit.getText().toString()));
            } catch (ParseException e) {
                calendarEnd.setTime(new Date());
            }
            new DatePickerDialog(AssessmentDetails.this, assessmentEndDate, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                    calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
        });


        //Save button
        assessmentSaveButton.setOnClickListener(view -> {
                    assessment.setTitle(assessmentTitleEdit.getText().toString());
                    assessment.setEndDate(assessmentEndEdit.getText().toString());
                    assessment.setType(AssessmentType.valueOf(assessmentTypeDropdown.getSelectedItem().toString()));
                    assessment.setCompleted(completedCheck.isChecked());
                    assessment.setPassed(passedCheck.isChecked());


                    String courseName = assessmentCourseDropdown.getSelectedItem().toString();

                    for (Course c : repository.getAllCourses()) {
                        if (c.getTitle().equals(courseName))
                            assessment.setCourseID(c.getId());
                    }


                    if(assessment.getId() < 1){
                        repository.insert(assessment);
                        Toast.makeText(getParent(), "Assessment created", Toast.LENGTH_LONG).show();
                    }

                    else{
                        repository.update(assessment);
                        Toast.makeText(getParent(), "Assessment updated", Toast.LENGTH_LONG).show();
                    }
                }

        );

        //Cancel button
        assessmentCancelButton.setOnClickListener(view -> finish());

    }


    private void setupViews() {
        assessmentTitleEdit = findViewById(R.id.assessmentTitleEdit);
        assessmentEndEdit = findViewById(R.id.assessmentEndEdit);
        assessmentCourseDropdown = findViewById(R.id.assessmentCourseDropdown);
        assessmentTypeDropdown = findViewById(R.id.assessmentTypeDropdown);
        completedCheck = findViewById(R.id.completedCheck);
        passedCheck = findViewById(R.id.passedCheck);
        assessmentSaveButton = findViewById(R.id.assessmentSaveButton);
        assessmentCancelButton = findViewById(R.id.assessmentCancelButton);
    }
}