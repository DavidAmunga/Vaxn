package com.buttercell.vaxn.doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Appointment;
import com.buttercell.vaxn.model.Patient;
import com.buttercell.vaxn.model.Record;
import com.buttercell.vaxn.model.Test;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddRecord extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddRecord";
    TextView txt_test_name, txt_user_name;
    EditText txt_results, txt_comments;
    Button btnAdd;
    ProgressDialog progressDialog;

    Patient patient;
    Test test;


    String guardian_key, patient_key;
    @BindView(R.id.scheduleBox)
    AppCompatCheckBox scheduleBox;

    @BindView(R.id.txt_testName)
    MaterialEditText txtTestName;
    @BindView(R.id.cardAppointment)
    CardView cardAppointment;

    @BindView(R.id.txtAppointmentDate)
    TextView txtAppointmentDate;
    @BindView(R.id.btnDate)
    Button btnDate;

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
        setContentView(R.layout.activity_add_record);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Record");

        initViews();

        if (getIntent().getExtras() != null) {
            patient = (Patient) getIntent().getSerializableExtra("patient");
            test = (Test) getIntent().getSerializableExtra("test");
            guardian_key = getIntent().getStringExtra("guardian_key");
            patient_key = getIntent().getStringExtra("patient_key");

            txt_user_name.setText(String.valueOf(patient.getFirstName() + " " + patient.getLastName()));
            txt_test_name.setText(test.getTest_name());

        }

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked");
            }
        });

        scheduleBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cardAppointment.setVisibility(View.VISIBLE);

                } else {
                    cardAppointment.setVisibility(View.GONE);
                }
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Start");
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        AddRecord.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle("Pick a Date");
                datePickerDialog.show(getFragmentManager(), "DatePicker");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_record(patient, test, guardian_key, patient_key);
            }
        });
    }

    private void add_record(Patient user, Test test, String guardian_key, String patient_key) {
        String test_results = txt_results.getText().toString().trim();
        String test_comments = txt_comments.getText().toString().trim();


        if (!TextUtils.isEmpty(test_comments) && !TextUtils.isEmpty(test_results)) {
            progressDialog.setMessage("Adding Record...");
            progressDialog.setCancelable(false);
            progressDialog.dismiss();
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String dateAdded = df.format(c.getTime());

            Record record = new Record(test.getTest_name(), test_results, test_comments, dateAdded);


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                    .child(guardian_key).child("userPatients").
                            child(patient_key).child("userRecords").push();

            if (scheduleBox.isChecked()) {
                String testName = txtTestName.getText().toString().trim();
                String date = txtAppointmentDate.getText().toString().trim();

                Appointment appointment = new Appointment(date, testName);


                DatabaseReference refAppointment = FirebaseDatabase.getInstance().getReference("Users")
                        .child(guardian_key).child("userPatients").
                                child(patient_key).child("userAppointments").push();

                refAppointment.setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(AddRecord.this, "Appointment added!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddRecord.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

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

        txt_test_name = findViewById(R.id.txt_test_name);
        txt_user_name = findViewById(R.id.txt_userName);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;

        String date = dayOfMonth + "-" + month + "-" + year;
        txtAppointmentDate.setText(date);
    }

    @OnClick(R.id.btnDate)
    public void onViewClicked() {
        Log.d(TAG, "onViewClicked: Start");
    }
}
