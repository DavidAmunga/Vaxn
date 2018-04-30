package com.buttercell.vaxn.guardian;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.doctor.DoctorTests;
import com.buttercell.vaxn.model.Patient;
import com.buttercell.vaxn.model.Test;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImmunizationSchedule extends AppCompatActivity {

    private static final String TAG = "ImmunizationSchedule";

    FirebaseRecyclerAdapter<Test, DoctorTests.TestViewHolder> adapter;
    DatabaseReference mDatabase;
    @BindView(R.id.test_list)
    RecyclerView testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunization_schedule);
        ButterKnife.bind(this);


        if (getIntent().getExtras() != null) {
            Patient patient = (Patient) getIntent().getExtras().getSerializable("patient");

            getSupportActionBar().setTitle(patient.getFirstName() + "'s Schedule");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Tests");
        testList.setLayoutManager(new LinearLayoutManager(this));
        testList.setHasFixedSize(true);


        Query query = mDatabase;


        FirebaseRecyclerOptions<Test> options =
                new FirebaseRecyclerOptions.Builder<Test>()
                        .setQuery(query, Test.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Test, DoctorTests.TestViewHolder>(
                options
        ) {
            @Override
            public DoctorTests.TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout, parent, false);
                return new DoctorTests.TestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorTests.TestViewHolder holder, int position, @NonNull final Test model) {
                holder.setTestName(model.getTest_name());
                holder.setTestFullName(model.getTest_full_name());

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showInfoDialog(model);
                        return false;
                    }
                });
            }


        };
        testList.setAdapter(adapter);
    }

    private void showInfoDialog(Test model) {
        Log.d(TAG, "showInfoDialog:Starts ");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(model.getTest_name());

        LayoutInflater inflater = this.getLayoutInflater();
        View info_layout = inflater.inflate(R.layout.test_info_layout, null);

        TextView txt_full_name = info_layout.findViewById(R.id.txt_full_name);
        TextView txt_details = info_layout.findViewById(R.id.txt_details);
        TextView txt_start_age = info_layout.findViewById(R.id.txt_start_age);
        TextView txt_doses = info_layout.findViewById(R.id.txt_doses);
        TextView txt_dose_gap = info_layout.findViewById(R.id.txt_dose_gap);


        txt_full_name.setText(model.getTest_full_name());
        txt_details.setText(model.getTest_details());
        txt_start_age.setText(model.getTest_schedule_age()+" months");
        txt_doses.setText(model.getTest_doses());
        txt_dose_gap.setText(model.getTest_dose_gap());

        alertDialog.setView(info_layout);
        alertDialog.setIcon(R.drawable.ic_local_hospital);

        alertDialog.show();

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
