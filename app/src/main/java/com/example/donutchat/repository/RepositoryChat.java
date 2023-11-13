package com.example.donutchat.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.donutchat.model.Group;

import java.util.ArrayList;

public interface RepositoryChat {

    public void Login(String email, String Password);

    public void SignUp(String username, String email, String password);

    //public void addUser(String username, String email);

    void initializeFirebaseGroup(Group group);

    public void addGroup(String groupname);



    public void signUpWithGoogle();

    public void logInWithGoogle();

    void addJoiner(Group group);

    void addMessage(String message, Group group);

    public MutableLiveData<ArrayList<Group>> getGroups();

    public MutableLiveData<ArrayList<String>> getMessages();

    public void loadGroups();


    void initializeFirebaseLogin(Context context);

    void initializeFirebaseGroupList();



    void loadMessages(Group group);
}