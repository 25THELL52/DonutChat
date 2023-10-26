package com.example.donutchat.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.donutchat.utility.CallBackinterfaceImpl;

import java.util.ArrayList;

public interface RepositoryChat {

    public void Login(String email, String Password);

    public void SignUp(String username, String email, String password);

    public void addUser(String username, String email);

    public void addGroup(String groupname);

    public void addJoiner(String groupename);

    public void addMessage(String message, String groupname);

    public void signUpWithGoogle();

    public void logInWithGoogle();

    public MutableLiveData<ArrayList<String>> getGroups();

    public MutableLiveData<ArrayList<String>> getMessages();

    public void loadGroups();

    public void loadMessages(String groupename);

    public boolean SignUphelper(String email, String password, String username);

    void initializeFirebaseLogin(Context context);

    void initializeFirebaseGroupList();

    void initializeFirebaseGroup(String groupname);

    public void initializeFirebase(Context context, String groupname, String activitynama);

}