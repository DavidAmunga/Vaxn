package com.buttercell.vaxn.doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Test;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddTest extends AppCompatActivity {

    Button btnAdd;
    MaterialEditText txt_test_name, txt_full_name, txt_test_details, txt_test_age, txt_doses, txt_dose_gap;
    ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AvenirLTStd-Medium_0.otf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_add_test);


        getSupportActionBar().setTitle("Add Test");

        initViews();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txt_test_name.getText().toString().trim();
                String full_name = txt_full_name.getText().toString().trim();
                String details = txt_test_details.getText().toString().trim();
                String age = txt_test_age.getText().toString().trim();
                String doses = txt_test_age.getText().toString().trim();
                String doseGap = txt_dose_gap.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(full_name) && !TextUtils.isEmpty(details) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(doses) && !TextUtils.isEmpty(doseGap)) {
                    progressDialog.setMessage("Adding Test.....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Test test = new Test(name, full_name, details, age, doses, doseGap);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tests").push();
                    ref.setValue(test).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(AddTest.this, "Test Added!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddTest.this, DoctorHome.class);
                            intent.putExtra("fragment", "tests");
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddTest.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddTest.this, "Please input all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        btnAdd = findViewById(R.id.btnAdd);
        txt_test_name = findViewById(R.id.txt_test_name);
        txt_full_name = findViewById(R.id.txt_full_name);
        txt_test_details = findViewById(R.id.txt_details);
        txt_test_age = findViewById(R.id.txt_test_age);
        txt_doses = findViewById(R.id.txt_doses);
        txt_dose_gap = findViewById(R.id.txt_dose_gap);
        progressDialog = new ProgressDialog(this);
    }
}
