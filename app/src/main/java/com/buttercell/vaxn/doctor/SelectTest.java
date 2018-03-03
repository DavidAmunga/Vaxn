package com.buttercell.vaxn.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.Test;
import com.buttercell.vaxn.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectTest extends AppCompatActivity {

    private static final String TAG = "SelectTest";
    RecyclerView mList;
    FirebaseRecyclerAdapter<Test,DoctorTests.TestViewHolder> adapter;
    DatabaseReference mDatabase;
    User user;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AvenirLTStd-Medium_0.otf")
                .setFontAttrId(R.attr.fontPath).build());
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

        Query query = mDatabase;


        FirebaseRecyclerOptions<Test> options =
                new FirebaseRecyclerOptions.Builder<Test>()
                        .setQuery(mDatabase, Test.class)
                        .build();


        adapter=new FirebaseRecyclerAdapter<Test, DoctorTests.TestViewHolder>(
               options
        ) {
            @Override
            public DoctorTests.TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout,parent,false);
                return new DoctorTests.TestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorTests.TestViewHolder holder, int position, @NonNull final Test model) {
                holder.setTestName(model.getTest_name());
                holder.setTestFullName(model.getTest_full_name());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
