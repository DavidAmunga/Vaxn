package com.buttercell.vaxn.doctor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Test;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amush on 22-Jan-18.
 */

public class DoctorTests extends Fragment {
    private static final String TAG = "DoctorTests";


    RecyclerView mList;
    FirebaseRecyclerAdapter<Test,TestViewHolder> adapter;
    DatabaseReference mDatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_tests, container, false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tests");

        FloatingActionButton fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddTest.class));
            }
        });

        mDatabase= FirebaseDatabase.getInstance().getReference("Tests");
        mList=view.findViewById(R.id.test_list);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);




        adapter=new FirebaseRecyclerAdapter<Test, TestViewHolder>(
                Test.class,
                R.layout.test_layout,
                TestViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(TestViewHolder viewHolder, final Test model, int position) {
                viewHolder.setTestName(model.getTest_name());
                viewHolder.setTestFullName(model.getTest_full_name());

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showInfoDialog(model);

                        return false;
                    }
                });
            }
        };
        mList.setAdapter(adapter);


    }

    private void showInfoDialog(Test model) {
        Log.d(TAG, "showInfoDialog:Starts ");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(model.getTest_name());

        LayoutInflater inflater = this.getLayoutInflater();
        View info_layout = inflater.inflate(R.layout.test_info_layout, null);

        TextView txt_full_name=info_layout.findViewById(R.id.txt_full_name);
        TextView txt_details=info_layout.findViewById(R.id.txt_details);
        TextView txt_start_age=info_layout.findViewById(R.id.txt_start_age);
        TextView txt_doses=info_layout.findViewById(R.id.txt_doses);
        TextView txt_dose_gap=info_layout.findViewById(R.id.txt_dose_gap);


        txt_full_name.setText(model.getTest_full_name());
        txt_details.setText(model.getTest_details());
        txt_start_age.setText(model.getTest_schedule_age());
        txt_doses.setText(model.getTest_doses());
        txt_dose_gap.setText(model.getTest_dose_gap());

        alertDialog.setView(info_layout);
        alertDialog.setIcon(R.drawable.ic_local_hospital);

        alertDialog.show();

    }

    public static class TestViewHolder extends RecyclerView.ViewHolder
    {

        public TestViewHolder(View itemView) {
            super(itemView);
        }
        public void setTestName(String name)
        {
            TextView txtName=itemView.findViewById(R.id.txt_testname);
            txtName.setText(name);
        }
        public void setTestFullName(String name)
        {
            TextView txtFullName=itemView.findViewById(R.id.txt_full_name);
            txtFullName.setText(name);

        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }
}
