package com.example.msti;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.msti.model.loginData;
import com.example.msti.model.userData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.msti.staticStrings.FILE_NAME;
import static com.example.msti.staticStrings.LOGIN_ENROLL;
import static com.example.msti.staticStrings.PICK_IMAGE_REQUEST;
import static com.example.msti.staticStrings.PROFILE_STATUS;
import static com.example.msti.staticStrings.USERS;
import static com.example.msti.staticStrings.USER_KEY;
import static com.example.msti.staticStrings.USER_PICTURES;
import static com.example.msti.staticStrings.scanned_QR;


public class Create_Profile extends AppCompatActivity {

    private Uri imageUri;

    private String IMAGE_URL;

    private boolean imagePicked = false;

    private StorageReference mStorageRef;

    private ProgressDialog progress;

    Spinner Branch, Year, Shift;
    CircleImageView chooseProfilePic;
    RadioGroup Gender, College;
    Button verifyID;
    Button getStart;
    TextInputEditText enrollNO;
    TextInputEditText Name;

    List<String> branch_list;
    List<String> year_list;

    String MSIT_branch[], MSI_branch[], MSIT_year[], MSI_year[];
    String selected_college;


    ArrayAdapter<String> branch_Adapter_spinner;
    ArrayAdapter<String> year_Adapter_spinner;

    FirebaseAuth auth;

    SharedPreferences.Editor editor;


    userData user;


    private FirebaseFirestore database;
    private StorageReference uploadPic;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        database = FirebaseFirestore.getInstance();
        init();
        checkPermission(
                Manifest.permission.CAMERA,
                123);


        College.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton clgBtn = group.findViewById(checkedId);
                selected_college = clgBtn.getText() + "";
                Toast.makeText(Create_Profile.this, selected_college, Toast.LENGTH_SHORT).show();
                year_list.clear();
                branch_list.clear();
                if (selected_college.equals("MSI")) {
                    {
                        year_list.addAll(Arrays.asList(MSI_year));
                        year_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, year_list);
                        Year.setAdapter(year_Adapter_spinner);
                    }

                    {
                        branch_list.addAll(Arrays.asList(MSI_branch));
                        branch_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, branch_list);
                        Branch.setAdapter(branch_Adapter_spinner);
                    }

                } else if (selected_college.equals("MSIT")) {
                    {
                        year_list.addAll(Arrays.asList(MSIT_year));
                        year_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, year_list);
                        Year.setAdapter(year_Adapter_spinner);
                    }

                    {
                        branch_list.addAll(Arrays.asList(MSIT_branch));
                        branch_Adapter_spinner = new ArrayAdapter<>(Create_Profile.this,
                                android.R.layout.simple_spinner_dropdown_item, branch_list);
                        Branch.setAdapter(branch_Adapter_spinner);
                    }
                }
            }
        });

        verifyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Create_Profile.this, scan_qr.class));
            }
        });


        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setTitle("Creating your profile");
                progress.show();
                final String name = Name.getText().toString();

                RadioButton radioButton = findViewById(Gender.getCheckedRadioButtonId());
                final String selected_gender = radioButton.getText().toString();

                final String SELECTED_USER = selected_gender.equals("Male") ? "MALE_USER" : "FEMALE_USER";

                uploadPic = mStorageRef.child(name + scanned_QR).child(name);

                final CollectionReference loginEnroll = database.collection(LOGIN_ENROLL);

                // checking for all conditions before creating database
                if (enrollNO.getText().toString().equals(scanned_QR)
                        && !scanned_QR.isEmpty() && imagePicked &&
                        !name.isEmpty() && !selected_gender.isEmpty() && !selected_college.isEmpty()) {
                    final loginData data_LOGIN = new loginData(auth.getCurrentUser().getPhoneNumber()
                            , scanned_QR, selected_gender);
// uploading image , retrieving link and creating database of user

                    uploadPic.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadPic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    IMAGE_URL = uri.toString();
                                    user = new userData(name, auth.getCurrentUser().getPhoneNumber(), scanned_QR
                                            , selected_college, Shift.getSelectedItem().toString(),
                                            Branch.getSelectedItem().toString(),
                                            Year.getSelectedItem().toString(), selected_gender,
                                            IMAGE_URL);
                                    database.collection(USERS).document(selected_gender).collection(SELECTED_USER)
                                            .document(scanned_QR).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            loginEnroll.document(scanned_QR).set(data_LOGIN).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Welcome",
                                                            Toast.LENGTH_SHORT).show();

                                                    //adding data of user to sharedPreferences
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(user, userData.class);
                                                    editor.putString(USER_KEY, json);
                                                    editor.putString(PROFILE_STATUS, "CREATED");
                                                    editor.apply();
                                                    progress.dismiss();
                                                    Intent i = new Intent(Create_Profile.this, MainActivity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progress.dismiss();
                                                    Toast.makeText(Create_Profile.this, e.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Create_Profile.this, e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.dismiss();
                            Toast.makeText(Create_Profile.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (!imagePicked) {
                    progress.dismiss();
                    Toast.makeText(Create_Profile.this, "Choose Profile Picture..."
                            , Toast.LENGTH_LONG).show();
                } else if (name.isEmpty()) {
                    progress.dismiss();
                    Toast.makeText(Create_Profile.this, "Name Required..."
                            , Toast.LENGTH_LONG).show();
                } else if (selected_gender.isEmpty()) {
                    progress.dismiss();
                    Toast.makeText(Create_Profile.this, "Select Gender..."
                            , Toast.LENGTH_LONG).show();
                } else if (selected_college.isEmpty()) {
                    progress.dismiss();
                    Toast.makeText(Create_Profile.this, "Select College..."
                            , Toast.LENGTH_LONG).show();
                } else {
                    progress.dismiss();
                    Toast.makeText(Create_Profile.this, "Invalid Enrollment No..."
                            , Toast.LENGTH_LONG).show();
                }
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
        Name = findViewById(R.id.student_name);
        getStart = findViewById(R.id.getStarted);

        verifyID = findViewById(R.id.verifyID);

        Branch = findViewById(R.id.Branch_spinner);
        Year = findViewById(R.id.student_year);
        Shift = findViewById(R.id.shift);

        branch_list = new ArrayList<>();
        year_list = new ArrayList<>();

        MSI_branch = getResources().getStringArray(R.array.MSI_branch);
        MSIT_branch = getResources().getStringArray(R.array.MSIT_branch);
        MSIT_year = getResources().getStringArray(R.array.four_years);
        MSI_year = getResources().getStringArray(R.array.three_years);

        chooseProfilePic = findViewById(R.id.chooseProfilePic);

        progress = new ProgressDialog(Create_Profile.this);

        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child(USER_PICTURES);

        editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
    }


    private void checkPermission(String CameraService, int requestCode) {
        if (ContextCompat.checkSelfPermission(Create_Profile.this, CameraService)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(Create_Profile.this,
                    new String[]{CameraService},
                    requestCode);
        } else {
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
            } else {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putString(PROFILE_STATUS, "NOT_CREATED");
        editor.apply();
    }
}
