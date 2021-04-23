package com.pbl.garagemanagementsystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.adapters.PreviewAdapter;
import com.pbl.garagemanagementsystem.classes.Spares;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot> {

    ArrayList<String> spares;
    FirebaseFirestore db;
    int num;
    int[] cost;
    int totalEstimate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Bundle b = getIntent().getExtras();

        spares = b.getStringArrayList("Spares");
        num = spares.size();
        cost = new int[num];

        //Fetch Estimate of all Required spares.
        db = FirebaseFirestore.getInstance();
        db.collection("Inventory")
                .get()
                .addOnCompleteListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {

                                          PreviewAdapter adapter = new PreviewAdapter(spares, cost);
                                          recyclerView.setAdapter(adapter);

                                          adapter.notifyDataSetChanged();

                                          for (int i : cost){
                                              totalEstimate += i;
                                          }

                                          TextView txt_TotalEstimate = findViewById(R.id.txt_TotalEstimate);
                                          txt_TotalEstimate.setText(""+totalEstimate);
                                      }
                                  }
                , 3000);

    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            int cnt = 0;
            for (QueryDocumentSnapshot document : task.getResult()) {
                Spares spare = document.toObject(Spares.class);
                if(cnt == num){
                    break;
                }
                else if (spares.contains(spare.spare)){
                    int position = spares.indexOf(spare.spare);
                    cost[position] = Integer.parseInt(spare.Estimate);
                    cnt++;
                }
            }
        } else {
            Toast.makeText(this, "Error Occurred in DB Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view) {
        finish();
    }
}