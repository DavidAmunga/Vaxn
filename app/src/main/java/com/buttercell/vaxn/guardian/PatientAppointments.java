package com.buttercell.vaxn.guardian;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.buttercell.vaxn.model.Appointment;
import com.buttercell.vaxn.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paperdb.Paper;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientAppointments extends android.app.Fragment {

    private static final String TAG = "PatientAppointments";
    @BindView(R.id.appointmentList)
    RecyclerView appointmentList;
    Unbinder unbinder;
    String guardianKey="";

    FirebaseRecyclerAdapter<Appointment, AppointmentViewHolder> adapter;

    public PatientAppointments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_appointments, container, false);
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
                Log.d(TAG, "onViewCreated: Guardian Key"+Common.guardian_key);
            }
            else
            {
                guardianKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
        }

        Query query = FirebaseDatabase.getInstance().getReference("Users").
                child(guardianKey).child("userPatients").
                child(Common.patient_key).child("userAppointments");



        FirebaseRecyclerOptions<Appointment> options =
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(query, Appointment.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Appointment, AppointmentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position, @NonNull Appointment model) {
                Log.d(TAG, "onBindViewHolder: Start");
                holder.setAppointmentDate(model.getVisitDate());
                holder.setTestName(model.getTestName());
            }

            @Override
            public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_layout, parent, false);
                return new AppointmentViewHolder(view);
            }
        };
        appointmentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        appointmentList.setHasFixedSize(true);
        appointmentList.setAdapter(adapter);


    }


    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        public AppointmentViewHolder(View itemView) {
            super(itemView);
        }

        public void setAppointmentDate(String date) {
            TextView txtDate = itemView.findViewById(R.id.txt_dob);
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date oldDate = sdf.parse(date);
                Log.d(TAG, "Date " + sdf.format(oldDate));

                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd");
                Date newDate = sdf1.parse(oldDate.toString());

                txtDate.setText(sdf1.format(newDate).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public void setTestName(String name) {
            TextView txtName = itemView.findViewById(R.id.txt_test_name);
            txtName.setText(name);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
