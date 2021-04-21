package com.pbl.garagemanagementsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.garagemanagementsystem.R;

public class SpareAdapter extends RecyclerView.Adapter<SpareAdapter.SpareViewHolder> {

    private ArrayList<String> cSpareList;
    private OnItemClickListener sListener;

    public interface OnItemClickListener {
        void onClickDelete(int position);
    }

    public void setOnItemClickListener(SpareAdapter.OnItemClickListener slistener) {
        sListener = slistener;
    }

    public SpareAdapter(ArrayList<String> cSpare) {
        this.cSpareList = cSpare;
    }

    public class SpareViewHolder extends RecyclerView.ViewHolder {
        public TextView text_spare;
        public ImageView delete_spare;

        public SpareViewHolder(@NonNull View itemView, final SpareAdapter.OnItemClickListener slistener) {
            super(itemView);
            text_spare = itemView.findViewById(R.id.text_spare);
            delete_spare = itemView.findViewById(R.id.button_delete_spare);

            delete_spare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            slistener.onClickDelete(position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public SpareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estimate_spare, parent, false);
        SpareViewHolder spareViewHolder = new SpareViewHolder(view, sListener);
        return spareViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpareAdapter.SpareViewHolder holder, int position) {
        String currentItem = cSpareList.get(position);

        holder.text_spare.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return cSpareList.size();
    }
}