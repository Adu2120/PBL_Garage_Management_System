package com.pbl.garagemanagementsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.Users;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "UserRegisterActivity";

    private TextInputEditText CustomerName, MobileNo, Email, CarRegNo;
    TextInputLayout registercar_reg_no;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CustomerName = findViewById(R.id.register_customer_name);
        MobileNo = findViewById(R.id.register_email);
        Email = findViewById(R.id.register_mobile_no);
        CarRegNo = findViewById(R.id.register_car_reg_no);
        registercar_reg_no = findViewById(R.id.registercar_reg_no);
    }

    public void registerUser(View view) {
        registercar_reg_no.setError(null);
        String customerName = Objects.requireNonNull(CustomerName.getText()).toString();
        String mobileNo = Objects.requireNonNull(MobileNo.getText()).toString();
        String email = Objects.requireNonNull(Email.getText()).toString();
        String carRegNo = Objects.requireNonNull(CarRegNo.getText()).toString();
        DocumentReference userRef = db.collection("Users").document(carRegNo);
        if (isValidCarNo(carRegNo)) {
            Users user = new Users(customerName, mobileNo, email, carRegNo);
            userRef.set(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(
                                RegisterActivity.this,
                                "User registered",
                                Toast.LENGTH_SHORT
                        ).show();
                        //No Need Because Previous Activity is UserActivity an we have to go there.
                        // startActivity(new Intent(RegisterActivity.this, UserActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(
                                RegisterActivity.this,
                                "Something went wrong !",
                                Toast.LENGTH_SHORT
                        ).show();
                        Log.d(TAG, e.toString());
                    });
        } else {
            if (!isValidCarNo(carRegNo)) {
                registercar_reg_no.setError("Please Enter Valid Car Number.");
            } else {
                registercar_reg_no.setError(null);
            }
        }
    }

    public boolean isValidCarNo(String target) {
        String carNoPattern = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$";
        return target.matches(carNoPattern) && target.length() > 0;
    }

}