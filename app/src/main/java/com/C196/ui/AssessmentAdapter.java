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
import com.C196.entities.Assessment;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    class AssessmentViewHolder extends RecyclerView.ViewHolder{
        private final TextView assessmentNameTextView;
        private final TextView assessmentEndTextView;

        private AssessmentViewHolder(View itemView){
            super(itemView);
            assessmentNameTextView = itemView.findViewById(R.id.assessmentNameTextView);
            assessmentEndTextView = itemView.findViewById(R.id.assessmentEndTextView);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Assessment current = mAssessments.get(position);
                Intent intent = new Intent(context, AssessmentDetails.class);

                intent.putExtra("title", current.getTitle());
                intent.putExtra("end", current.getEndDate());
                intent.putExtra("type", current.getType());
                intent.putExtra("completed", current.isCompleted());
                intent.putExtra("passed", current.isPassed());
                intent.putExtra("courseID", current.getCourseID());
                context.startActivity(intent);
            });
        }
    }

    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;


    public AssessmentAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate((R.layout.assessment_list_item), parent, false);

        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {

        if(mAssessments != null){
            Assessment assessment = mAssessments.get(position);

            holder.assessmentNameTextView.setText(assessment.getTitle());
            holder.assessmentEndTextView.setText(assessment.getEndDate());
        }
        else{
            holder.assessmentNameTextView.setText("No assessment name!");
            holder.assessmentEndTextView.setText("No assessment end!");
        }

    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setmAssessments(List<Assessment> assessments){
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}
