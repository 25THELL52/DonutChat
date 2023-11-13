package com.example.donutchat.viewmodel;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.model.Group;
import com.example.donutchat.repository.RepositoryChatImpl;

import java.util.ArrayList;

public class GroupListViewModel extends ViewModel implements LifecycleObserver {


    RepositoryChatImpl repositoryChatimpl;
    MutableLiveData<ArrayList<Group>> groups;
    public LiveData<ArrayList<Group>> _groups ;
    public MutableLiveData<String> messageFromViewmmodel = new MutableLiveData<>();
    public MutableLiveData<String> messageFromRepository;



    public GroupListViewModel(){

    repositoryChatimpl = RepositoryChatImpl.getRepositoryChatImpl();
    messageFromRepository = repositoryChatimpl.messageFromRepo;
    groups = repositoryChatimpl.Groups;
        _groups = getGroups();





}

    public void initializeFirebase() {

        repositoryChatimpl.initializeFirebaseGroupList();
    }



    public void onCreateClicked(String groupname) {
        repositoryChatimpl.addGroup(groupname);
    }

    public LiveData<ArrayList<Group>> getGroups() {
        //groups= repositoryChatimpl.Groups;
        return groups;
    }

    public void loadGroups() {
        repositoryChatimpl.loadGroups();

    }



    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)

    private void handleListeners() {


        if (repositoryChatimpl.groupListListener != null) {
            repositoryChatimpl.myapp.orderByKey().equalTo("groups").removeEventListener(repositoryChatimpl.groupListListener);
            Log.i("myMessage", "listener groupListListener removed ");
        }
        repositoryChatimpl.isGroupDataLoaded = false;




    }

}
