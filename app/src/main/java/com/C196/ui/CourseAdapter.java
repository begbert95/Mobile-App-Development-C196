package com.C196.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.C196.R;
import com.C196.entities.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    class CourseViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseItemView;

        private CourseViewHolder(View itemView){
            super(itemView);
            courseItemView = itemView.findViewById(R.id.courseNameTextView);
            itemView.setOnClickListener(view -> {
                int position=getAdapterPosition();
                final Course current = mCourses.get(position);
                Intent intent = new Intent(context, CourseDetails.class);
                //TODO figure out list properties
                intent.putExtra("id", current.getId());
                intent.putExtra("title", current.getTitle());
                intent.putExtra("start", current.getStart().toString());
                intent.putExtra("end", current.getEnd().toString());
                intent.putExtra("status", current.getStatus());
                intent.putExtra("instructorName", current.getInstructorName());
                intent.putExtra("instructorPhone", current.getInstructorPhone());
                intent.putExtra("instructorEmail", current.getInstructorEmail());
                context.startActivity(intent);
            });
        }
    }

    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;


    public CourseAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate((R.layout.course_list_item), parent, false);

        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        if(mCourses == null){
            holder.courseItemView.setText("<No courses created>");
            return;
        }

        Course course = mCourses.get(position);
        String title = course.getTitle();
        holder.courseItemView.setText(title);
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setmCourses(List<Course> courses){
        mCourses=courses;
        notifyDataSetChanged();
    }
}
