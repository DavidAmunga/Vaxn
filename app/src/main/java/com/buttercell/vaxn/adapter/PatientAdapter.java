package com.buttercell.vaxn.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.guardian.PatientDetails;
import com.buttercell.vaxn.model.Patient;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class PatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "PatientViewHolder";
    TextView txtPatientName, txtPatientDob;

    public PatientViewHolder(View itemView) {
        super(itemView);

        txtPatientName = itemView.findViewById(R.id.txt_patient_name);
        txtPatientDob = itemView.findViewById(R.id.txt_dob);
    }

    public void setPatientName(String firstName, String lastName) {
        TextView txtPatientName = itemView.findViewById(R.id.txt_patient_name);
        txtPatientName.setText(String.valueOf(firstName + " " + lastName));
    }

    public void setPatientDob(String date) {
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


    @Override
    public void onClick(View v) {

    }
}

public class PatientAdapter extends RecyclerView.Adapter<PatientViewHolder> {

    private List<Patient> listData = new ArrayList();
    private Context context;


    public PatientAdapter(List<Patient> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View itemView = inflater.inflate(R.layout.patient_layout, parent, false);

        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PatientViewHolder holder, final int position) {
        holder.setPatientName(listData.get(position).getFirstName(), listData.get(position).getLastName());
        holder.setPatientDob(listData.get(position).getDob());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Patient patient=listData.get(position);
                context.startActivity(new Intent(context, PatientDetails.class)
                        .putExtra("patient", (Serializable) patient));


            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}
