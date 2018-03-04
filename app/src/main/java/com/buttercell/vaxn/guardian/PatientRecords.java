package com.buttercell.vaxn.guardian;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.common.Common;
import com.buttercell.vaxn.doctor.DoctorRecords;
import com.buttercell.vaxn.model.Record;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientRecords extends android.app.Fragment {
    private static final String TAG = "PatientRecords";
    @BindView(R.id.recordsList)
    RecyclerView recordsList;
    Unbinder unbinder;

    private FirebaseRecyclerAdapter<Record, DoctorRecords.RecordViewHolder> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_records, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Query query = FirebaseDatabase.getInstance().getReference("Users").
                child(userId).child("userPatients").
                child(Common.patient_key).child("userRecords");


        FirebaseRecyclerOptions<Record> options =
                new FirebaseRecyclerOptions.Builder<Record>()
                        .setQuery(query, Record.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Record, DoctorRecords.RecordViewHolder>(options) {
            @Override
            public DoctorRecords.RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout, parent, false);
                return new DoctorRecords.RecordViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorRecords.RecordViewHolder holder, int position, @NonNull Record model) {
                holder.setTestName(model.getTestName());
                holder.setUserName(model.getUserName());
            }

        };
        recordsList.setHasFixedSize(true);
        recordsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordsList.setAdapter(adapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

