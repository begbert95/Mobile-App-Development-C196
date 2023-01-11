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

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Course;
import com.C196.entities.Status;
import com.C196.entities.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                    Status.valueOf(getIntent().getStringExtra("status")),
                    getIntent().getStringExtra("instructorName"),
                    getIntent().getStringExtra("instructorPhone"),
                    getIntent().getStringExtra("instructorEmail"),
                    getIntent().getIntExtra("termID", -1),
                    getIntent().getStringExtra("note")
            );
        }
        catch (Exception e){
            course = new Course();
        }


        courseStartEdit.setText(course.getStart());
        courseEndEdit.setText(course.getEnd());
        courseTitleEdit.setText(course.getTitle());
        courseInstructorNameEdit.setText(course.getInstructorName());
        courseInstructorEmailEdit.setText(course.getInstructorEmail());
        courseInstructorPhoneEdit.setText(course.getInstructorPhone());




        courseNotesEdit.setText(course.getNote());

        ArrayList<Term> termArrayList = new ArrayList<>(repository.getAllTerms());
        ArrayList<String> stringArrayList = new ArrayList<>();
        Term tempTerm = null;
        for(Term t : termArrayList) {
            stringArrayList.add(t.getTitle());
            if(t.getId() == course.getTermID())
                tempTerm = t;
        }


        ArrayAdapter<String> termArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, stringArrayList);
        termSpinner.setAdapter(termArrayAdapter);
        if(tempTerm != null)
            termSpinner.setSelection(termArrayAdapter.getPosition(tempTerm.getTitle()));



        ArrayAdapter<Status> arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Status.values());
        statusSpinner.setAdapter(arrayAdapter);
        statusSpinner.setSelection(arrayAdapter.getPosition(course.getStatus()));


        /* TODO Delete or use
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dropdown.set.setText(arrayAdapter.getItem(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                courseNotesEdit.setText("nothing selected");
            }
        });
        */

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

            if(course.getId() == -1){
                course.setId(0);
                repository.insert(course);
                Toast.makeText(getParent(), "Course created", Toast.LENGTH_LONG).show();

            }
            else{
                repository.update(course);
                Toast.makeText(getParent(), "Course updated", Toast.LENGTH_LONG).show();

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

                String startDateFromScreen = courseStartDate.toString();

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
                String endDateFromScreen = courseEndDate.toString();

                try {
                    date = sdf.parse(endDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent = new Intent(CourseDetails.this, Receiver.class);
                intent.putExtra("key", endDateFromScreen + " should trigger");

                sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}