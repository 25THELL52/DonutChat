package com.example.donutchat.viewmodel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.repository.RepositoryChatimpl;

public class SignUpViewModel extends ViewModel implements LifecycleObserver {


    public MutableLiveData<Boolean> isSignedUp;

    public MutableLiveData<Boolean> getIssignedUp() {
        return isSignedUp;
    }

    public MutableLiveData<String> messageFromViewmmodel = new MutableLiveData<>();
    public MutableLiveData<String> messageFromRepository;

    RepositoryChatimpl repositoryChatimpl;

    public SignUpViewModel(){

        repositoryChatimpl = RepositoryChatimpl.getRepositoryChatimpl();
        messageFromRepository = repositoryChatimpl.messageFromRepo;
        isSignedUp = repositoryChatimpl.isSignedUpFromRepo;


    }

    public void onSignUpClicked(String email, String password, String username) {

        repositoryChatimpl.SignUp(username, email, password);


        //else {error.setValue("already chosen username, get creative !");}
        //send intent to MainActivity or toast an error

    }

    public void onSignUpWithGoogleClicked() {
        repositoryChatimpl.signUpWithGoogle();
    }


    public void clearInfo() {
        isSignedUp.setValue(false);

    }
}
