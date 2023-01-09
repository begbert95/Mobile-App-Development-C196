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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Course;
import com.C196.entities.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TermDetails extends AppCompatActivity {

    EditText termIDEdit;
    EditText termTitleEdit;
    EditText termStartEdit;
    EditText termEndEdit;
    DatePickerDialog.OnDateSetListener termStartDate;
    DatePickerDialog.OnDateSetListener termEndDate;
    Button termSaveButton;
    Button termCancelButton;

    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();

    int id;
    String title, start, end;

    Term term;

    Repository repository;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details);

        termTitleEdit = findViewById(R.id.termTitleEdit);
        termStartEdit = findViewById(R.id.termStartEdit);
        termEndEdit = findViewById(R.id.termEndEdit);
        termSaveButton = findViewById(R.id.termSaveButton);
        termCancelButton = findViewById(R.id.termCancelButton);

        repository = new Repository(getApplication());

        RecyclerView recyclerView = findViewById(R.id.termDetailCourseRecycler);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Course> filteredCourses = new ArrayList<>();
        for(Course c : repository.getAllCourses()){
            if(c.getTermID() == id)
                filteredCourses.add(c);
        }

        /* TODO this needs to be back
        if(filteredCourses.isEmpty())
            filteredCourses.addAll(repository.getAllCourses());
        */
        courseAdapter.setmCourses(filteredCourses);

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");

        termIDEdit.setText(id);
        termTitleEdit.setText(title);
        termStartEdit.setText(start);
        termEndEdit.setText(end);

        termStartEdit.setOnClickListener(view -> {
            String info = termStartEdit.getText().toString();

            if(!info.equals("")){
                try{
                    calendarStart.setTime(sdf.parse(info));
                }
                catch (ParseException e){
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(TermDetails.this, termStartDate, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                    calendarStart.get(Calendar.DAY_OF_MONTH)).show();
        });
        termStartDate = (datePicker, i, i1, i2) -> {
            calendarStart.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
            termStartEdit.setText(sdf.format(calendarStart.getTime()));
        };

        termEndEdit.setOnClickListener(view -> {
            String info = termEndEdit.getText().toString();

            if(!info.equals("")){
                try{
                    calendarEnd.setTime(sdf.parse(info));
                }
                catch (ParseException e){
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(TermDetails.this, termEndDate, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                    calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
        });
        termEndDate = (datePicker, i, i1, i2) -> {
            calendarEnd.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
            termEndEdit.setText(sdf.format(calendarStart.getTime()));
        };

        termSaveButton.setOnClickListener(view -> {
            if(id == -1){
                term = new Term(
                        termTitleEdit.getText().toString(),
                        termStartDate.toString(),
                        termEndDate.toString()
                );
                repository.insert(term);
                Toast.makeText(getParent(), "Term created", Toast.LENGTH_LONG).show();
            }
            else {
                term = new Term(
                        termTitleEdit.getText().toString(),
                        termStartDate.toString(),
                        termEndDate.toString()
                );
                repository.update(term);
                Toast.makeText(getParent(), "Term updated", Toast.LENGTH_LONG).show();
            }
            try {
                finish();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        termCancelButton = findViewById(R.id.termCancelButton);
        termCancelButton.setOnClickListener(view -> finish());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_details, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        Date date = null;
        Intent intent;
        PendingIntent sender;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        switch (item.getItemId()) {

            case R.id.termDelete:
                for (Term t : repository.getAllTerms()) {
                    if (t.getId() == id)
                        term = t; break;
                }

                int courseCount = 0;

                for(Course c : repository.getAllCourses())
                    if (c.getTermID() == id)
                        courseCount++;



                if(courseCount > 0)
                    Toast.makeText(TermDetails.this, "Unable to delete a term with courses assigned!", Toast.LENGTH_LONG).show();
                else {
                    repository.delete(term);
                    Toast.makeText(TermDetails.this, term.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.termNotifyStart:

                String startDateFromScreen = termStartDate.toString();

                try {
                    date = sdf.parse(startDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent = new Intent(TermDetails.this, Receiver.class);
                intent.putExtra("key", startDateFromScreen + " should trigger");
                //TODO Fix request code
                sender = PendingIntent.getBroadcast(TermDetails.this, 15, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
                return true;
            case R.id.termNotifyEnd:
                String endDateFromScreen = termEndDate.toString();

                try {
                    date = sdf.parse(endDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent = new Intent(TermDetails.this, Receiver.class);
                intent.putExtra("key", endDateFromScreen + " should trigger");
                //TODO fix request code
                sender = PendingIntent.getBroadcast(TermDetails.this, 15, intent, PendingIntent.FLAG_IMMUTABLE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}