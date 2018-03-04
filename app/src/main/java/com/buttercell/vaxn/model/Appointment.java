package com.buttercell.vaxn.model;

/**
 * Created by amush on 04-Mar-18.
 */

public class Appointment {
    private String visitDate,testName;

    public Appointment() {
    }

    public Appointment(String visitDate, String testName) {
        this.visitDate = visitDate;
        this.testName = testName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
