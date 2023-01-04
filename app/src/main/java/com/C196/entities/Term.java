package com.C196.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

@Entity (tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private LocalDate start;
    private LocalDate end;
    private ArrayList<Course> courses;


    public Term(int id, String title, LocalDate start, LocalDate end){
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public Term(String title, LocalDate start, LocalDate end, ArrayList<Course> courses) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.courses = courses;
    }
    public Term(String title, LocalDate start, LocalDate end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public Term(){}

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
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
