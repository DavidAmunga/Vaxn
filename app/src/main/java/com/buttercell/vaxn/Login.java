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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buttercell.vaxn.doctor.DoctorHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    Button btnLogin;
    TextView txtRegister;

    EditText txtEmail, txtPassword;
    private static final int RC_SIGN_IN = 2;
    ProgressDialog mProgress;

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
        setContentView(R.layout.activity_login);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Paper.init(this);
        initViews();

        mAuth = FirebaseAuth.getInstance();

        checkLogin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Toast.makeText(Login.this, task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "onComplete: Sign Up Success");
                                    normalSignIn();
                                }

                                // ...
                            }
                        });
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });


    }

    private void normalSignIn() {
        mProgress.setMessage("Signing in...");
        mProgress.setCancelable(false);
        mProgress.show();
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        } else {
                            Log.d(TAG, "onComplete: Sign In Success!");



                            Paper.book().write("email", email);
                            Paper.book().write("pass", password);
                            Paper.book().write("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

//                           Verify if doctor or patient
                            verifyIdentity(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        }



                    // ...
                }
    });


}

    private void verifyIdentity(String uid) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userRole=dataSnapshot.child("userRole").getValue().toString();
                String userName=dataSnapshot.child("userName").getValue().toString();
                if(userRole.equals("Patient"))
                {
                    mProgress.dismiss();
                    Toast.makeText(Login.this, "Welcome! "+userName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, PatientHome.class));
                    finish();
                }
                else if(userRole.equals("Doctor"))
                {
                    mProgress.dismiss();
                    Toast.makeText(Login.this, "Welcome! Dr. "+userName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, DoctorHome.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initViews() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_pass);
        txtRegister = (TextView) findViewById(R.id.txt_signUp);
        mProgress = new ProgressDialog(this);
    }

    private void checkLogin() {


        String em = Paper.book().read("email");
        String pwd = Paper.book().read("pass");

        if (em != null && pwd != null) {
            if (!TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd)) {
                mProgress.setMessage("One Moment Please...");
                mProgress.setCancelable(false);
                mProgress.show();
                mAuth.signInWithEmailAndPassword(em, pwd)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(Login.this, task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    mProgress.dismiss();
                                } else {
                                    Log.d(TAG, "onComplete: Sign In Success!");
                                    Paper.book().write("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    verifyIdentity(FirebaseAuth.getInstance().getCurrentUser().getUid());


                                }

                            }
                        });


            }
        }
    }
}
