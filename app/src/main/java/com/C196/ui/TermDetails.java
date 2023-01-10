package com.C196.ui;

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

    Repository repository = new Repository(getApplication());
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details);

        setupViews();

        createIntentValues();

        createListeners();

        termStartDate = (view, year, monthOfYear, dayOfMonth) -> {

            calendarStart.set(Calendar.YEAR, year);
            calendarStart.set(Calendar.MONTH, monthOfYear);
            calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            termStartEdit.setText(sdf.format(calendarStart.getTime()));
        };

        termEndDate = (view, year, monthOfYear, dayOfMonth) -> {

            calendarEnd.set(Calendar.YEAR, year);
            calendarEnd.set(Calendar.MONTH, monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            termEndEdit.setText(sdf.format(calendarEnd.getTime()));
        };
    }


    private void createIntentValues(){
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");

        termTitleEdit.setText(title);
        termStartEdit.setText(start);
        termEndEdit.setText(end);
    }

    private void setupViews(){
        termTitleEdit = findViewById(R.id.termTitleEdit);
        termStartEdit = findViewById(R.id.termStartEdit);
        termEndEdit = findViewById(R.id.termEndEdit);
        termSaveButton = findViewById(R.id.termSaveButton);
        termCancelButton = findViewById(R.id.termCancelButton);

        RecyclerView recyclerView = findViewById(R.id.termDetailCourseRecycler);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course c : repository.getAllCourses()) {
            if (c.getTermID() == id)
                filteredCourses.add(c);
        }

        courseAdapter.setmCourses(filteredCourses);

    }

    private void createListeners(){
        termStartEdit.setOnClickListener(view -> {
            String info = termStartEdit.getText().toString();

            if (!info.equals("")) {
                try {
                    calendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                calendarStart.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
            }
            new DatePickerDialog(TermDetails.this, termStartDate, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                    calendarStart.get(Calendar.DAY_OF_MONTH)).show();
        });

        termEndEdit.setOnClickListener(view -> {
            String info = termEndEdit.getText().toString();

            if (!info.equals("")) {
                try {
                    calendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                calendarEnd.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
            }
            new DatePickerDialog(TermDetails.this, termEndDate, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                    calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
        });

        termSaveButton.setOnClickListener(view -> {
            if (id == -1) {
                term = new Term(
                        0,
                        termTitleEdit.getText().toString(),
                        termStartDate.toString(),
                        termEndDate.toString()
                );
                repository.insert(term);
                Toast.makeText(getParent(), "Term created", Toast.LENGTH_LONG).show();
            } else {
                term = new Term(
                        id,
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

        termCancelButton.setOnClickListener(view -> finish());
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_details, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        Date date = null;
        Intent intent;
        PendingIntent sender;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (item.getItemId() == R.id.termDelete) {

            for (Term t : repository.getAllTerms()) {
                if (t.getId() == id) {
                    term = t;
                    break;
                }
            }

            if (term == null) {
                return false;
            }

            int courseCount = 0;

            for (Course c : repository.getAllCourses())
                if (c.getTermID() == id)
                    courseCount++;


            if (courseCount > 0)
                Toast.makeText(TermDetails.this, "Unable to delete a term with courses assigned!", Toast.LENGTH_LONG).show();
            else {
                repository.delete(term);
                Toast.makeText(TermDetails.this, term.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        else if(item.getItemId() == R.id.termNotifyStart) {
            String startDateFromScreen = termStartDate.toString();

            try {
                date = sdf.parse(startDateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            intent = new Intent(TermDetails.this, Receiver.class);
            intent.putExtra("key", startDateFromScreen + " should trigger");

            sender = PendingIntent.getBroadcast(TermDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
            return true;
        }

        else if(item.getItemId() == R.id.termNotifyEnd) {

            String endDateFromScreen = termEndDate.toString();

            try {
                date = sdf.parse(endDateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            intent = new Intent(TermDetails.this, Receiver.class);
            intent.putExtra("key", endDateFromScreen + " should trigger");

            sender = PendingIntent.getBroadcast(TermDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}