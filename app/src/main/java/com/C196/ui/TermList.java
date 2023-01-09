package com.C196.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.C196.R;
import com.C196.database.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermList extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_list);

        RecyclerView termListRecycler = findViewById(R.id.termListCourseRecycler);
        final TermAdapter termAdapter = new TermAdapter(this);
        termListRecycler.setAdapter(termAdapter);
        termListRecycler.setLayoutManager(new LinearLayoutManager(this));

        repository = new Repository(getApplication());
        termAdapter.setTerms(repository.getAllTerms());

        FloatingActionButton newTermButton = findViewById(R.id.newTermButton);
        newTermButton.setOnClickListener(view ->
                startActivity(new Intent(TermList.this, TermDetails.class)));


    }
    @Override
    protected void onResume() {

        super.onResume();

        RecyclerView termListRecycler = findViewById(R.id.termListCourseRecycler);
        final TermAdapter termAdapter = new TermAdapter(this);
        termListRecycler.setAdapter(termAdapter);
        termListRecycler.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(repository.getAllTerms());
    }
}