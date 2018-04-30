package com.buttercell.vaxn.guardian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPatient extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    @BindView(R.id.txt_first_name)
    MaterialEditText txtFirstName;
    @BindView(R.id.txt_last_name)
    MaterialEditText txtLastName;
    @BindView(R.id.txt_dob)
    MaterialEditText txtDob;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setTitle("Add Patient");

        txtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        AddPatient.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle("Pick a Date");
                datePickerDialog.show(getFragmentManager(), "DatePicker");
            }
        });
    }



    @OnClick(R.id.btnAdd)
    public void onViewClicked() {
        String firstName = txtFirstName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();
        String dob = txtDob.getText().toString().trim();

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(dob)) {
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Adding Patient....");
            progressDialog.show();

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



            String patientKey=FirebaseDatabase.getInstance().getReference("Users").child(userId).child("userPatients").push().getKey();

            Patient patient = new Patient(firstName, lastName, dob,userId,patientKey);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("userPatients").child(patientKey);
            ref.setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPatient.this, "Added!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddPatient.this,GuardianHome.class)
                    .putExtra("fragment","patients"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPatient.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(this, "Please insert all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;

        String date = dayOfMonth + "-" + month + "-" + year;


        txtDob.setText(date);

    }
}
