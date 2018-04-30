package com.buttercell.vaxn.model;

import java.io.Serializable;

/**
 * Created by amush on 23-Jan-18.
 */

public class User implements Serializable {
    public String userRole, userPhotoUrl, userPhoneNo, userName, userEmail, userDob, userAddress;

    public User(String userRole, String userPhotoUrl, String userPhoneNo, String userName, String userEmail, String userDob, String userAddress) {
        this.userRole = userRole;
        this.userPhotoUrl = userPhotoUrl;
        this.userPhoneNo = userPhoneNo;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userDob = userDob;
        this.userAddress = userAddress;
    }

    public User() {
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserDob() {
        return userDob;
    }

    public void setUserDob(String userDob) {
        this.userDob = userDob;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}
