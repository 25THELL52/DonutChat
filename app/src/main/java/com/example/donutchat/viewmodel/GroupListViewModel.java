package com.example.donutchat.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.repository.RepositoryChatimpl;

import java.util.ArrayList;

public class GroupListViewModel extends ViewModel implements LifecycleObserver {


    RepositoryChatimpl repositoryChatimpl;
    MutableLiveData<ArrayList<String>> groups;
    public MutableLiveData<String> messageFromViewmmodel = new MutableLiveData<>();
    public MutableLiveData<String> messageFromRepository;


    public GroupListViewModel(){

    repositoryChatimpl = RepositoryChatimpl.getRepositoryChatimpl();
    groups = repositoryChatimpl.Groups;
    messageFromRepository = repositoryChatimpl.messageFromRepo;
    groups = repositoryChatimpl.Groups;





}

    public void initializeFirebase() {

        repositoryChatimpl.initializeFirebaseGroupList();
    }



    public void onCreateClicked(String groupname) {
        repositoryChatimpl.addGroup(groupname);
    }

    public LiveData<ArrayList<String>> getGroups() {
        //groups= repositoryChatimpl.Groups;
        return groups;
    }

    public void loadGroups() {
        repositoryChatimpl.loadGroups();

    }



    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)

    private void handleListeners() {


        if (repositoryChatimpl.groupListListener != null) {

            repositoryChatimpl.myapp.child("groups").removeEventListener(repositoryChatimpl.groupListListener);
            Log.i("messy2", "listener groupListListener removed");
        }




    }
}
