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

public class GroupViewModel extends ViewModel implements LifecycleObserver {


    RepositoryChatimpl repositoryChatimpl;
    MutableLiveData<String> currentGroup = new MutableLiveData<>(null);
    MutableLiveData<ArrayList<String>> messages;

    MutableLiveData<String> currentUsername;

    public GroupViewModel(){
        repositoryChatimpl = RepositoryChatimpl.getRepositoryChatimpl();
        messages = repositoryChatimpl.Messages;
        currentUsername = repositoryChatimpl.currentUsername;

    }


    public void initializeFirebase() {

        repositoryChatimpl.initializeFirebaseGroup(currentGroup.getValue());
    }

    public void onSendClicked(String message) {
        repositoryChatimpl.addMessage(message, currentGroup.getValue());
        ;

    }

    public void addJoiner() {
        repositoryChatimpl.addJoiner(currentGroup.getValue());
    }


    public void setCurrentusername(MutableLiveData<String> currentusername) {
        this.currentUsername = currentusername;
    }



    public LiveData<ArrayList<String>> getMessages() {
        //messages= repositoryChatimpl.Messages;
        return messages;
    }

    public void loadMessages() {
        Log.i("messy", "currentgroup.getValue" + currentGroup.getValue());
        repositoryChatimpl.loadMessages(currentGroup.getValue());

    }

    public void setMessages(MutableLiveData<ArrayList<String>> messages) {
        this.messages = messages;
    }


    public void setCurrentgroup(MutableLiveData<String> currentgroup) {
        this.currentGroup = currentgroup;
    }



    public LiveData<String> getCurrentusername() {
        return currentUsername;
    }

    public LiveData<String> getCurrentgroup() {
        return currentGroup;
    }

    public void clearInfo() {

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


    }

}
