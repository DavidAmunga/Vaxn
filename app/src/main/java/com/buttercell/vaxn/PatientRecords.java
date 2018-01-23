package com.buttercell.vaxn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.doctor.DoctorRecords;
import com.buttercell.vaxn.model.Record;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

/**
 * Created by amush on 22-Jan-18.
 */

public class PatientRecords extends Fragment {

    RecyclerView mList;

    DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<Record, DoctorRecords.RecordViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_records, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Records");

        mDatabase = FirebaseDatabase.getInstance().getReference("Records");
        mList = view.findViewById(R.id.records_list);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);

        Paper.init(getContext());


        String name= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        adapter = new FirebaseRecyclerAdapter<Record, DoctorRecords.RecordViewHolder>(
                Record.class,
                R.layout.record_layout,
                DoctorRecords.RecordViewHolder.class,
                mDatabase.orderByChild("userName").equalTo(name)
        ) {
            @Override
            protected void populateViewHolder(DoctorRecords.RecordViewHolder viewHolder, Record model, int position) {
                viewHolder.setTestName(model.getTestName());
                viewHolder.setUserName(model.getUserName());
            }
        };

        mList.setAdapter(adapter);

    }

}
