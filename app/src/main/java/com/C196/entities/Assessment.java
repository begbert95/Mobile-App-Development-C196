package com.C196.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Instant endDate;
    private AssessmentType type;
    private boolean completed;
    private boolean passed;
    private int courseID;

    public Assessment(String title, Instant endDate, AssessmentType type, boolean completed, boolean passed) {
        this.title = title;
        this.endDate = endDate;
        this.type = type;
        this.completed = completed;
        this.passed = passed;
    }

    public Assessment(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
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
