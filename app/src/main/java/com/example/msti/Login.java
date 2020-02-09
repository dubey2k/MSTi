package com.example.msti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class Login extends AppCompatActivity {

    TextInputEditText phone;
    Button loginbtn;
    private static final String FILE_NAME = "MSTI_USER_DETAILS";
    private String PROFILE_STATUS="PROFILE_CREATED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.loginphone);
        loginbtn = findViewById(R.id.loginbtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = phone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    phone.setError("Valid number is required");
                    phone.requestFocus();
                    return;
                }

                String phonenumber = "+91" + number;

                Intent intent = new Intent(Login.this, OTPActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        if(FirebaseAuth.getInstance().getCurrentUser() !=null && preferences.getString(PROFILE_STATUS,"NOT_CREATED").equals("CREATED")){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null && !preferences.getString(PROFILE_STATUS,"NOT_CREATED").equals("CREATED")) {
            Intent intent = new Intent(Login.this, Create_Profile.class);
            startActivity(intent);
            finish();
        }
    }
}
