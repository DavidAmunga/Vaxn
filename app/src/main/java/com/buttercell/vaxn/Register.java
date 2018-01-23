package com.buttercell.vaxn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Register extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "Register";

    Button btnRegister;
    MaterialEditText txtEmail, txtPass, txtConfirmPass, txtUserName, txtPhoneNo, txtAddress;
    TextView txtSignIn,txtDob;
    Spinner spinnerType;
    ArrayAdapter<String> adapter;
    List<String> spinnerList;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

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
        setContentView(R.layout.activity_register);

        initViews();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Paper.init(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Sign up user
                signUp();
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Login.class));
                finish();
            }
        });

        txtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        Register.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle("Pick a Date");
                datePickerDialog.show(getFragmentManager(), "DatePicker");
            }
        });
    }

    private void signUp() {
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPass.getText().toString().trim();
        final String userName = txtUserName.getText().toString().trim();
        String confirmPass = txtConfirmPass.getText().toString().trim();
        final String phoneNo = txtPhoneNo.getText().toString().trim();
        final String dob = txtDob.getText().toString().trim();
        final String address = txtAddress.getText().toString().trim();


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(confirmPass)) {
            if (!confirmPass.equals(password)) {
                Toast.makeText(this, "Please Confirm your Correct Password", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Signing up...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {

                                    String type = spinnerType.getSelectedItem().toString();

                                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(id);
                                    ref.child("userEmail").setValue(email);
                                    ref.child("userName").setValue(userName);
                                    ref.child("userPhoneNo").setValue(phoneNo);
                                    ref.child("userAddress").setValue(address);
                                    ref.child("userDob").setValue(dob);
                                    ref.child("userRole").setValue(type);

                                    Paper.book().write("email", email);
                                    Paper.book().write("pass", password);
                                    Paper.book().write("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName).build();

                                    user.updateProfile(profileUpdates);


                                    progressDialog.dismiss();
                                    Toast.makeText(Register.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Login.class);

                                    startActivity(intent);

                                }


                            }
                        });


            }


        } else {
            Toast.makeText(this, "Please fill out all the Fields!", Toast.LENGTH_SHORT).show();
        }


    }

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);
        txtEmail = findViewById(R.id.txt_email);
        txtUserName = findViewById(R.id.txt_userName);
        txtPass = findViewById(R.id.txt_pass);
        txtConfirmPass = findViewById(R.id.txt_confirm_pass);
        txtPhoneNo = findViewById(R.id.txt_phoneNo);
        txtSignIn = findViewById(R.id.txt_signIn);
        txtDob = findViewById(R.id.txt_dob);
        txtAddress = findViewById(R.id.txt_address);

        spinnerType = findViewById(R.id.spinner);
        spinnerList = new ArrayList<String>
                (Arrays.asList(getResources().getStringArray(R.array.array_occupation)));

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);

        spinnerType.setAdapter(adapter);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        Log.d(TAG, "month " + month);
        String date = dayOfMonth + "-" + month + "-" + year;
        Log.d(TAG, "onDateSet: " + date);
        txtDob.setText(date);
    }
}
