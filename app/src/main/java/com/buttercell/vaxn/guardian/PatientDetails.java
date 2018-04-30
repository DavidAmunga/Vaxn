package com.buttercell.vaxn.guardian;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.buttercell.vaxn.R;
import com.buttercell.vaxn.common.Common;
import com.buttercell.vaxn.model.Patient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PatientDetails extends AppCompatActivity {


    private static final String TAG = "PatientDetails";

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.txt_patient_name)
    TextView txtPatientName;
    @BindView(R.id.txt_patient_dob)
    TextView txtPatientDob;

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
        setContentView(R.layout.activity_patient_details);
        ButterKnife.bind(this);


        if (getIntent().getExtras() != null) {
            Patient patient = (Patient) getIntent().getExtras().getSerializable("patient");

            setPatientName(patient.getFirstName(), patient.getLastName());
            setPatientDob(patient.getDob());

            String patientKey=patient.getPatientKey();
            String guardianKey=patient.getGuardianKey();


            Log.d(TAG, "onCreate: Patient Key"+patientKey);

            Common.patient_key = patientKey;
            Common.guardian_key=guardianKey;

        }


        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of("Records", PatientRecords.class));
        pages.add(FragmentPagerItem.of("Appointments", PatientAppointments.class));


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getFragmentManager(), pages);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);


    }

    public void setPatientName(String firstName, String lastName) {
        txtPatientName.setText(String.valueOf(firstName + " " + lastName));
    }

    public void setPatientDob(String date) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date oldDate = sdf.parse(date);

            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd");
            Date newDate = sdf1.parse(oldDate.toString());

            txtPatientDob.setText("Date of Birth : " + sdf1.format(newDate).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
