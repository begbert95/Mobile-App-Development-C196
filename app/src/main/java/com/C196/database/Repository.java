package com.C196.database;

import android.app.Application;

import com.C196.dao.AlertDAO;
import com.C196.dao.AssessmentDAO;
import com.C196.dao.CourseDAO;
import com.C196.dao.TermDAO;
import com.C196.entities.Alert;
import com.C196.entities.Assessment;
import com.C196.entities.Course;
import com.C196.entities.Term;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final CourseDAO mCourseDAO;
    private final TermDAO mTermDAO;
    private final AssessmentDAO mAssessmentDAO;
    private final AlertDAO mAlertDAO;
    private ArrayList<Term> mAllTerms;
    private ArrayList<Course> mAllCourses;
    private ArrayList<Assessment> mAllAssessments;
    private ArrayList<Alert> mAllAlerts;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository (Application application){
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        mCourseDAO = db.courseDAO();
        mTermDAO = db.termDAO();
        mAssessmentDAO = db.assessmentDAO();
        mAlertDAO = db.alertDAO();
    }


    //region ****************** Terms ******************
    public ArrayList<Term> getAllTerms() {
        databaseExecutor.execute(() -> mAllTerms = mTermDAO.getAllTerms());
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        return mAllTerms;
    }
    public void insert(Term term){
        databaseExecutor.execute(() -> mTermDAO.insert(term));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void update(Term term){
        databaseExecutor.execute(() -> mTermDAO.update(term));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void delete(Term term){
        databaseExecutor.execute(() -> mTermDAO.delete(term));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    //endregion ****************** Terms ******************

    //region ****************** Courses ******************
    public ArrayList<Course> getAllCourses() {
        databaseExecutor.execute(() -> mAllCourses = mCourseDAO.getAllCourses());
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        return mAllCourses;
    }
    public void insert(Course course){
        databaseExecutor.execute(() -> mCourseDAO.insert(course));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void update(Course course){
        databaseExecutor.execute(() -> mCourseDAO.update(course));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void delete(Course course){
        databaseExecutor.execute(() -> mCourseDAO.delete(course));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    //endregion ****************** Courses ******************

    //region ****************** Assessments ******************
    public ArrayList<Assessment> getAllAssessments() {
        databaseExecutor.execute(() -> mAllAssessments = mAssessmentDAO.getAllAssessments());
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        return mAllAssessments;
    }
    public void insert(Assessment assessment){
        databaseExecutor.execute(() -> mAssessmentDAO.insert(assessment));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void update(Assessment assessment){
        databaseExecutor.execute(() -> mAssessmentDAO.update(assessment));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void delete(Assessment assessment){
        databaseExecutor.execute(() -> mAssessmentDAO.delete(assessment));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    //endregion ****************** Assessments ******************
    //region ****************** Alerts ******************

    public ArrayList<Alert> getAllAlerts() {
        databaseExecutor.execute(() -> mAllAlerts = mAlertDAO.getAllAlerts());
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        return mAllAlerts;
    }
    public void insert(Alert alert){
        databaseExecutor.execute(() -> mAlertDAO.insert(alert));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void update(Alert alert){
        databaseExecutor.execute(() -> mAlertDAO.update(alert));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void delete(Alert alert){
        databaseExecutor.execute(() -> mAlertDAO.delete(alert));

        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    //endregion ****************** Assessments ******************
}
