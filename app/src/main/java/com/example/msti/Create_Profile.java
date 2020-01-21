package com.example.msti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Create_Profile extends AppCompatActivity {

    Spinner branch, year;
    RadioGroup Gender, College;
    Button verifyID;

    List<String> branch_list;
    List<String> year_list;

    String MSIT_branch[], MSI_branch[], MSIT_year[], MSI_year[];

    ArrayAdapter<String> branch_Adapter_spinner;
    ArrayAdapter<String> year_Adapter_spinner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
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

                AlertDialog.Builder alt = new AlertDialog.Builder(Create_Profile.this);
                LayoutInflater inflater = LayoutInflater.from(Create_Profile.this);
               // View view = inflater.inflate();
                AlertDialog alertDialog = alt.create();

                alertDialog.show();
            }
        });


    }

    private void init() {
        Gender = findViewById(R.id.gender);
        College = findViewById(R.id.college);

        verifyID = findViewById(R.id.verifyID);

        branch = findViewById(R.id.Branch_spinner);
        year = findViewById(R.id.student_year);

        branch_list = new ArrayList<>();
        year_list = new ArrayList<>();

        MSI_branch = getResources().getStringArray(R.array.MSI_branch);
        MSIT_branch = getResources().getStringArray(R.array.MSIT_branch);
        MSIT_year = getResources().getStringArray(R.array.four_years);
        MSI_year = getResources().getStringArray(R.array.three_years);

    }

}
