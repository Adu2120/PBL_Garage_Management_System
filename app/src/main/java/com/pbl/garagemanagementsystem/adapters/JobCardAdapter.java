package com.pbl.garagemanagementsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.garagemanagementsystem.R;

public class JobCardAdapter extends RecyclerView.Adapter<JobCardAdapter.ComplaintViewHolder> {
    private final ArrayList<String> cComplaintList;
    private OnItemClickListener mListener;

    //To delete the complaint from RecyclerView
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    //set the listener for above interface
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    //Constructor
    public JobCardAdapter(ArrayList<String> cComplaint) {
        this.cComplaintList = cComplaint;
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {
        public TextView text_complaint;
        public ImageView delete_complaint;

        public ComplaintViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            text_complaint = itemView.findViewById(R.id.text);
            delete_complaint = itemView.findViewById(R.id.button_delete);

            delete_complaint.setOnClickListener(view -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card_view, parent, false);
        return new ComplaintViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        String currentItem = cComplaintList.get(position);
        holder.text_complaint.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return cComplaintList.size();
    }

}
