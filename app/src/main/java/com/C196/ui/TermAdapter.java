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
import com.C196.entities.Term;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    class TermViewHolder extends RecyclerView.ViewHolder{
        private final TextView termItemView;

        private TermViewHolder(View itemView){
            super(itemView);
            termItemView = itemView.findViewById(R.id.termTextView);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Term current = mTerms.get(position);

                Intent intent = new Intent(context, TermDetails.class);
                intent.putExtra("id", current.getId());
                intent.putExtra("title", current.getTitle());
                intent.putExtra("start", current.getStart());
                intent.putExtra("end", current.getEnd());
                context.startActivity(intent);
            });
        }
    }

    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflater;


    public TermAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate((R.layout.term_list_item), parent, false);

        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {

        if(mTerms == null || mTerms.isEmpty()){
            holder.termItemView.setText(R.string.no_terms);
            return;
        }

        holder.termItemView.setText(mTerms.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTerms(List<Term> terms){
        mTerms = terms;
        notifyDataSetChanged();
    }
}
