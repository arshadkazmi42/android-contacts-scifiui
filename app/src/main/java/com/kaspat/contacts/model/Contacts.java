package com.kaspat.contacts.model;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by root on 23/12/17.
 */

public class Contacts implements Comparable<Contacts> {
    private String id;
    private String displayName;
    private String phoneNumber;
    private String profilePicture;
    private String email;

    public Contacts(){}

    public Contacts(String id, String displayName, String phoneNumber, String profilePicture, String email) {
        this.id = id;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhoneNumber(String phoneNumber) {
        Log.e("TEST", phoneNumber + " ");
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(@NonNull Contacts contacts) {
        return getDisplayName().compareTo(contacts.getDisplayName());
    }
}
