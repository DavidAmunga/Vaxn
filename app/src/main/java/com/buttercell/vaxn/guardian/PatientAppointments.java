package com.buttercell.vaxn.guardian;


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
import com.buttercell.vaxn.common.Common;
import com.buttercell.vaxn.doctor.DoctorRecords;
import com.buttercell.vaxn.model.Appointment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientAppointments extends android.app.Fragment {

    private static final String TAG = "PatientAppointments";
    @BindView(R.id.appointmentList)
    RecyclerView appointmentList;
    Unbinder unbinder;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Query query = FirebaseDatabase.getInstance().getReference("Users").
                child(userId).child("userPatients").
                child(Common.patient_key).child("userAppointments");


        FirebaseRecyclerOptions<Appointment> options =
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(query, Appointment.class)
                        .build();

        FirebaseRecyclerAdapter<Appointment, AppointmentViewHolder> adapter = new FirebaseRecyclerAdapter<Appointment, AppointmentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position, @NonNull Appointment model) {
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

                txtDate.setText("Date of Birth : " + sdf1.format(newDate).toString());
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
