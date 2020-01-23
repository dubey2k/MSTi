package com.example.msti;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.msti.model.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Create_Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 111;
    private Uri imageUri;
    private boolean imagePicked=false;

    Spinner branch, year;
    CircleImageView chooseProfilePic;
    RadioGroup Gender, College;
    Button verifyID;
    Button getStart;
    TextInputEditText enrollNO;

    List<String> branch_list;
    List<String> year_list;

    String MSIT_branch[], MSI_branch[], MSIT_year[], MSI_year[];

    public static String scanned_QR = "NOT_SCANNED";
    private String LOGIN_ENROLL = "Login_Enrollment_No";

    ArrayAdapter<String> branch_Adapter_spinner;
    ArrayAdapter<String> year_Adapter_spinner;

    FirebaseAuth auth;

    userData user;


     private FirebaseFirestore database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        database = FirebaseFirestore.getInstance();
        init();


        College.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton clgBtn = group.findViewById(checkedId);
                String college = clgBtn.getText() + "";
                Toast.makeText(Create_Profile.this, college, Toast.LENGTH_SHORT).show();
                year_list.clear();
                branch_list.clear();
                if (college.equals("MSI")) {
                    {
                        year_list.addAll(Arrays.asList(MSI_year));
                        year_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, year_list);
                        year.setAdapter(year_Adapter_spinner);
                    }

                    {
                        branch_list.addAll(Arrays.asList(MSI_branch));
                        branch_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, branch_list);
                        branch.setAdapter(branch_Adapter_spinner);
                    }

                } else if (college.equals("MSIT")) {
                    {
                        year_list.addAll(Arrays.asList(MSIT_year));
                        year_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, year_list);
                        year.setAdapter(year_Adapter_spinner);
                    }

                    {
                        branch_list.addAll(Arrays.asList(MSIT_branch));
                        branch_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, branch_list);
                        branch.setAdapter(branch_Adapter_spinner);
                    }
                }
            }
        });

        verifyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(
                        Manifest.permission.CAMERA,
                        123);
                startActivity(new Intent(Create_Profile.this, scan_qr.class));
            }
        });



        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enrollNO.getText().equals(scanned_QR) && !scanned_QR.isEmpty() && imagePicked){

                    //user=new userData();

                    CollectionReference loginEnroll = database.collection(LOGIN_ENROLL);
                    loginEnroll.add(scanned_QR).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                        }
                    });
                }
                else if(!imagePicked)
                    Toast.makeText(Create_Profile.this,"Choose Profile Picture"
                            ,Toast.LENGTH_LONG).show();

                else
                    Toast.makeText(Create_Profile.this,"Invalid Enrollment No..."
                            ,Toast.LENGTH_LONG).show();
            }
        });


        chooseProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


    }


    private void init() {
        Gender = findViewById(R.id.gender);
        College = findViewById(R.id.college);

        enrollNO = findViewById(R.id.student_enrollment);
        getStart = findViewById(R.id.getStarted);

        verifyID = findViewById(R.id.verifyID);

        branch = findViewById(R.id.Branch_spinner);
        year = findViewById(R.id.student_year);

        branch_list = new ArrayList<>();
        year_list = new ArrayList<>();

        MSI_branch = getResources().getStringArray(R.array.MSI_branch);
        MSIT_branch = getResources().getStringArray(R.array.MSIT_branch);
        MSIT_year = getResources().getStringArray(R.array.four_years);
        MSI_year = getResources().getStringArray(R.array.three_years);

        chooseProfilePic = findViewById(R.id.chooseProfilePic);

        auth=FirebaseAuth.getInstance();

    }


    private void checkPermission(String CameraService, int requestCode) {
        if (ContextCompat.checkSelfPermission(Create_Profile.this, CameraService)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(Create_Profile.this,
                    new String[] { CameraService },
                    requestCode);
        }
        else {
            Toast.makeText(Create_Profile.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Create_Profile.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(Create_Profile.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                ImageView imageView = findViewById(R.id.chooseProfilePic);
                imageView.setImageBitmap(bitmap);
                imagePicked = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
