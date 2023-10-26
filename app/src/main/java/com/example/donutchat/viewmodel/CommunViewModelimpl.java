package com.example.donutchat.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.repository.RepositoryChat;
import com.example.donutchat.repository.RepositoryChatimpl;
import com.example.donutchat.ui.SignUpActivity;
import com.example.donutchat.utility.CallBackinterfaceImpl;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Observable;

public class CommunViewModelimpl extends ViewModel implements CommunViewModel, LifecycleObserver {


    Context context;
    RepositoryChatimpl repositoryChatimpl;
    MutableLiveData<ArrayList<String>> messages;
    MutableLiveData<ArrayList<String>> groups;

    MutableLiveData<String> currentUsername;
    MutableLiveData<String> currentGroup = new MutableLiveData<>(null);
    public MutableLiveData<Boolean> isSignedUp;
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>(Boolean.FALSE);

    public MutableLiveData<Boolean> getIssignedUp() {
        return isSignedUp;
    }

    public MutableLiveData<Boolean> getIsloggedIn() {
        return isLoggedIn;
    }

    public MutableLiveData<String> messageFromViewmmodel = new MutableLiveData<>();
    public MutableLiveData<String> messageFromRepository;

    public CommunViewModelimpl() {

        repositoryChatimpl = RepositoryChatimpl.getRepositoryChatimpl();
        isSignedUp = repositoryChatimpl.isSignedUpFromRepo;
        isLoggedIn = repositoryChatimpl.isLoggedInFromRepo;
        messageFromRepository = repositoryChatimpl.messageFromRepo;
        messages = repositoryChatimpl.Messages;
        groups = repositoryChatimpl.Groups;
        currentUsername = repositoryChatimpl.currentUsername;
    }


    @Override
    public void onLoginClicked(String email, String password) {
        // update currentusername
        //send intent to GroupListActivity or toast an error
        //repositoryChatimpl.Login(email, password).getValue();
        repositoryChatimpl.Login(email, password);
    }

    @Override
    public void onSignUpClicked(String email, String password, String username) {

        repositoryChatimpl.SignUp(username, email, password);


        //else {error.setValue("already chosen username, get creative !");}
        //send intent to MainActivity or toast an error

    }

    @Override
    public void onCreateClicked(String groupname) {
        repositoryChatimpl.addGroup(groupname);
    }

    @Override
    public void onSendClicked(String message) {
        repositoryChatimpl.addMessage(message, currentGroup.getValue());
        ;

    }

    @Override
    public void onSignUpHereClicked() {
        //send intent to SignUpActivity or toast an error
    }


    @Override
    public void onSignUpWithGoogleClicked() {
        repositoryChatimpl.signUpWithGoogle();
    }

    @Override
    public void onLogInWithGoogleClicked() {
        repositoryChatimpl.logInWithGoogle();
    }

    @Override
    public void initializeFirebase(Context context, String activityname) {

        repositoryChatimpl.initializeFirebase(context, currentGroup.getValue(), activityname);
    }

    @Override
    public void addJoiner() {
        repositoryChatimpl.addJoiner(currentGroup.getValue());
    }


    public LiveData<ArrayList<String>> getMessages() {
        //messages= repositoryChatimpl.Messages;
        return messages;
    }

    public void loadMessages() {
        Log.i("messy", "currentgroup.getValue" + currentGroup.getValue());
        repositoryChatimpl.loadMessages(currentGroup.getValue());

    }


    public LiveData<ArrayList<String>> getGroups() {
        //groups= repositoryChatimpl.Groups;
        return groups;
    }

    public void loadGroups() {
        repositoryChatimpl.loadGroups();

    }


    public LiveData<String> getCurrentusername() {
        return currentUsername;
    }

    public LiveData<String> getCurrentgroup() {
        return currentUsername;
    }

    public void setMessages(MutableLiveData<ArrayList<String>> messages) {
        this.messages = messages;
    }

    public void setGroups(MutableLiveData<ArrayList<String>> groups) {
        this.groups = groups;
    }

    public void setCurrentusername(MutableLiveData<String> currentusername) {
        this.currentUsername = currentusername;
    }

    public void setCurrentgroup(MutableLiveData<String> currentgroup) {
        this.currentGroup = currentgroup;
    }


    public void clearInfo() {
        isSignedUp.setValue(false);
        isLoggedIn.setValue(false);
        messageFromRepository.setValue(null);
        repositoryChatimpl.isGroupDataLoaded = false;
        repositoryChatimpl.isMessagesDataLoaded = false;
        currentGroup.setValue(null);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)

    private void handleListeners() {

        if (repositoryChatimpl.groupListener != null) {
            if (currentGroup.getValue() != null) {
                repositoryChatimpl.groups.child(currentGroup.getValue()).orderByKey().equalTo("messages").removeEventListener(repositoryChatimpl.groupListener);
                Log.i("messy2", "listener groupListener removed");
            }
        }
        if (repositoryChatimpl.groupListListener != null) {

            repositoryChatimpl.myapp.child("groups").removeEventListener(repositoryChatimpl.groupListListener);
            Log.i("messy2", "listener groupListListener removed");
        }


        if (repositoryChatimpl.loginListener != null) {

            repositoryChatimpl.myapp.child("users").removeEventListener(repositoryChatimpl.loginListener);
            Log.i("messy2", "listener loginListener removed");
        }

    }

}
