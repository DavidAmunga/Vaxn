package com.buttercell.vaxn.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.guardian.GuardianPatients;
import com.buttercell.vaxn.guardian.PatientDetails;
import com.buttercell.vaxn.model.Patient;
import com.buttercell.vaxn.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectPatient extends AppCompatActivity {
    private static final String TAG = "SelectPatient";

    @BindView(R.id.patientList)
    RecyclerView patientList;

    User user;
    String guardian_key;
    FirebaseRecyclerAdapter<Patient, GuardianPatients.PatientViewHolder> adapter;
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
        setContentView(R.layout.activity_select_patient);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Patient");

        patientList.setHasFixedSize(true);
        patientList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (getIntent().getExtras() != null) {
            user = (User) getIntent().getSerializableExtra("user");
            guardian_key = getIntent().getStringExtra("guardian_key");


        }


        Query query = FirebaseDatabase.getInstance().getReference("Users").
                child(guardian_key).
                child("userPatients");


        FirebaseRecyclerOptions<Patient> options =
                new FirebaseRecyclerOptions.Builder<Patient>()
                        .setQuery(query, Patient.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Patient, GuardianPatients.PatientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GuardianPatients.PatientViewHolder holder, final int position, @NonNull final Patient model) {

                holder.setPatientName(model.getFirstName(), model.getLastName());
                holder.setPatientDob(model.getDob());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SelectPatient.this, SelectTest.class)
                                .putExtra("patient_key", adapter.getRef(position).getKey())
                                .putExtra("guardian_key", guardian_key)
                                .putExtra("patient", (Serializable) model));


                    }
                });


            }

            @Override
            public GuardianPatients.PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_layout, parent, false);
                return new GuardianPatients.PatientViewHolder(view);
            }
        };

        patientList.setAdapter(adapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
