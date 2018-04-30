package com.buttercell.vaxn.model;

import java.io.Serializable;

/**
 * Created by amush on 03-Mar-18.
 */

public class Patient implements Serializable {
    private String firstName,lastName,dob,guardianKey,patientKey;

    public Patient(String firstName, String lastName, String dob,String guardianKey,String patientKey) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.guardianKey=guardianKey;
        this.patientKey=patientKey;
    }

    public Patient() {
    }

    public String getPatientKey() {
        return patientKey;
    }

    public void setPatientKey(String patientKey) {
        this.patientKey = patientKey;
    }

    public String getGuardianKey() {
        return guardianKey;
    }

    public void setGuardianKey(String guardianKey) {
        this.guardianKey = guardianKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
