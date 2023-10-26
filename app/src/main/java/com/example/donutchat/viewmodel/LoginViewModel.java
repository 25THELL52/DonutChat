package com.example.donutchat.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.repository.RepositoryChatimpl;

public class LoginViewModel extends ViewModel implements LifecycleObserver {

    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<Boolean> getIsloggedIn() {
        return isLoggedIn;
    }
    RepositoryChatimpl repositoryChatimpl;
    public MutableLiveData<String> messageFromRepository;

    public LoginViewModel(){

        repositoryChatimpl = RepositoryChatimpl.getRepositoryChatimpl();
        isLoggedIn = repositoryChatimpl.isLoggedInFromRepo;
        messageFromRepository = repositoryChatimpl.messageFromRepo;

    }

    public void initializeFirebase(Context context) {

        repositoryChatimpl.initializeFirebaseLogin(context);
    }

    public void onLoginClicked(String email, String password) {
        // update currentusername
        //send intent to GroupListActivity or toast an error
        //repositoryChatimpl.Login(email, password).getValue();
        repositoryChatimpl.Login(email, password);
    }
    public void onLogInWithGoogleClicked() {
        repositoryChatimpl.logInWithGoogle();
    }

    public void onSignUpHereClicked() {
        //send intent to SignUpActivity or toast an error
    }

    public void clearInfo() {

        isLoggedIn.setValue(false);


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)

    private void handleListeners() {

        if (repositoryChatimpl.loginListener != null) {

            repositoryChatimpl.myapp.child("users").removeEventListener(repositoryChatimpl.loginListener);
            Log.i("messy2", "listener loginListener removed");
        }

    }
}
