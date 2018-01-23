package com.buttercell.vaxn.doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Record;
import com.buttercell.vaxn.model.Test;
import com.buttercell.vaxn.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddRecord extends AppCompatActivity {


    EditText txt_results, txt_comments;
    Button btnAdd;
    ProgressDialog progressDialog;

    User user;
    Test test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Record");

        initViews();

        if (getIntent().getExtras() != null) {
            user = (User) getIntent().getSerializableExtra("user");
            ;
            test = (Test) getIntent().getSerializableExtra("test");
            ;
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_record(user, test);
            }
        });
    }

    private void add_record(User user, Test test) {
        String test_results = txt_results.getText().toString().trim();
        String test_comments = txt_comments.getText().toString().trim();


        if (!TextUtils.isEmpty(test_comments) && !TextUtils.isEmpty(test_results)) {
            progressDialog.setMessage("Adding Record...");
            progressDialog.setCancelable(false);
            progressDialog.dismiss();
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String dateAdded = df.format(c.getTime());

            Record record = new Record(user.getUserName(), test.getTest_name(), test_results, test_comments, dateAdded);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Records").push();
            ref.setValue(record).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    Toast.makeText(AddRecord.this, "Record added!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddRecord.this, DoctorHome.class).putExtra("fragment", "records"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddRecord.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initViews() {
        txt_comments = findViewById(R.id.txt_test_comments);
        txt_results = findViewById(R.id.txt_test_results);
        btnAdd = findViewById(R.id.btnAdd);

        progressDialog = new ProgressDialog(this);
    }
}
