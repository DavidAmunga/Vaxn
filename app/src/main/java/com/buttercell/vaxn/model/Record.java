package com.buttercell.vaxn.model;

/**
 * Created by amush on 23-Jan-18.
 */

public class Record {
    public String userName,testName,testResults,testComments,dateAdded;

    public Record(String userName, String testName, String testResults, String testComments, String dateAdded) {
        this.userName = userName;
        this.testName = testName;
        this.testResults = testResults;
        this.testComments = testComments;
        this.dateAdded = dateAdded;
    }

    public Record() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestResults() {
        return testResults;
    }

    public void setTestResults(String testResults) {
        this.testResults = testResults;
    }

    public String getTestComments() {
        return testComments;
    }

    public void setTestComments(String testComments) {
        this.testComments = testComments;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
