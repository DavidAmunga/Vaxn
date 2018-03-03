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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Patient;
import com.buttercell.vaxn.model.Record;
import com.buttercell.vaxn.model.Test;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by amush on 22-Jan-18.
 */

public class DoctorRecords extends Fragment {

    RecyclerView mList;

    DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<Record, RecordViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_records, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);


        getActivity().setTitle("Patient Records");

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectUser.class));
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference("Records");
        mList = view.findViewById(R.id.records_list);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Records");


        FirebaseRecyclerOptions<Record> options =
                new FirebaseRecyclerOptions.Builder<Record>()
                        .setQuery(query, Record.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Record, RecordViewHolder>(options) {
            @Override
            public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout,parent,false);
                return new RecordViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecordViewHolder holder, int position, @NonNull Record model) {
                holder.setTestName(model.getTestName());
                holder.setUserName(model.getUserName());
            }

        };

        mList.setAdapter(adapter);
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {

        public RecordViewHolder(View itemView) {
            super(itemView);
        }

        public void setUserName(String name) {
            TextView txtName = itemView.findViewById(R.id.txt_userName);
            txtName.setText(name);
        }

        public void setTestName(String name) {
            TextView txtTestName = itemView.findViewById(R.id.txt_test_name);
            txtTestName.setText(name);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

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
