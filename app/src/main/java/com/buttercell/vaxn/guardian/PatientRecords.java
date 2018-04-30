package com.buttercell.vaxn.guardian;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.common.Common;
import com.buttercell.vaxn.doctor.DoctorRecords;
import com.buttercell.vaxn.model.Record;
import com.buttercell.vaxn.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientRecords extends android.app.Fragment {
    private static final String TAG = "PatientRecords";
    @BindView(R.id.recordsList)
    RecyclerView recordsList;
    Unbinder unbinder;
    String guardianKey="";

    private FirebaseRecyclerAdapter<Record, RecordViewHolder> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_records, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Paper.init(getContext());

        if(Paper.book().read("currentUser")!=null)
        {
            User user=Paper.book().read("currentUser");

            if(user.getUserRole().equals("Doctor"))
            {
                guardianKey=Common.guardian_key;


            }
            else
            {
                guardianKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
        }

        Query query = FirebaseDatabase.getInstance().getReference("Users").
                child(guardianKey).child("userPatients").
                child(Common.patient_key).child("userRecords");


        FirebaseRecyclerOptions<Record> options =
                new FirebaseRecyclerOptions.Builder<Record>()
                        .setQuery(query, Record.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Record, RecordViewHolder>(options) {
            @Override
            public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout, parent, false);
                return new RecordViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecordViewHolder holder, int position, @NonNull Record model) {
                holder.setTestName(model.getTestName());
                holder.setTestResults(model.getTestResults());
            }

        };
        recordsList.setHasFixedSize(true);
        recordsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordsList.setAdapter(adapter);


    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {

        public RecordViewHolder(View itemView) {
            super(itemView);
        }

        public void setTestResults(String results) {
            TextView txtTestResult = itemView.findViewById(R.id.txt_test_results);
            txtTestResult.setText(results);
        }

        public void setTestName(String name) {
            TextView txtTestName = itemView.findViewById(R.id.txt_test_name);
            txtTestName.setText(name);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

