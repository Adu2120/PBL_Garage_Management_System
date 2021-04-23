package com.pbl.garagemanagementsystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pbl.garagemanagementsystem.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(this, UserActivity.class);
        //Implemented Runnable using Lambda Notation
        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }
                , 3000);
    }
}