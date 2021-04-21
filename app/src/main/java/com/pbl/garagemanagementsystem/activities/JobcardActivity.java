package com.pbl.garagemanagementsystem.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.adapters.ComplaintAdapter;
import com.pbl.garagemanagementsystem.adapters.SpareAdapter;
import com.pbl.garagemanagementsystem.classes.Complaint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobcardActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference customerDocRef = db.collection("Users")
            .document("MH-09-9719")
            .collection("Jobcards")
            .document("9LOONNrIahBCtFnBgfOQ");

    List<String> Spares = Arrays.asList("Oil", "Tyres", "Brake oil", "coolant", "tyre head", "glass", "mat change");
    private ArrayList<Complaint> mComplaintList;
    private ArrayList<String> mSpareList;
    private ComplaintAdapter mAdapter;
    private SpareAdapter mSpareAdapter;

    TextInputLayout addcomp, addmobno, addspare;
    private EditText editComplaint;
    private EditText editSpare;
    private String CComplaint;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobcard);
        addmobno = findViewById(R.id.registermobile_no);
        addcomp = findViewById(R.id.tl_customer_complaints);
        addspare = findViewById(R.id.tl_estimate_spares);
        EditText editMobile = findViewById(R.id.register_mobile_no);
        editComplaint = findViewById(R.id.edit_customer_complaints);
        editSpare = findViewById(R.id.edit_estimate_spares);
        EditText editCarRegNo = findViewById(R.id.edit_car_reg_no);
//
//        editCarRegNo.setEnabled(false);
//        editMobile.setEnabled(false);


        AutoCompleteTextView editText = findViewById(R.id.edit_estimate_spares);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Spares);
        editText.setAdapter(adapter);

        addcomp.setEndIconOnClickListener(view -> {

            if (TextUtils.isEmpty(editComplaint.getText())) {
                Toast.makeText(JobcardActivity.this, "Enter complaint", Toast.LENGTH_SHORT).show();
                addcomp.setError("Please enter complaint");

            } else {
                addcomp.setError(null);
                CComplaint = editComplaint.getText().toString();
                insertComplaint(CComplaint);
                editComplaint.setText("");
            }

        });

        addspare.setEndIconOnClickListener(view -> {
            if (TextUtils.isEmpty(editSpare.getText())) {
                Toast.makeText(JobcardActivity.this, "Enter Spare", Toast.LENGTH_SHORT).show();
                addspare.setError("Please enter spare");

            } else {
                addspare.setError(null);
                insertSpare(editSpare.getText().toString());
                editSpare.setText("");
            }
        });

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
        getEstimate();

    }


    public void insertComplaint(String complaint) {
        mComplaintList.add(new Complaint(complaint));
        mAdapter.notifyDataSetChanged();
        updateEstimate();
    }

    public void getEstimate() {
        customerDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String TotalEstimate = documentSnapshot.getString("Total estimate");
                    }

                })
                .addOnFailureListener(e -> {

                });
    }

    public void updateEstimate() {

        for (int i = 0; i < mSpareList.size(); i++) {
            final DocumentReference spareDocRef = db.collection("Inventory").document(mSpareList.get(i));

            db.runTransaction((Transaction.Function<Void>) transaction -> {
                DocumentSnapshot snapshot = transaction.get(spareDocRef);

                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                int totalEstimate = (int) snapshot.get("price");
                transaction.update(customerDocRef, "Total estimate", FieldValue.increment(totalEstimate));

                // Success
                return null;
            }).addOnSuccessListener(aVoid -> {
            })
                    .addOnFailureListener(e -> {
                    });
        }

    }

    public void insertSpare(String spare) {
        mSpareList.add(spare);
        mSpareAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Enterd", Toast.LENGTH_SHORT).show();
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

    public void buildRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.list_customer_complaints);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ComplaintAdapter(mComplaintList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView mSpareRecyclerView = findViewById(R.id.list_estimate_spares);
        mSpareRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mSpareLayoutManager = new LinearLayoutManager(this);
        mSpareAdapter = new SpareAdapter(mSpareList);
        mSpareRecyclerView.setLayoutManager(mSpareLayoutManager);
        mSpareRecyclerView.setAdapter(mSpareAdapter);

        mAdapter.setOnItemClickListener(new ComplaintAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeComplaint(position);
            }
        });

        mSpareAdapter.setOnItemClickListener(new SpareAdapter.OnItemClickListener() {
            @Override
            public void onClickDelete(int position) {
                removeSpare(position);
            }
        });
    }
}