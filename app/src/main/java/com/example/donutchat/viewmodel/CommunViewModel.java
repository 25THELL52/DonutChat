package com.example.donutchat.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public interface CommunViewModel   {


    public void onLoginClicked(String email, String Password);
    public void onSignUpClicked(String email, String Password, String username);
    public void onCreateClicked(String groupname);
    public void onSendClicked(String message);
    public void onSignUpHereClicked();
    //public void onRecyclerviewitemClicked();
    public void onSignUpWithGoogleClicked();
    public void onLogInWithGoogleClicked();


    public void initializeFirebase(Context context, String activityname);
    //public void onItemClick(String groupname);

    //void getUsernameforemail(String email);

    public void addJoiner();
}
