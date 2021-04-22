package com.pbl.garagemanagementsystem.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.adapters.ComplaintAdapter;
import com.pbl.garagemanagementsystem.adapters.SpareAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobcardActivity extends AppCompatActivity {
    List<String> Spares = Arrays.asList("Oil", "Tyres", "Brake oil", "coolant", "tyre head", "glass", "mat change");
    private ArrayList<String> mComplaintList;
    private ArrayList<String> mSpareList;
    private ComplaintAdapter mAdapter;
    private SpareAdapter mSpareAdapter;

    private String carRegNo;
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
        editComplaint = findViewById(R.id.edit_customer_complaints);
        editSpare = findViewById(R.id.edit_estimate_spares);
        Bundle b = getIntent().getExtras();
        carRegNo = b.getString("carRegNo");
//
//        editCarRegNo.setEnabled(false);
//        editMobile.setEnabled(false);

        //For auto Complete the text
        AutoCompleteTextView editText = findViewById(R.id.edit_estimate_spares);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Spares);
        editText.setAdapter(adapter);

        //After adding element
        addcomp.setEndIconOnClickListener(view -> {
            //if input text is empty
            if (TextUtils.isEmpty(editComplaint.getText())) {
                //Toast.makeText(JobcardActivity.this, "Enter complaint", Toast.LENGTH_SHORT).show();
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
        addspare.setEndIconOnClickListener(view -> {
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

    public void buildRecyclerView() {
        //Complaint RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.list_customer_complaints);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ComplaintAdapter(mComplaintList); //mComplaintList is ArrayList
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //Delete the Item using
        mAdapter.setOnItemClickListener(new ComplaintAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeComplaint(position);
            }
        });

        //Spare RecyclerView
        RecyclerView mSpareRecyclerView = findViewById(R.id.list_estimate_spares);
        mSpareRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mSpareLayoutManager = new LinearLayoutManager(this);
        mSpareAdapter = new SpareAdapter(mSpareList); //mSpareList is ArrayList
        mSpareRecyclerView.setLayoutManager(mSpareLayoutManager);
        mSpareRecyclerView.setAdapter(mSpareAdapter);
        mSpareAdapter.setOnItemClickListener(new SpareAdapter.OnItemClickListener() {
            @Override
            public void onClickDelete(int position) {
                removeSpare(position);
            }
        });
    }

    public void preview(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putStringArrayListExtra("Spares", mSpareList);
        startActivity(intent);
    }
}