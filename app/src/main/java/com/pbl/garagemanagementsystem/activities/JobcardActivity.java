package com.pbl.garagemanagementsystem.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.DocumentException;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.adapters.JobCardAdapter;
import com.pbl.garagemanagementsystem.classes.JobCard;
import com.pbl.garagemanagementsystem.classes.PDFGeneration;
import com.pbl.garagemanagementsystem.classes.Spares;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobcardActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot>, Runnable{
    List<String> Spares = Arrays.asList(
            "Bolt",
            "Brake Oil",
            "Brakes",
            "Car Wash",
            "Cleaning",
            "Coolant",
            "Cushon",
            "Foam",
            "Glass",
            "Hand Brake",
            "Head Lights",
            "Mat Change",
            "Mud Guard",
            "Oil Change",
            "Rear Mirror",
            "Screw",
            "Servicing",
            "Shiner",
            "Tires",
            "Wind Shields",
            "Wire",
            "Tire Head"
    );
    FirebaseFirestore db;
    PDFGeneration pdfGeneration;
    int num;
    int[] cost;
    int totalEstimate = 0;
    private ArrayList<String> mComplaintList;
    private ArrayList<String> mSpareList;
    private JobCardAdapter mAdapter;
    private JobCardAdapter mSpareAdapter;

    private String carRegNo, name, phone, email, date;
    TextInputLayout addcomp, addmobno, addspare;
    private EditText editComplaint;
    private EditText editSpare;
    private String CComplaint;
    TextInputEditText edit_car_reg_no;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobcard);

        //Fetch Estimate of all Required spares.
        db = FirebaseFirestore.getInstance();
        edit_car_reg_no = findViewById(R.id.edit_car_reg_no);
        addmobno = findViewById(R.id.registermobile_no);
        addcomp = findViewById(R.id.tl_customer_complaints);
        addspare = findViewById(R.id.tl_estimate_spares);
        editComplaint = findViewById(R.id.edit_customer_complaints);
        editSpare = findViewById(R.id.edit_estimate_spares);
        Bundle b = getIntent().getExtras();
        carRegNo = b.getString("carRegNo");
        name = b.getString("name");
        phone = b.getString("phone");
        email = b.getString("email");
//
//        editCarRegNo.setEnabled(false);
//        editMobile.setEnabled(false);

        //For autoComplete the text
        AutoCompleteTextView editText = findViewById(R.id.edit_estimate_spares);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Spares);
        editText.setAdapter(adapter);
        edit_car_reg_no.setText(carRegNo);

        //After adding element
        addcomp.setEndIconOnClickListener(view -> { //Implemented this OnClickListener using Lambda Notation
            //if input text is empty
            if (TextUtils.isEmpty(editComplaint.getText())) {
                addcomp.setError("Please enter complaint");

            } else {
                //after adding text field should be empty
                addcomp.setError(null);
                CComplaint = editComplaint.getText().toString();
                insertComplaint(CComplaint);
                editComplaint.setText("");
            }

        });

        //After adding element
        addspare.setEndIconOnClickListener(view -> { //Implemented this OnClickListener using Lambda Notation
            //if input text is empty
            if (TextUtils.isEmpty(editSpare.getText())) {
                Toast.makeText(JobcardActivity.this, "Enter Spare", Toast.LENGTH_SHORT).show();
                addspare.setError("Please enter spare");

            } else {
                //after adding text field should be empty
                addspare.setError(null);
                insertSpare(editSpare.getText().toString());
                editSpare.setText("");
            }
        });

        //for autocomplete the text
        editComplaint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(TextUtils.isEmpty(editComplaint.getText())))
                    addcomp.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //for autocomplete the text
        editSpare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(TextUtils.isEmpty(editSpare.getText())))
                    addspare.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        createLists();
        buildRecyclerView();


    }

    //Insert Complaint In recycler view
    public void insertComplaint(String complaint) {
        mComplaintList.add(complaint);
        mAdapter.notifyDataSetChanged();
    }

    public void insertSpare(String spare) {
        mSpareList.add(spare);
        mSpareAdapter.notifyDataSetChanged();
    }

    public void removeComplaint(int position) {
        mComplaintList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    public void removeSpare(int position) {
        mSpareList.remove(position);
        mSpareAdapter.notifyDataSetChanged();
    }

    public void createLists() {
        mComplaintList = new ArrayList<>();
        mSpareList = new ArrayList<>();
    }

    public String current_Date() {
        Date date = new Date();
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",
                Locale.getDefault());
        return sfd.format(date);
    }

    public void buildRecyclerView() {
        //Complaint RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.list_customer_complaints);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new JobCardAdapter(mComplaintList); //mComplaintList is ArrayList
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Delete the Item using
        //Implemented this JobCardAdapter.OnItemClickListener() using Lambda Notation
        mAdapter.setOnItemClickListener(new JobCardAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeComplaint(position);
            }
        });

        //Spare RecyclerView
        RecyclerView mSpareRecyclerView = findViewById(R.id.list_estimate_spares);
        mSpareRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mSpareLayoutManager = new LinearLayoutManager(this);
        mSpareAdapter = new JobCardAdapter(mSpareList); //mSpareList is ArrayList
        mSpareRecyclerView.setLayoutManager(mSpareLayoutManager);
        mSpareRecyclerView.setAdapter(mSpareAdapter);
        mSpareAdapter.setOnItemClickListener(new JobCardAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeSpare(position);
            }
        });
    }

    public void preview(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putStringArrayListExtra("Spares", mSpareList);
        startActivity(intent);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            int cnt = 0;
            for (QueryDocumentSnapshot document : task.getResult()) {
                com.pbl.garagemanagementsystem.classes.Spares spare = document.toObject(Spares.class);
                if (cnt == num) {
                    break;
                } else if (mSpareList.contains(spare.spare)) {
                    int position = mSpareList.indexOf(spare.spare);
                    cost[position] = Integer.parseInt(spare.Estimate);
                    cnt++;
                }
            }
            totalEstimate = 0;
            for (int i : cost) {
                totalEstimate += i;
            }
        } else {
            Toast.makeText(this, "Error Occurred in DB Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void submit(View view) {
        num = mSpareList.size();
        cost = new int[num];
        db.collection("Inventory")
                .get()
                .addOnCompleteListener(this);
        date = current_Date();
        pdfGeneration = new PDFGeneration(name, email, phone, carRegNo, date, mSpareList, mComplaintList);
        new Handler().postDelayed(this,3000);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        JobCard jc = new JobCard(carRegNo, date, mComplaintList, mSpareList, totalEstimate);
        db.collection("JobCard").document(date)
                .set(jc)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(
                        JobcardActivity.this,
                        "Job card Added to Database",
                        Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(e -> Toast.makeText(
                        JobcardActivity.this,
                        "Something went wrong !",
                        Toast.LENGTH_SHORT
                ).show());
        try {
            pdfGeneration.GeneratePDF();
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), carRegNo+"_"+date+".pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file), "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}