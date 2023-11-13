package com.example.donutchat.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.repository.RepositoryChatImpl;

public class LoginViewModel extends ViewModel implements LifecycleObserver {

    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>(Boolean.FALSE);
    public MutableLiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }
    RepositoryChatImpl repositoryChatimpl;
    public MutableLiveData<String> messageFromRepository;

    public LoginViewModel(){

        repositoryChatimpl = RepositoryChatImpl.getRepositoryChatImpl();
        isLoggedIn = repositoryChatimpl.isLoggedInFromRepo;
        messageFromRepository = repositoryChatimpl.messageFromRepo;

    }

    public void initializeFirebase(Context context) {

        repositoryChatimpl.initializeFirebaseLogin(context);
    }

    public void onLoginClicked(String email, String password) {

        repositoryChatimpl.Login(email, password);
    }
    public void onLogInWithGoogleClicked() {
        repositoryChatimpl.logInWithGoogle();
    }



    public void clearInfo() {

        isLoggedIn.setValue(false);


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)

    private void handleListeners() {

        if (repositoryChatimpl.loginListener != null) {

            repositoryChatimpl.myapp.child("users").removeEventListener(repositoryChatimpl.loginListener);
            Log.i("myMessage", "listener loginListener removed");
        }

    }
}
