package com.C196.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.util.ArrayList;

@Entity (tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Instant start;
    private Instant end;
    private ArrayList<Course> courses;

    public Term(String title, Instant start, Instant end, ArrayList<Course> courses) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.courses = courses;
    }

    public Term(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course){
        courses.add(course);
    }

    public boolean removeCourse(Course c){
        return courses.remove(c);
    }

    public boolean isSafeToDelete(){
        return courses.isEmpty();
    }
}
