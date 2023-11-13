package com.example.donutchat.viewmodel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.repository.RepositoryChatImpl;

public class SignUpViewModel extends ViewModel implements LifecycleObserver {


    public MutableLiveData<Boolean> isSignedUp;

    public MutableLiveData<Boolean> getIsSignedUp() {
        return isSignedUp;
    }

    public MutableLiveData<String> messageFromViewModel = new MutableLiveData<>();
    public MutableLiveData<String> messageFromRepository;

    RepositoryChatImpl repositoryChatimpl;

    public SignUpViewModel(){

        repositoryChatimpl = RepositoryChatImpl.getRepositoryChatImpl();
        messageFromRepository = repositoryChatimpl.messageFromRepo;
        isSignedUp = repositoryChatimpl.isSignedUpFromRepo;


    }

    public void onSignUpClicked(String email, String password, String username) {

        repositoryChatimpl.SignUp(username, email, password);

    }

    public void onSignUpWithGoogleClicked() {
        repositoryChatimpl.signUpWithGoogle();
    }


    public void clearInfo() {
        isSignedUp.setValue(false);

    }
}
