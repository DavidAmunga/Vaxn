package com.buttercell.vaxn.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.buttercell.vaxn.R;

public class AddRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Record");


    }
}
