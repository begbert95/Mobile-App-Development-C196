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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Assessment;
import com.C196.entities.Course;
import com.C196.entities.Status;
import com.C196.entities.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseDetails extends AppCompatActivity {
    Spinner statusSpinner;
    Spinner termSpinner;

    EditText courseTitleEdit;
    EditText courseNotesEdit;
    EditText courseStartEdit;
    EditText courseEndEdit;
    EditText courseInstructorNameEdit;
    EditText courseInstructorPhoneEdit;
    EditText courseInstructorEmailEdit;

    DatePickerDialog.OnDateSetListener courseStartDate;
    DatePickerDialog.OnDateSetListener courseEndDate;

    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();

    Button courseSaveButton;
    Button courseCancelButton;


    Course course;
    Term term;
    Repository repository = new Repository(getApplication());

    @NonNull
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail);

        setupViews();


        try{
            course = new Course(
                    getIntent().getIntExtra("id", -1),
                    getIntent().getStringExtra("title"),
                    getIntent().getStringExtra("start"),
                    getIntent().getStringExtra("end"),
                    (Status) getIntent().getSerializableExtra("status"),
                    getIntent().getStringExtra("instructorName"),
                    getIntent().getStringExtra("instructorPhone"),
                    getIntent().getStringExtra("instructorEmail"),
                    getIntent().getIntExtra("termID", 0),
                    getIntent().getStringExtra("note")
            );
        }
        catch (Exception e){
            course = new Course();
        }


        courseTitleEdit.setText(course.getTitle());
        courseStartEdit.setText(course.getStart());
        courseEndEdit.setText(course.getEnd());
        courseInstructorNameEdit.setText(course.getInstructorName());
        courseInstructorEmailEdit.setText(course.getInstructorEmail());
        courseInstructorPhoneEdit.setText(course.getInstructorPhone());
        courseNotesEdit.setText(course.getNote());


        RecyclerView assessmentRecyclerView = findViewById(R.id.assessmentRecycler);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        assessmentRecyclerView.setAdapter(assessmentAdapter);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<Assessment> filteredAssessments = new ArrayList<>();

        for(Assessment a : repository.getAllAssessments())
            if(a.getCourseID() == course.getId())
                filteredAssessments.add(a);

        assessmentAdapter.setmAssessments(filteredAssessments);


        ArrayList<String> stringArrayList = new ArrayList<>();

        for(Term t : repository.getAllTerms()) {
            stringArrayList.add(t.getTitle());
            if(t.getId() == course.getTermID())
                term = t;
        }


        ArrayAdapter<String> termArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, stringArrayList);
        termSpinner.setAdapter(termArrayAdapter);
        if(term != null)
            termSpinner.setSelection(termArrayAdapter.getPosition(term.getTitle()));



        ArrayAdapter<Status> statusArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Status.values());
        statusSpinner.setAdapter(statusArrayAdapter);
        statusSpinner.setSelection(statusArrayAdapter.getPosition(course.getStatus()));


        setupListeners();


        courseStartDate = (view, year, monthOfYear, dayOfMonth) -> {

            calendarStart.set(Calendar.YEAR, year);
            calendarStart.set(Calendar.MONTH, monthOfYear);
            calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            courseStartEdit.setText(sdf.format(calendarStart.getTime()));
        };

        courseEndDate = (view, year, monthOfYear, dayOfMonth) -> {

            calendarEnd.set(Calendar.YEAR, year);
            calendarEnd.set(Calendar.MONTH, monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            courseEndEdit.setText(sdf.format(calendarEnd.getTime()));
        };
    }

    private void setupListeners(){
        courseStartEdit.setOnClickListener(view -> {

            try {
                calendarStart.setTime(sdf.parse(courseStartEdit.getText().toString()));
            } catch (ParseException e) {
                calendarStart.setTime(new Date());
            }

            new DatePickerDialog(CourseDetails.this, courseStartDate, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                    calendarStart.get(Calendar.DAY_OF_MONTH)).show();
        });
        courseEndEdit.setOnClickListener(view -> {

            try {
                calendarEnd.setTime(sdf.parse(courseEndEdit.getText().toString()));
            } catch (ParseException e) {
                calendarEnd.setTime(new Date());
            }

            new DatePickerDialog(CourseDetails.this, courseEndDate, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                    calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
        });

        courseSaveButton.setOnClickListener(view -> {
            course.setTitle(courseTitleEdit.getText().toString());
            course.setStart(courseStartEdit.getText().toString());
            course.setEnd(courseEndEdit.getText().toString());
            course.setInstructorName(courseInstructorNameEdit.getText().toString());
            course.setInstructorEmail(courseInstructorEmailEdit.getText().toString());
            course.setInstructorPhone(courseInstructorPhoneEdit.getText().toString());
            course.setStatus(Status.valueOf(statusSpinner.getSelectedItem().toString()));
            course.setNote(courseNotesEdit.getText().toString());

            Term tempTerm = repository.getAllTerms().get(termSpinner.getSelectedItemPosition());

            course.setTermID(tempTerm.getId());


            if(course.getId() == -1){
                course.setId(0);
                repository.insert(course);
                Toast.makeText(this, "Course created", Toast.LENGTH_LONG).show();

            }
            else{
                repository.update(course);
                Toast.makeText(this, "Course updated", Toast.LENGTH_LONG).show();

            }

            try {
                this.finish();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        courseCancelButton.setOnClickListener(view -> finish());
    }
    private void setupViews(){
        courseTitleEdit = findViewById(R.id.courseTitleEdit);
        courseNotesEdit = findViewById(R.id.courseNotesEdit);
        courseStartEdit = findViewById(R.id.courseStartEdit);
        courseEndEdit = findViewById(R.id.courseEndEdit);
        courseInstructorNameEdit = findViewById(R.id.courseInstructorNameEdit);
        courseInstructorPhoneEdit = findViewById(R.id.courseInstructorPhoneEdit);
        courseInstructorEmailEdit = findViewById(R.id.courseInstructorEmailEdit);
        statusSpinner = findViewById(R.id.courseStatusDropdown);
        termSpinner = findViewById(R.id.courseTermDropdown);
        courseSaveButton = findViewById(R.id.courseSaveButton);
        courseCancelButton = findViewById(R.id.courseCancelButton);

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.assessmentRecycler);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> filteredAssessments = new ArrayList<>();
        for (Assessment a : repository.getAllAssessments()) {
            if (a.getCourseID() == course.getId()) filteredAssessments.add(a);
        }
        assessmentAdapter.setmAssessments(filteredAssessments);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        Date date = null;
        Intent intent;
        PendingIntent sender;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        switch (item.getItemId()) {

            case R.id.courseDelete:
                repository.delete(course);
                Toast.makeText(CourseDetails.this, course.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                this.finish();
                return true;

            case R.id.courseShareMenu:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, courseNotesEdit.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, courseTitleEdit.getText().toString());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.courseNotifyStart:

                String startDateFromScreen = courseStartEdit.getText().toString();

                try {
                    date = sdf.parse(startDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent = new Intent(CourseDetails.this, Receiver.class);
                intent.putExtra("key", startDateFromScreen + " should trigger");

                sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
                return true;
            case R.id.courseNotifyEnd:
                String endDateFromScreen = courseEndEdit.getText().toString();

                try {
                    date = sdf.parse(endDateFromScreen);
                    intent = new Intent(CourseDetails.this, Receiver.class);
                    intent.putExtra("key", endDateFromScreen + " should trigger");

                    sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
                    return true;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

        }
        return super.onOptionsItemSelected(item);
    }
}