package com.example.msti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.msti.model.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import static com.example.msti.staticStrings.ENROLLMENT_NO;
import static com.example.msti.staticStrings.FILE_NAME;
import static com.example.msti.staticStrings.GENDER;
import static com.example.msti.staticStrings.LOGIN_ENROLL;
import static com.example.msti.staticStrings.PHONE_NO;
import static com.example.msti.staticStrings.PROFILE_STATUS;
import static com.example.msti.staticStrings.USERS;
import static com.example.msti.staticStrings.USER_KEY;

public class OTPActivity extends AppCompatActivity {
    private String verificationid;
    private FirebaseAuth mAuth;
    private EditText editText;
    private ProgressDialog progress;

    String phonenumber;

    private FirebaseFirestore db;
    private FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        progress = new ProgressDialog(OTPActivity.this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        database = FirebaseFirestore.getInstance();

        editText = findViewById(R.id.editTextCode);

        phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if ((code.isEmpty() || code.length() < 6)) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        progress.setTitle("Processing");
        progress.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.collection(LOGIN_ENROLL)
                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int flag = 0;
                                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                                        if (d.get(PHONE_NO).toString().equals(phonenumber)) {
                                            Log.d(staticStrings.TAG, "onSuccess: " + phonenumber + d.get(PHONE_NO).toString());
                                            flag = 1;
                                            String e = d.get(ENROLLMENT_NO).toString();
                                            String g = d.get(GENDER).toString();
                                            String U_G;
                                            if (g.equals("Male"))
                                                U_G = "MALE_USER";
                                            else
                                                U_G = "FEMALE_USER";

                                            //saving data to sharedPreferences
                                            database.collection(USERS).document(g).collection(U_G)
                                                    .document(e).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    userData u;
                                                    if (documentSnapshot.exists()) {
                                                        u = documentSnapshot.toObject(userData.class);
                                                        Gson gson = new Gson();
                                                        String jsonStr = gson.toJson(u, userData.class);
                                                        SharedPreferences.Editor editor =
                                                                getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
                                                        editor.putString(USER_KEY, jsonStr);
                                                        editor.putString(PROFILE_STATUS, "CREATED");
                                                        editor.apply();
                                                        hideProgress();
                                                        Intent i = new Intent(OTPActivity.this, MainActivity.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progress.dismiss();
                                                    Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                    if (flag == 0) {
                                        progress.dismiss();
                                        Intent i = new Intent(OTPActivity.this, Create_Profile.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }).

                                    addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progress.dismiss();
                                            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            progress.dismiss();
                            Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    public void hideProgress() {
        if (progress != null) {
            if (progress.isShowing()) { //check if dialog is showing.

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) progress.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                        progress.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    progress.dismiss();
            }
            progress = null;
        }
    }


    private void sendVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}

