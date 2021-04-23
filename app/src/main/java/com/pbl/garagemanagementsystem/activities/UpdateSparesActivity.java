package com.pbl.garagemanagementsystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.Spares;

import java.util.Objects;

public class UpdateSparesActivity extends AppCompatActivity {

    public String spares, amount;
    FirebaseFirestore db;
    DocumentReference userRef;
    TextInputEditText spareName, spare_Amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spares);
        db = FirebaseFirestore.getInstance();
        spareName = findViewById(R.id.tiet_spare_name);
        spare_Amount = findViewById(R.id.tiet_spare_amount);
    }

    public void update(View view) {
        spares = Objects.requireNonNull(spareName.getText()).toString();
        amount = Objects.requireNonNull(spare_Amount.getText()).toString();

        userRef = db.collection("Inventory").document(spares);
        Spares spares1 = new Spares(spares, amount);
        userRef.set(spares1)
                //Implemented this OnSuccessListener using Lambda Notation
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(
                            UpdateSparesActivity.this,
                            "Spare Updated",
                            Toast.LENGTH_SHORT
                    ).show();
                    spareName.setText(null);
                    spare_Amount.setText(null);
                })
                //Implemented this OnFailureListener using Lambda Notation
                .addOnFailureListener(e -> Toast.makeText(
                        UpdateSparesActivity.this,
                        "Something went wrong !",
                        Toast.LENGTH_SHORT
                ).show());
    }
}