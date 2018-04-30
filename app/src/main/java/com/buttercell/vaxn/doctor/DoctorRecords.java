package com.buttercell.vaxn.doctor;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.adapter.PatientAdapter;
import com.buttercell.vaxn.model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorRecords extends Fragment {
    private static final String TAG = "DoctorRecords";

    @BindView(R.id.records_list)
    RecyclerView patientList;

    Unbinder unbinder;
    List<Patient> listPatient = new ArrayList<>();
    @BindView(R.id.fab)
    FloatingActionButton fab;


    public DoctorRecords() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_records, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("All Patients");

        patientList.setHasFixedSize(true);
        patientList.setLayoutManager(new LinearLayoutManager(getContext()));


        final DatabaseReference refGuardian = FirebaseDatabase.getInstance().getReference("Users");
        refGuardian.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.child("userRole").getValue().equals("Guardian")) {
                        String key = postSnapshot.getKey();
                        if (refGuardian.child(key).child("userPatients") != null) {
                            refGuardian.child(key).child("userPatients").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Patient patient = snapshot.getValue(Patient.class);

                                        listPatient.add(patient);

                                        PatientAdapter patientAdapter = new PatientAdapter(listPatient, getContext());

                                        patientList.setAdapter(patientAdapter);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), SelectGuardian.class));
    }


}
