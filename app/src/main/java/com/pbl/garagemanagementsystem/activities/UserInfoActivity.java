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

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String carRegNo, customerName, mobileNo, email;
    CollectionReference userRef = db.collection("Users");
    TextView CustomerName, Mobileno, CarRegNo, txtemail;
    ArrayList<Users> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        CustomerName = findViewById(R.id.customer_name);
        Mobileno = findViewById(R.id.mobile_no);
        CarRegNo = findViewById(R.id.user_car_registration_no);
        txtemail = findViewById(R.id.user_email);

        users = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        carRegNo = b.getString("carNo");
        loadUser();
    }

    public void loadUser() {
        userRef.whereEqualTo("carRegNo", carRegNo)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> { //Implemented this OnSuccessListener using Lambda Notation

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Users user = documentSnapshot.toObject(Users.class);
                        user.setCarRegNo(documentSnapshot.getId());
                        carRegNo = user.getCarRegNo();
                        customerName = user.getCustomerName();
                        mobileNo = user.getMobileNo();
                        email = user.getEmail();
                        users.add(user);
                    }
                    CustomerName.setText(customerName);
                    CarRegNo.setText(carRegNo);
                    Mobileno.setText(mobileNo);
                    txtemail.setText(email);
                })
                //Implemented this OnFailureListener using Lambda Notation
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error To Fetch Data", Toast.LENGTH_LONG).show());


    }

    public void newJobCard(View view) {
        Intent intent = new Intent(this, JobcardActivity.class);
        intent.putExtra("carRegNo", carRegNo);
        intent.putExtra("name", customerName);
        intent.putExtra("phone", mobileNo);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void previous(View view) {
        Intent intent = new Intent(this, PreviousServicesActivity.class);
        intent.putExtra("carRegNo", carRegNo);
        intent.putExtra("name", customerName);
        intent.putExtra("phone", mobileNo);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}