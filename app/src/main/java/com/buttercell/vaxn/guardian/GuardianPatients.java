package com.buttercell.vaxn.guardian;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.doctor.DoctorRecords;
import com.buttercell.vaxn.model.Patient;
import com.buttercell.vaxn.model.Record;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuardianPatients extends Fragment {
    private static final String TAG = "GuardianPatients";

    @BindView(R.id.patientList)
    RecyclerView patientList;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Unbinder unbinder;
    FirebaseRecyclerAdapter<Patient,PatientViewHolder> adapter;

    public GuardianPatients() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guardian_patients, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Patients");

        patientList.setHasFixedSize(true);
        patientList.setLayoutManager(new LinearLayoutManager(getContext()));

        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Users").child(userId).child("userPatients");


        Query query = mDatabase;


        FirebaseRecyclerOptions<Patient> options =
                new FirebaseRecyclerOptions.Builder<Patient>()
                        .setQuery(query, Patient.class)
                        .build();

       adapter=new FirebaseRecyclerAdapter<Patient, PatientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PatientViewHolder holder, int position, @NonNull Patient model) {
                holder.setPatientName(model.getFirstName(),model.getLastName());
                holder.setPatientDob(model.getDob());


            }

            @Override
            public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_layout,parent,false);
                return new PatientViewHolder(view);
            }
        };

       patientList.setAdapter(adapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        startActivity(new Intent(getContext(),AddPatient.class));
    }
    public static class PatientViewHolder extends RecyclerView.ViewHolder
    {

        public PatientViewHolder(View itemView) {
            super(itemView);
        }
        public void setPatientName(String firstName,String lastName)
        {
            TextView txtPatientName=itemView.findViewById(R.id.txt_patient_name);
            txtPatientName.setText(String.valueOf(firstName+" "+lastName));
        }
        public void setPatientDob(String date)
        {
            TextView txtDate = itemView.findViewById(R.id.txt_dob);
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date oldDate = sdf.parse(date);
                Log.d(TAG, "Date " + sdf.format(oldDate));

                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd");
                Date newDate = sdf1.parse(oldDate.toString());

                txtDate.setText("Date of Birth : " + sdf1.format(newDate).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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
