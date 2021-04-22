package com.pbl.garagemanagementsystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.Users;

public class UserActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String carRegno;
    CollectionReference userRef = db.collection("Users");
    TextInputEditText etCarNo;
    TextInputLayout carregistrationno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        etCarNo = findViewById(R.id.car_registration_no);
        carregistrationno = findViewById(R.id.carregistrationno);
    }

    public void findUser(View view) {
        userRef.whereEqualTo("carRegNo", etCarNo.getText().toString())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Users user = documentSnapshot.toObject(Users.class);
                        user.setCarRegNo(documentSnapshot.getId());
                        carRegno = user.getCarRegNo();
                        String comp = String.valueOf(etCarNo.getText());

                        //if TextField is null
                        if (TextUtils.isEmpty(etCarNo.getText())) {
                            carregistrationno.setError("Please enter spare");
                        }

                        if (comp.equals(user.getCarRegNo())){
                            Intent intent = new Intent(UserActivity.this,UserInfoActivity.class);
                            intent.putExtra("carNo",user.getCarRegNo());
                            startActivity(intent);
                            return;
                        }
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                    builder.setMessage("Car Not Registered.")
                            .setPositiveButton("yes", (dialog, id) -> startActivity(new Intent(UserActivity.this, RegisterActivity.class)))
                            .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error To Fetch Data", Toast.LENGTH_LONG).show());


    }

    public void registerUser(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}