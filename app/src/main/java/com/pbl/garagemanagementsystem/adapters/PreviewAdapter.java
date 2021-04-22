package com.pbl.garagemanagementsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.garagemanagementsystem.R;

import java.util.ArrayList;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewHolder> {
    ArrayList<String> spare;
    int[] cost;

    public PreviewAdapter(ArrayList<String> spare, int[] cost) {
        this.spare = spare;
        this.cost = cost;
    }

    @NonNull
    @Override
    public PreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spare_preview, parent, false);
        return new PreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewHolder holder, int position) {
        int Estimate = cost[position];
        String Est = String.valueOf(Estimate);
        String spareName = spare.get(position);
        holder.spareEstimate.setText(Est);
        holder.spareName.setText(spareName);
    }

    @Override
    public int getItemCount() {
        return spare.size();
    }

    public class PreviewHolder extends RecyclerView.ViewHolder{

        public TextView spareName;
        public TextView spareEstimate;

        public PreviewHolder(@NonNull View itemView) {
            super(itemView);
            spareName = itemView.findViewById(R.id.txt_spare);
            spareEstimate = itemView.findViewById(R.id.txt_Estimate);
        }
    }
}
