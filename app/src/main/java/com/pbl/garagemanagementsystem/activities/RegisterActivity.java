package com.pbl.garagemanagementsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.Users;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "UserRegisterActivity";

    private TextInputEditText CustomerName, MobileNo, Email, CarRegNo;
    private Button RegButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CustomerName = findViewById(R.id.register_customer_name);
        MobileNo = findViewById(R.id.register_email);
        Email = findViewById(R.id.register_mobile_no);
        CarRegNo = findViewById(R.id.register_car_reg_no);
    }

    public void registerUser(View view) {
        String customerName = CustomerName.getText().toString();
        String mobileNo = MobileNo.getText().toString();
        String email = Email.getText().toString();
        String carRegNo = CarRegNo.getText().toString();
        DocumentReference userRef = db.collection("Users").document(carRegNo);
        Users user = new Users(customerName, mobileNo, email, carRegNo);
        userRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(
                                RegisterActivity.this,
                                "User registered",
                                Toast.LENGTH_SHORT
                        ).show();
                        //No Need Because Previous Activity is UserActivity an we have to go there.
                        // startActivity(new Intent(RegisterActivity.this, UserActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                RegisterActivity.this,
                                "Something went wrong !",
                                Toast.LENGTH_SHORT
                        ).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}