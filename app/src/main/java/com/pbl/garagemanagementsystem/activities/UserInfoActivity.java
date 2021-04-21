package com.pbl.garagemanagementsystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pbl.garagemanagementsystem.R;
import com.pbl.garagemanagementsystem.classes.Users;

public class UserInfoActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String carRegNo,carRegno, customerName, mobileNo, email;
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
        Toast.makeText(this, carRegNo, Toast.LENGTH_LONG).show();
        loadUser();
    }

    public void loadUser() {
        userRef.whereEqualTo("carRegNo", carRegNo)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Users user = documentSnapshot.toObject(Users.class);
                            user.setCarRegNo(documentSnapshot.getId());
                            carRegno = user.getCarRegNo();
                            customerName = user.getCustomerName();
                            mobileNo = user.getMobileNo();
                            email = user.getEmail();
                        }
                        CustomerName.setText(customerName);
                        CarRegNo.setText(carRegNo);
                        Mobileno.setText(mobileNo);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error To Fetch Data", Toast.LENGTH_LONG).show();
                    }
                });
        ;

    }

    public void newJobCard(View view) {
        startActivity(new Intent(this, JobcardActivity.class));
    }
}