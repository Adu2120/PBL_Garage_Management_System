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

import java.util.ArrayList;

public class PreviousServicesActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot> {
    FirebaseFirestore db;
    ArrayList<String> date;
    ArrayList<ArrayList<String>> complaint;

    String carRegNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_services);

        date = new ArrayList<>();
        complaint = new ArrayList<>();

        Bundle b = getIntent().getExtras();
        carRegNo = b.getString("carRegNo");

        RecyclerView recyclerView = findViewById(R.id.rc_previous_jobcard);
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
        new Handler().postDelayed(() -> {

            PreviousJobcardAdapter adapter = new PreviousJobcardAdapter(complaint, date);
            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
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
            }
        } else {
            Toast.makeText(this, "Error Occurred in DB Connection.", Toast.LENGTH_SHORT).show();
        }
    }
}