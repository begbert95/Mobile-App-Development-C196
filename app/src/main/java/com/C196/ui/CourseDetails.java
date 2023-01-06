package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Status;
import com.C196.entities.Term;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CourseDetails extends AppCompatActivity {
    Spinner dropdown = findViewById(R.id.courseStatusDropdown);

    EditText courseIDEdit = findViewById(R.id.courseIdEdit);
    EditText courseTitleEdit = findViewById(R.id.courseTitleEdit);
    EditText courseStatusDropdown = findViewById(R.id.courseStatusDropdown);

    DatePicker courseStartDateEdit = findViewById(R.id.courseStartDate);
    DatePicker courseEndDateEdit = findViewById(R.id.courseEndDate);
    DatePickerDialog.OnDateSetListener courseStartDate;
    DatePickerDialog.OnDateSetListener courseEndDate;


    Button courseSaveButton;
    Button courseCancelButton;

    int id;
    String title, start, end;

    Term term;

    Repository repository;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        dropdown.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Status.values()));
        EditText termIDEdit;
        EditText termTitleEdit;
        DatePickerDialog.OnDateSetListener termStartDate;
        DatePickerDialog.OnDateSetListener termEndDate;
        Button termSaveButton;
        Button termCancelButton;

        int id;
        String title, start, end;

        Term term;

        Repository repository;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    }
}