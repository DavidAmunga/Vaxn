package com.buttercell.vaxn.model;

import java.io.Serializable;

/**
 * Created by amush on 23-Jan-18.
 */

public class Test implements Serializable {
    public String test_name,test_full_name,test_details,test_schedule_age,test_doses,test_dose_gap;

    public Test(String test_name, String test_full_name, String test_details, String test_schedule_age, String test_doses, String test_dose_gap) {
        this.test_name = test_name;
        this.test_full_name = test_full_name;
        this.test_details = test_details;
        this.test_schedule_age = test_schedule_age;
        this.test_doses = test_doses;
        this.test_dose_gap = test_dose_gap;
    }

    public Test() {
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getTest_full_name() {
        return test_full_name;
    }

    public void setTest_full_name(String test_full_name) {
        this.test_full_name = test_full_name;
    }

    public String getTest_details() {
        return test_details;
    }

    public void setTest_details(String test_details) {
        this.test_details = test_details;
    }

    public String getTest_schedule_age() {
        return test_schedule_age;
    }

    public void setTest_schedule_age(String test_schedule_age) {
        this.test_schedule_age = test_schedule_age;
    }

    public String getTest_doses() {
        return test_doses;
    }

    public void setTest_doses(String test_doses) {
        this.test_doses = test_doses;
    }

    public String getTest_dose_gap() {
        return test_dose_gap;
    }

    public void setTest_dose_gap(String test_dose_gap) {
        this.test_dose_gap = test_dose_gap;
    }
}
