package com.example.donutchat.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.donutchat.model.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public class RepositoryChatImpl extends ViewModel implements RepositoryChat {

     FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public  DatabaseReference users;
    public  DatabaseReference myapp;
    public  DatabaseReference groups;

    public ValueEventListener groupListener;
    public ValueEventListener groupListListener;
    public  ValueEventListener loginListener;

    int messageCount;
    public boolean isMessagesDataLoaded = false;
    public boolean isGroupDataLoaded = false;

    static long userCount = 0;
    static long groupCount = 0;
    public MutableLiveData<String> messageFromRepo = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSignedUpFromRepo = new MutableLiveData<>();
    public MutableLiveData<String> currentUsername = new MutableLiveData<>();

    //** Singleton Pattern
    private static RepositoryChatImpl repositoryChatImpl = new RepositoryChatImpl();

    public MutableLiveData<Boolean> isLoggedInFromRepo = new MutableLiveData<Boolean>();
    ArrayList<String> myArrayOfMessages = new ArrayList<>();
    ArrayList<Group> myArrayOfGroups = new ArrayList<>();

    public MutableLiveData<ArrayList<String>> Messages = new MutableLiveData<>(myArrayOfMessages);
    public MutableLiveData<ArrayList<Group>> Groups = new MutableLiveData<>(myArrayOfGroups);



    private RepositoryChatImpl() {

    }

    public static RepositoryChatImpl getRepositoryChatImpl() {


        return repositoryChatImpl;
    }

    @Override
    public void Login(String email, String password) {



        this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser user = mAuth.getCurrentUser();
                            isLoggedInFromRepo.setValue(true);
                            messageFromRepo.setValue("logged in successfully");


                            users.orderByValue().equalTo(email).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    if (snapshot.hasChildren()) {

                                        DataSnapshot data = snapshot.getChildren().iterator().next();
                                        currentUsername.setValue(data.getKey());
                                        ;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        } else {
                            messageFromRepo.setValue("log in failed");
                            isLoggedInFromRepo.setValue(false);


                        }

                    }
                });


    }



    @Override //new Sign up
    public void SignUp(String username, String email, String password) {

        // checks is a user already exists
        final boolean[] useralreadythere = {false};


        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if (snapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

                    while (iterator.hasNext()) {

                        if (iterator.next().getKey().equals(username)) {


                            useralreadythere[0] = true;
                            messageFromRepo.setValue("already chosen username");

                            break;

                        }
                    }
                }
                if (!useralreadythere[0]) {

                    Task task = mAuth.createUserWithEmailAndPassword(email, password);
                    task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                messageFromRepo.setValue("sign up was successful!");


                                users.child(username).setValue(email);

                                ;
                                userCount++;
                                myapp.child("number of users").setValue(String.valueOf(userCount));
                                myapp.child("users").child(String.valueOf(userCount)).setValue(username);

                                isSignedUpFromRepo.setValue(true);





                            } else {

                                messageFromRepo.setValue("sign up failed");
                                isSignedUpFromRepo.setValue(false);
                            }
                        }
                    });


                }


            }


            @Override
            public void onCancelled(DatabaseError error) {

            }
        });



    }


    @Override
    public void signUpWithGoogle() {

    }

    @Override
    public void logInWithGoogle() {

    }

    @Override
    public void initializeFirebaseLogin(Context context){
        FirebaseApp.initializeApp(context);

        users = FirebaseDatabase.getInstance().getReference().child("Users");
        myapp = FirebaseDatabase.getInstance().getReference().child("MyApp");
        groups = FirebaseDatabase.getInstance().getReference().child("Groups");

        myapp.child("users").addValueEventListener( loginListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                userCount = snapshot.getChildrenCount();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    @Override
    public void initializeFirebaseGroupList(){


        myapp = FirebaseDatabase.getInstance().getReference().child("MyApp");

        myapp.orderByKey().equalTo("groups").addValueEventListener(groupListListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Log.i("myMessage","Current thread: "+ Thread.currentThread());




                if( snapshot.hasChildren()){



                    DataSnapshot data = snapshot.getChildren().iterator().next();
                    groupCount = data.getChildrenCount();

                    if (isGroupDataLoaded) {



                        myArrayOfGroups.add(new Group(groupCount, data.child(String.valueOf(groupCount)).getValue(String.class)));
                        Groups.setValue(myArrayOfGroups);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    @Override
    public void initializeFirebaseGroup(Group group){

        Query query = groups.child(String.valueOf(group.getGroupID())).orderByKey().equalTo("messages");

        query.addValueEventListener(groupListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.i("myMessage", "groupListener is created");


                if(snapshot.hasChildren()) {

                    if (isMessagesDataLoaded) {



                        myArrayOfMessages.clear();
                        Long numberOfMessages = snapshot.getChildren().iterator().next().getChildrenCount();
                        if (numberOfMessages!=0) {
                            myArrayOfMessages.add(String.valueOf(snapshot.getChildren().iterator().next().child(String.valueOf(numberOfMessages)).getValue()));



                            Messages.setValue(myArrayOfMessages);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });}


    @Override
    public void addGroup(String groupname) {
        // add a group to the group table and to the groups array
        Log.i("myMessage", "inside AddGroup method ");

        //Query groupp = myapp.child("groups");
        myapp.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {

                boolean b = false;
                if (snapshot.hasChildren()) {

                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

                    int i;
                    while (iterator.hasNext()) {

                        if (iterator.next().getValue().equals(groupname)) {
                            b = true;
                            break;
                        }


                    }
                }
                if (!b) {

                    myapp.child("groups").child(String.valueOf(groupCount + 1)).setValue(groupname);
                    myapp.child("number of groups").setValue(String.valueOf(groupCount + 1));
                    groups.child(String.valueOf(groupCount + 1)).child("group name").setValue(groupname);
                    groups.child(String.valueOf(groupCount + 1)).child("number of messages").setValue(0);
                    groups.child(String.valueOf(groupCount + 1)).child("number of joiners").setValue(0);
                    groups.child(String.valueOf(groupCount + 1)).child("joiners").setValue("");
                    groups.child(String.valueOf(groupCount + 1)).child("messages").setValue("");


                } else {
                    messageFromRepo.setValue("Already chosen groupname, get creative!");
                }


            }


            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    @Override
    public void addJoiner(Group group) {

        Query query = groups.orderByKey().equalTo(String.valueOf(group.getGroupID()));

        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if(snapshot.hasChildren()){

                    DataSnapshot data = snapshot.getChildren().iterator().next();


                            Integer joinercount = Integer.parseInt(data.child("number of joiners").getValue().toString());
                            Log.i("myMessage", "joinerCount "+ joinercount);

                            boolean b = false;
                            final Iterator<DataSnapshot> joinersIterator = data.child("joiners").getChildren().iterator();

                            while (joinersIterator.hasNext()) {

                                String joiner = joinersIterator.next().getValue().toString();

                                if (joiner.equals(currentUsername.getValue())) {

                                    b = true;
                                    break;
                                }


                            }

                            if(!b) {


                                joinercount++;
                                groups.child(String.valueOf(group.getGroupID())).child("joiners").child(String.valueOf(joinercount)).setValue(currentUsername.getValue());
                                groups.child(String.valueOf(group.getGroupID())).child("number of joiners").setValue(joinercount);

                            }







                    }
                    }








            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    @Override
    public void addMessage(String message, Group group) {

        Query query = groups.orderByKey().equalTo(String.valueOf(group.getGroupID()));

        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChildren())
                {
                    DataSnapshot data = snapshot.getChildren().iterator().next();


                    Integer messagecount;
                    messagecount = Integer.parseInt(data.child("number of messages").getValue().toString());
                    messagecount++;

                    groups.child(String.valueOf(group.getGroupID())).child("messages").child(messagecount + "").setValue(currentUsername.getValue() + " : " + message);

                    groups.child(String.valueOf(group.getGroupID())).child("number of messages").setValue(messagecount);




                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }



    @Override
    public MutableLiveData<ArrayList<Group>> getGroups() {
        return Groups;
    }

    @Override
    public MutableLiveData<ArrayList<String>> getMessages() {
        return Messages;
    }

    @Override
    public void loadGroups() {
        myArrayOfGroups.clear();

        myapp.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {


                    for (int i = 1; i < groupCount + 1; i++) {

                        myArrayOfGroups.add(new Group(i, snapshot.child(String.valueOf(i)).getValue().toString()));
                    }
                    Groups.setValue(myArrayOfGroups);
                    Log.i("myMessage", "groupCount is " + groupCount);


                }
                isGroupDataLoaded = true;
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    @Override
    public void loadMessages(Group group) {
        myArrayOfMessages.clear();



        Query query = groups.orderByKey().equalTo(String.valueOf(group.getGroupID()));

        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChildren())

                {
                    DataSnapshot data = snapshot.getChildren().iterator().next();



                    int i;
                    messageCount = Integer.parseInt(data.child("number of messages").getValue().toString());

                    if (messageCount != 0) {

                        for (i = 1; i < messageCount + 1; i++) {
                            myArrayOfMessages.add(String.valueOf(data.child("messages").child(String.valueOf(i)).getValue()));
                            Log.i("myMessage","added message is " + String.valueOf(data.child("messages").child(String.valueOf(i)).getValue()));



                        }

                        Messages.setValue(myArrayOfMessages);

                    }

                    Log.i("myMessage", "userCount " + userCount);
                }
                isMessagesDataLoaded = true;

            }







            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }




}
