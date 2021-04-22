package com.pbl.garagemanagementsystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.Users;

public class UserInfoActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String carRegNo, customerName, mobileNo, email;
    CollectionReference userRef = db.collection("Users");
    TextView CustomerName, Mobileno, CarRegNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        CustomerName = findViewById(R.id.customer_name);
        Mobileno = findViewById(R.id.mobile_no);
        CarRegNo = findViewById(R.id.user_car_registration_no);

        Bundle b = getIntent().getExtras();
        carRegNo = b.getString("carNo");
        loadUser();
    }

    public void loadUser() {
        userRef.whereEqualTo("carRegNo", carRegNo)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Users user = documentSnapshot.toObject(Users.class);
                        user.setCarRegNo(documentSnapshot.getId());
                        carRegNo = user.getCarRegNo();
                        customerName = user.getCustomerName();
                        mobileNo = user.getMobileNo();
                        email = user.getEmail();
                    }
                    CustomerName.setText(customerName);
                    CarRegNo.setText(carRegNo);
                    Mobileno.setText(mobileNo);
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error To Fetch Data", Toast.LENGTH_LONG).show());


    }

    public void newJobCard(View view) {
        Intent intent = new Intent(this, JobcardActivity.class);
        intent.putExtra("carRegNo", carRegNo);
        startActivity(intent);
    }
}