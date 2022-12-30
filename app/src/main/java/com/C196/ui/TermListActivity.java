package com.C196.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.C196.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        FloatingActionButton newTermButton = findViewById(R.id.newTermButton);
        newTermButton.setOnClickListener(view ->
                startActivity(new Intent(TermListActivity.this, TermActivity.class)));

    }
}