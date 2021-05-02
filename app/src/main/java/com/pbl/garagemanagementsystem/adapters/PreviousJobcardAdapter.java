package com.pbl.garagemanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.DocumentException;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.PDFGeneration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PreviousJobcardAdapter extends RecyclerView.Adapter<PreviousJobcardAdapter.PreviousHolder> implements OnCompleteListener<QuerySnapshot> {
    ArrayList<ArrayList<String>> complaint;
    ArrayList<ArrayList<String>> spares;
    ArrayList<String> date;
    ArrayList<Integer> totalEstimate;
    String carRegNo;
    String name, phone, email;
    PDFGeneration pdfGeneration;
    Context context;


    public PreviousJobcardAdapter(ArrayList<ArrayList<String>> complaint, ArrayList<ArrayList<String>> spares, ArrayList<String> date, ArrayList<Integer> totalEstimate, String carRegNo, String name, String phone, String email, Context context) {
        this.complaint = complaint;
        this.spares = spares;
        this.date = date;
        this.totalEstimate = totalEstimate;
        this.carRegNo = carRegNo;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.context = context;
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
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                pdfGeneration = new PDFGeneration(name, email, phone, carRegNo, date.get(position),spares.get(position), complaint1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pdfGeneration.GeneratePDF();
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), carRegNo+"_"+date.get(position)+".pdf");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file), "application/pdf");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            context.startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaint.size();
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {

    }

    public class PreviousHolder extends RecyclerView.ViewHolder {

        public TextView complaint2;
        public TextView date2;
        public Button btn;

        public PreviousHolder(@NonNull View itemView) {
            super(itemView);
            date2 = itemView.findViewById(R.id.txt_Date);
            complaint2 = itemView.findViewById(R.id.txt_complaint);
            btn = itemView.findViewById(R.id.view_pdf);
        }
    }
}
