package com.pbl.garagemanagementsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.garagemanagementsystem.R;

import java.util.ArrayList;

public class PreviousJobcardAdapter extends RecyclerView.Adapter<PreviousJobcardAdapter.PreviousHolder> {
    ArrayList<ArrayList<String>> complaint;
    ArrayList<String> date;

    public PreviousJobcardAdapter(ArrayList<ArrayList<String>> complaint, ArrayList<String> date) {
        this.complaint = complaint;
        this.date = date;
    }

    @NonNull
    @Override
    public PreviousJobcardAdapter.PreviousHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobcard, parent, false);
        return new PreviousJobcardAdapter.PreviousHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousJobcardAdapter.PreviousHolder holder, int position) {
        String date1 = date.get(position);
        ArrayList<String> complaint1 = complaint.get(position);
        String complaint3 = complaint1.get(0);
        holder.date2.setText(date1);
        holder.complaint2.setText(complaint3);
    }

    @Override
    public int getItemCount() {
        return complaint.size();
    }

    public class PreviousHolder extends RecyclerView.ViewHolder {

        public TextView complaint2;
        public TextView date2;

        public PreviousHolder(@NonNull View itemView) {
            super(itemView);
            date2 = itemView.findViewById(R.id.txt_Date);
            complaint2 = itemView.findViewById(R.id.txt_complaint);
        }
    }
}
