package com.C196.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.C196.entities.Term;

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

    Assessment assessment;

    Repository repository = new Repository(getApplication());
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_detail);

        setupViews();


        try {
            assessment = new Assessment(
                    0,
                    getIntent().getStringExtra("title"),
                    getIntent().getStringExtra("end"),
                    (AssessmentType) (getIntent().getSerializableExtra("type")),
                    getIntent().getBooleanExtra("completed", false),
                    getIntent().getBooleanExtra("passed", false),
                    getIntent().getIntExtra("courseID", 0)
            );
        } catch (Exception e) {
            assessment = new Assessment();
        }

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


        setupListeners();


        assessmentEndDate = (view, year, monthOfYear, dayOfMonth) -> {

            calendarEnd.set(Calendar.YEAR, year);
            calendarEnd.set(Calendar.MONTH, monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            assessmentEndEdit.setText(sdf.format(calendarEnd.getTime()));
        };
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
                        Toast.makeText(this, "Assessment created", Toast.LENGTH_LONG).show();

                    }

                    else{
                        repository.update(assessment);
                        Toast.makeText(this, "Assessment updated", Toast.LENGTH_LONG).show();
                    }
                    finish();
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

    public boolean onOptionsItemSelected(MenuItem item) {

        Date date = null;
        Intent intent;
        PendingIntent sender;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (item.getItemId() == R.id.assessmentDelete) {

          try{
              repository.delete(assessment);
              this.finish();
              return true;
          }catch(Exception e){
              return false;
          }
        }

        else if(item.getItemId() == R.id.assessmentNotifyEnd) {

            String endDateFromScreen = assessmentEndEdit.getText().toString();

            try {
                date = sdf.parse(endDateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            intent = new Intent(AssessmentDetails.this, Receiver.class);
            intent.putExtra("key", assessmentTitleEdit.getText().toString() + "is due today");

            sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_details, menu);
        return true;
    }
}