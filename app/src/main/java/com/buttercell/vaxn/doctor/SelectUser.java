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
import android.widget.TextView;

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

public class SelectUser extends AppCompatActivity {

    RecyclerView mList;
    DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<User, UserViewHolder> adapter;

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
        setContentView(R.layout.activity_select_user);

        getSupportActionBar().setTitle("Select User");


        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mList = findViewById(R.id.user_list);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setHasFixedSize(true);


        Query query = mDatabase;


        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(mDatabase, User.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
         options
        ) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final User model) {
                holder.setUserAddress(model.getUserAddress());
                holder.setUserMobileNo(model.getUserPhoneNo());
                holder.setUserName(model.getUserName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelectUser.this, SelectTest.class);
                        intent.putExtra("user", model);
                        startActivity(intent);
                    }
                });
            }


        };
        mList.setAdapter(adapter);


    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public UserViewHolder(View itemView) {
            super(itemView);
        }

        public void setUserName(String name) {
            TextView txtName = itemView.findViewById(R.id.txtName);
            txtName.setText(name);
        }

        public void setUserAddress(String address) {
            TextView txtAddress = itemView.findViewById(R.id.txtAddress);
            txtAddress.setText(address);
        }

        public void setUserMobileNo(String mobileNo) {
            TextView txtMobileNo = itemView.findViewById(R.id.txtMobileNo);
            txtMobileNo.setText(mobileNo);
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

}
