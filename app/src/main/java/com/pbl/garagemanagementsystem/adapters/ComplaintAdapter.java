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
import com.pbl.garagemanagementsystem.classes.Complaint;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {
    private final ArrayList<Complaint> cComplaintList;
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
    public ComplaintAdapter(ArrayList<Complaint> cComplaint) {
        this.cComplaintList = cComplaint;
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {
        public TextView text_complaint;
        public ImageView delete_complaint;

        public ComplaintViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            text_complaint = itemView.findViewById(R.id.text_complaint);
            delete_complaint = itemView.findViewById(R.id.button_delete_complaint);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint, parent, false);
        return new ComplaintViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        Complaint currentItem = cComplaintList.get(position);

        holder.text_complaint.setText(currentItem.getComplaint());

    }

    @Override
    public int getItemCount() {
        return cComplaintList.size();
    }
}