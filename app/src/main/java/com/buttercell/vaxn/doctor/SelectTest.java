package com.buttercell.vaxn.doctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Test;
import com.buttercell.vaxn.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectTest extends AppCompatActivity {

    private static final String TAG = "SelectTest";
    RecyclerView mList;
    FirebaseRecyclerAdapter<Test,DoctorTests.TestViewHolder> adapter;
    DatabaseReference mDatabase;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Test");

        mDatabase= FirebaseDatabase.getInstance().getReference("Tests");
        mList=findViewById(R.id.test_list);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setHasFixedSize(true);


        if (getIntent().getExtras() != null) {
            user=(User) getIntent().getSerializableExtra("user");;
        }
        adapter=new FirebaseRecyclerAdapter<Test, DoctorTests.TestViewHolder>(
                Test.class,
                R.layout.test_layout,
                DoctorTests.TestViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(DoctorTests.TestViewHolder viewHolder, final Test model, int position) {
                viewHolder.setTestName(model.getTest_name());
                viewHolder.setTestFullName(model.getTest_full_name());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SelectTest.this,AddRecord.class);
                        intent.putExtra("user",user);
                        intent.putExtra("test",model);
                        startActivity(intent);
                    }
                });
            }
        };
        mList.setAdapter(adapter);
    }
}
