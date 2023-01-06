package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Alert;
import com.C196.entities.Course;
import com.C196.entities.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TermDetails extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details);

        termIDEdit = findViewById(R.id.termIdEdit);
        termTitleEdit = findViewById(R.id.termTitleEdit);
        termStartDate = findViewById(R.id.termStartDate);
        termEndDate = findViewById(R.id.termEndDate);
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
        termStartDate.setText(start);
        termEndDate.setText(end);

        termSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id == -1){
                    term = new Term(
                            0,
                            termTitleEdit.getText().toString(),
                            termStartDate.toString(),
                            termEndDate.toString()
                    );
                    repository.insert(term);
                    //Toast.makeText(this, "Term created", Toast.LENGTH_LONG).show();
                }
                else {
                    term = new Term(
                            Integer.parseInt(termIDEdit.getText().toString()),
                            termTitleEdit.getText().toString(),
                            termStartDate.toString(),
                            termEndDate.toString()
                    );
                    repository.update(term);
                    //Toast.makeText(this, "Term updated", Toast.LENGTH_LONG).show();
                }
                try {
                    this.finalize();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_delete, menu);
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
                    if (t.getId() == id) term = t;
                }
                if (term.isSafeToDelete()) {
                    repository.delete(term);
                    Toast.makeText(TermDetails.this, term.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TermDetails.this, "Unable to delete a term with courses assigned!", Toast.LENGTH_LONG).show();
                }
                return true;
            /*case R.id.termShareMenu:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;*/
            case R.id.termNotifyStart:
                Alert alert = new Alert("Term " + termIDEdit.getText().toString() + ": " + termTitleEdit.getText() + " starts today!", termStartDate.toString());
                repository.insert(alert);


                String startDateFromScreen = termStartDate.toString();

                try {
                    date = sdf.parse(startDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent = new Intent(TermDetails.this, Receiver.class);
                intent.putExtra("key", startDateFromScreen + " should trigger");
                sender = PendingIntent.getBroadcast(TermDetails.this, repository.getAllAlerts().size(), intent, PendingIntent.FLAG_IMMUTABLE);
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
                sender = PendingIntent.getBroadcast(TermDetails.this, repository.getAllAlerts().size(), intent, PendingIntent.FLAG_IMMUTABLE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}