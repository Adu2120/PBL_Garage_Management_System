package com.pbl.garagemanagementsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.Complaint;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {
    private ArrayList<Complaint> cComplaintList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ComplaintAdapter(ArrayList<Complaint> cComplaint) {
        this.cComplaintList = cComplaint;
    }

    public class ComplaintViewHolder extends RecyclerView.ViewHolder {
        public TextView text_complaint;
        public ImageView delete_complaint;

        public ComplaintViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            text_complaint = itemView.findViewById(R.id.text_complaint);
            delete_complaint = itemView.findViewById(R.id.button_delete_complaint);

            delete_complaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint, parent, false);
        ComplaintViewHolder complaintViewHolder = new ComplaintViewHolder(view, mListener);
        return complaintViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        Complaint currentItem = cComplaintList.get(position);

        holder.text_complaint.setText(currentItem.getcComplaint());

    }

    @Override
    public int getItemCount() {
        return cComplaintList.size();
    }
}
