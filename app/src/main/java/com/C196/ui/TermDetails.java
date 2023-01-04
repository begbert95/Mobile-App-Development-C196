package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.C196.R;
import com.C196.database.Repository;
import com.C196.entities.Course;
import com.C196.entities.Term;

import java.time.LocalDate;
import java.util.ArrayList;

public class TermDetails extends AppCompatActivity {

    EditText termIDEdit;
    EditText termTitleEdit;
    EditText termStartEdit;
    EditText termEndEdit;
    Button termSaveButton;
    Button termCancelButton;

    int id;
    String title, start, end;

    Term term;

    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        termIDEdit = findViewById(R.id.termIdEdit);
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

        termSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id == -1){
                    term = new Term(
                            0,
                            termTitleEdit.getText().toString(),
                            LocalDate.parse(termStartEdit.getText().toString()),
                            LocalDate.parse(termEndEdit.getText().toString())
                    );
                    repository.insert(term);
                    //Toast.makeText(this, "Term created", Toast.LENGTH_LONG).show();
                }
                else {
                    term = new Term(
                            Integer.parseInt(termIDEdit.getText().toString()),
                            termTitleEdit.getText().toString(),
                            LocalDate.parse(termStartEdit.getText().toString()),
                            LocalDate.parse(termEndEdit.getText().toString())
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


}