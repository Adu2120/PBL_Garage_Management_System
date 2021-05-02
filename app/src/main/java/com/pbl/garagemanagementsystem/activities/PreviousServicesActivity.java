package com.pbl.garagemanagementsystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.adapters.PreviousJobcardAdapter;
import com.pbl.garagemanagementsystem.classes.JobCard;
import com.pbl.garagemanagementsystem.classes.PDFGeneration;

import java.util.ArrayList;

public class PreviousServicesActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot>, Runnable {
    FirebaseFirestore db;
    ArrayList<String> date;
    ArrayList<ArrayList<String>> complaint;
    ArrayList<ArrayList<String>> spares;
    ArrayList<Integer> TotalEstimate;
    RecyclerView recyclerView;
    String carRegNo;
    String name;
    String contactNo;
    String email;
    PDFGeneration pdfGeneration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_services);

        date = new ArrayList<>();
        complaint = new ArrayList<>();
        spares = new ArrayList<>();
        TotalEstimate = new ArrayList<>();

        Bundle b = getIntent().getExtras();
        carRegNo = b.getString("carRegNo");
        name = b.getString("name");
        contactNo = b.getString("phone");
        email = b.getString("email");


        recyclerView = findViewById(R.id.rc_previous_jobcard);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        db = FirebaseFirestore.getInstance();
        db.collection("JobCard")
                .whereEqualTo("carRegNo", carRegNo)
                .get()
                .addOnCompleteListener(this);

        Toast.makeText(this, "Please Wait for 3 Second.", Toast.LENGTH_SHORT).show();
        //Implemented Runnable using Lambda Notation
        new Handler().postDelayed(this
                , 3000);


    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                JobCard jc = document.toObject(JobCard.class);
                complaint.add(jc.getComplaints());
                date.add(jc.getDate());
                spares.add(jc.getSpares());
                TotalEstimate.add(jc.getTotalEstimate());
            }
        } else {
            Toast.makeText(this, "Error Occurred in DB Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void run() {

        PreviousJobcardAdapter adapter = new PreviousJobcardAdapter(complaint, spares, date, TotalEstimate, carRegNo, name, contactNo, email, this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}