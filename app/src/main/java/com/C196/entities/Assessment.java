package com.C196.entities;

import android.app.Application;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.C196.R;
import com.C196.database.Repository;

import java.time.Instant;
import java.time.LocalDate;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private LocalDate endDate;
    private AssessmentType type;
    private boolean completed;
    private boolean passed;
    private int courseID;

    public Assessment(String title, LocalDate endDate, AssessmentType type, boolean completed, boolean passed, int courseID) {

        this.title = title;
        this.endDate = endDate;
        this.type = type;
        this.completed = completed;
        this.passed = passed;
        this.courseID = courseID;
    }

    public Assessment(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }


}
