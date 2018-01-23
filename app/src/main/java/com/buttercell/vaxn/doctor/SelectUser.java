package com.buttercell.vaxn.doctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectUser extends AppCompatActivity {

    RecyclerView mList;
    DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<User,UserViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        getSupportActionBar().setTitle("Select User");


        mDatabase= FirebaseDatabase.getInstance().getReference("Users");
        mList=findViewById(R.id.user_list);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setHasFixedSize(true);


        adapter=new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.user_layout,
                UserViewHolder.class,
                mDatabase.orderByChild("userRole").equalTo("Patient")

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, final User model, int position) {
                viewHolder.setUserAddress(model.getUserAddress());
                viewHolder.setUserMobileNo(model.getUserPhoneNo());
                viewHolder.setUserName(model.getUserName());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SelectUser.this,SelectTest.class);
                        intent.putExtra("user",model);
                        startActivity(intent);
                    }
                });
            }
        };
        mList.setAdapter(adapter);


    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public UserViewHolder(View itemView) {
            super(itemView);
        }
        public void setUserName(String name)
        {
            TextView txtName=itemView.findViewById(R.id.txtName);
            txtName.setText(name);
        }
        public void setUserAddress(String address)
        {
            TextView txtAddress=itemView.findViewById(R.id.txtAddress);
            txtAddress.setText(address);
        }
        public void setUserMobileNo(String mobileNo)
        {
            TextView txtMobileNo=itemView.findViewById(R.id.txtMobileNo);
            txtMobileNo.setText(mobileNo);
        }
    }
}
