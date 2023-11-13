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
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class GroupViewModel extends ViewModel implements LifecycleObserver {


    RepositoryChatImpl repositoryChatimpl;
    MutableLiveData<Group> currentGroup = new MutableLiveData<>(null);

    private MutableLiveData<ArrayList<String>> messages;
    public LiveData<ArrayList<String>> _messages ;

    MutableLiveData<String> currentUsername;

    public GroupViewModel(){
        repositoryChatimpl = RepositoryChatImpl.getRepositoryChatImpl();
        messages = repositoryChatimpl.Messages;
        _messages = getMessages();
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
        return messages;
    }

    public void loadMessages() {
        repositoryChatimpl.loadMessages(currentGroup.getValue());

    }

    public void setMessages(MutableLiveData<ArrayList<String>> messages) {
        this.messages = messages;
    }


    public void setCurrentgroup(MutableLiveData<Group> currentgroup) {
        this.currentGroup = currentgroup;
    }



    public LiveData<String> getCurrentusername() {
        return currentUsername;
    }

    public LiveData<Group> getCurrentgroup() {
        return currentGroup;
    }

    public void clearInfo() {

        repositoryChatimpl.isMessagesDataLoaded = false;
        currentGroup.setValue(null);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)

    private void handleListeners() {

        if (repositoryChatimpl.groupListener != null) {
            if (currentGroup.getValue() != null) {


                repositoryChatimpl.groups.child(String.valueOf(currentGroup.getValue().getGroupID())).orderByKey().equalTo("messages").removeEventListener(repositoryChatimpl.groupListener);
                Log.i("myMessage", "listener groupListener removed");
            }
        }
        repositoryChatimpl.isMessagesDataLoaded = false;


    }

}
