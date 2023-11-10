package com.example.donutchat.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public class RepositoryChatimpl extends ViewModel implements RepositoryChat {

    static FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
    private static RepositoryChatimpl repositoryChatImpl = new RepositoryChatimpl();

    public MutableLiveData<Boolean> isLoggedInFromRepo = new MutableLiveData<Boolean>();
    ArrayList<String> myArrayOfMessages = new ArrayList<>();
    ArrayList<String> myArrayOfGroups = new ArrayList<>();

    public MutableLiveData<ArrayList<String>> Messages = new MutableLiveData<>(myArrayOfMessages);
    public MutableLiveData<ArrayList<String>> Groups = new MutableLiveData<>(myArrayOfGroups);





    /*public LiveData<Boolean> getIssignedup() {
        return issignedup;
    }

     */

    private RepositoryChatimpl() {

    }

    public static RepositoryChatimpl getRepositoryChatimpl() {


        return repositoryChatImpl;
    }

    @Override
    public void Login(String email, String password) {



        this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            isLoggedInFromRepo.setValue(true);
                            messageFromRepo.setValue("logged in successfully");

                            // Query user = FirebaseDatabase.getInstance().getReference().child("Users");
                            users.orderByValue().equalTo(email).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {

                                        DataSnapshot data = snapshot.getChildren().iterator().next();
                                        currentUsername.setValue(data.getKey());
                                        ;
                                        Log.i("messy", snapshot.getKey());
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

    @Override
    public boolean SignUphelper(String email, String password, String username) {

        final boolean[] flag = new boolean[2];

        Task task = mAuth.createUserWithEmailAndPassword(email, password);
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                // If sign in fails, display a message to the user.
                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                if (task.isSuccessful()) {
                    Log.i("mess", "sign up successful entered");
                    flag[0] = true;
                    Log.i("mess", "flag is" + flag[0]);
                    Log.i("mess", "this flag is" + flag);

                    Log.i("messi", "got into addUser statment");
                    FirebaseDatabase.getInstance().getReference().child("Users").child(username).setValue(email);
                    Log.i("messi", "left addUser statment");

                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {

                    Log.i("mess", "sign up unsuccesful entered");

                }
            }
        });

        Log.i("mess", "flag is" + flag[0]);
        Log.i("mess", "this flag is" + flag);

        return flag[0];
    }


    @Override
    public void addUser(String username, String email) {

        // add a user to the user table and to the users array
        Log.i("messi", "got into addUser");
        FirebaseDatabase.getInstance().getReference().child("Users").child(username).setValue(email);
        myapp.child("users").child(String.valueOf(userCount)).setValue(username);


    }

    @Override //new Sign up
    public void SignUp(String username, String email, String password) {

        // checks is a user already exists
        Log.i("mess", "checkUser entered");
        final boolean[] useralreadythere = {false};


        //Query user = FirebaseDatabase.getInstance().getReference().child("Users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if (snapshot.hasChildren()) {
                    Log.i("messi", "got into onDataChange");
                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

                    while (iterator.hasNext()) {
                        Log.i("messi", "got into while loop");

                        if (iterator.next().getKey().equals(username)) {

                            Log.i("messi", "got into if inside the while loop");

                            useralreadythere[0] = true;
                            Log.i("messi", "is alreadythere val" + useralreadythere[0]);
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
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (task.isSuccessful()) {

                                messageFromRepo.setValue("sign up was successful!");
                                Log.i("messi", messageFromRepo.getValue());

                                Log.i("messi", "got into addUser statment");

                                users.child(username).setValue(email);
                                myapp.child("users").child(username).setValue("");
                                //usercount++;
                                Log.i("messi", "left addUser statment");
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                //FirebaseUser user = mAuth.getCurrentUser();
                                isSignedUpFromRepo.setValue(true);
                            } else {

                                Log.i("mess", "sign up unsuccesful entered");
                                messageFromRepo.setValue("sign up failed");
                                isSignedUpFromRepo.setValue(false);
                                Log.i("messi", "message.getValue()" + messageFromRepo.getValue());
                            }
                        }
                    });


                }


            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.i("messi", "database error");

            }
        });


        Log.i("messi", "entered fake while loop");

        // while(!isdataloaded[0] ){}
        Log.i("messi", "left fake while loop");


        Log.i("messi", "checkUser left ");

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
        Log.i("messy", "users was initialized");
        myapp = FirebaseDatabase.getInstance().getReference().child("MyApp");
        groups = FirebaseDatabase.getInstance().getReference().child("Groups");

        myapp.child("users").addValueEventListener( loginListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.i("messy2", "loginListener is created");

                userCount = snapshot.getChildrenCount();
                Log.i("messy", "usercount is :" + String.valueOf(userCount));
                Log.i("messy", "snapshot has children? :" + snapshot.getChildrenCount());


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    @Override
    public void initializeFirebaseGroupList(){

        //Log.i("messy7", "activityname is: "+activityname);

        //(myapp.child("groups")).
        myapp.orderByKey().equalTo("groups").addValueEventListener(groupListListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Log.i("message","Current thread: "+ Thread.currentThread());
                Log.i("messy7", "grouplistListener is created");
                Log.i("messy7", "insede the groupListlistener");

                Log.i("messy7", "isGroupDataLoaded" + String.valueOf(isGroupDataLoaded));



                if( snapshot.hasChildren()){

                    DataSnapshot data = snapshot.getChildren().iterator().next();
                    groupCount = data.getChildrenCount();

                    if (isGroupDataLoaded) {
                        Log.i("messy7", "groupcount is :" + String.valueOf(groupCount));
                        //myarrayofgroups.clear();

                        myArrayOfGroups.add(data.child(String.valueOf(groupCount)).getValue(String.class));
                        Groups.setValue(myArrayOfGroups);
                        Log.i("messy7", "myarrayofgroups.get(0)" + myArrayOfGroups.get(0));
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    @Override
    public void initializeFirebaseGroup(String groupname){


        Query group = groups.child(groupname).orderByKey().equalTo("messages");

        group.addValueEventListener(groupListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.i("messy2", "groupListener is created");

                if (isMessagesDataLoaded) {
                    DataSnapshot data = snapshot.getChildren().iterator().next();

                    Log.i("messy", "data.getChildrenCount() " + data.getChildrenCount());
                    Log.i("messy", "data.000 " + data.child(String.valueOf(data.getChildrenCount())).getValue());

                    //myarrayofmessages.add(String.valueOf(data.child(String.valueOf(data.getChildrenCount())).getValue()));
                    myArrayOfMessages.clear();
                    myArrayOfMessages.add((String) data.child(String.valueOf(data.getChildrenCount())).getValue());
                    Log.i("messy", "myarrayofmessages.size() " + myArrayOfMessages.size());
                    Log.i("messy", "myarrayofmessages.get(0) " + myArrayOfMessages.get(0));

                    Messages.setValue(myArrayOfMessages);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });}



    @Override
    public void initializeFirebase(Context context, String groupname, String activityname) {
        Log.i("messy", "current groupename" + groupname);

        FirebaseApp.initializeApp(context);
        //if (users == null) {

        users = FirebaseDatabase.getInstance().getReference().child("Users");
        Log.i("messy", "users was initialized");
        myapp = FirebaseDatabase.getInstance().getReference().child("MyApp");
        groups = FirebaseDatabase.getInstance().getReference().child("Groups");

        if (activityname.equals("login")) {
            myapp.child("users").addValueEventListener( loginListener =new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Log.i("messy2", "loginListener is created");

                    userCount = snapshot.getChildrenCount();
                    Log.i("messy", "usercount is :" + String.valueOf(userCount));
                    Log.i("messy", "snapshot has children? :" + snapshot.getChildrenCount());


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        //myapp.orderByKey().equalTo("groups")
        if (activityname.equals("grouplist")) {
            Log.i("messy7", "activityname is: "+activityname);

            //(myapp.child("groups")).
            myapp.orderByKey().equalTo("groups").addValueEventListener(groupListListener =new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    Log.i("messy7", "grouplistListener is created");
                    Log.i("messy7", "insede the groupListlistener");

                    Log.i("messy7", "isGroupDataLoaded" + String.valueOf(isGroupDataLoaded));

                    DataSnapshot data = snapshot.getChildren().iterator().next();
                    groupCount = data.getChildrenCount();
                    if (isGroupDataLoaded) {
                        Log.i("messy7", "groupcount is :" + String.valueOf(groupCount));
                        //myarrayofgroups.clear();

                        myArrayOfGroups.add(data.child(String.valueOf(groupCount)).getValue(String.class));
                        Groups.setValue(myArrayOfGroups);
                        Log.i("messy7", "myarrayofgroups.get(0)" + myArrayOfGroups.get(0));
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

        Log.i("messy7", "current groupename" + groupname);
        if (activityname.equals("group")) {


            Query group = groups.child(groupname).orderByKey().equalTo("messages");

            group.addValueEventListener(groupListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Log.i("messy2", "groupListener is created");

                    if (isMessagesDataLoaded) {
                        DataSnapshot data = snapshot.getChildren().iterator().next();

                        Log.i("messy", "data.getChildrenCount() " + data.getChildrenCount());
                        Log.i("messy", "data.000 " + data.child(String.valueOf(data.getChildrenCount())).getValue());

                        //myarrayofmessages.add(String.valueOf(data.child(String.valueOf(data.getChildrenCount())).getValue()));
                        myArrayOfMessages.clear();
                        myArrayOfMessages.add((String) data.child(String.valueOf(data.getChildrenCount())).getValue());
                        Log.i("messy", "myarrayofmessages.size() " + myArrayOfMessages.size());
                        Log.i("messy", "myarrayofmessages.get(0) " + myArrayOfMessages.get(0));

                        Messages.setValue(myArrayOfMessages);

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }

    });}}

    @Override
    public void addGroup(String groupname) {
        // add a group to the group table and to the groups array
        Log.i("messy2", "ADD GROUP CLICKED ****************************");

        //Query groupp = myapp.child("groups");
        myapp.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("messy2", "groupListListener is created");

                boolean b = false;
                if (snapshot.hasChildren()) {

                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

                    //int groupcount = Integer.parseInt(data.getChildrenCount()+"");
                    int i;
                    while (iterator.hasNext()) {
                        //Log.i("messy", "snapshot child number" + i + snapshot.child(String.valueOf(i)).getValue().toString());

                        if (iterator.next().getValue().equals(groupname)) {
                            b = true;
                            break;
                        }


                    }
                }
                if (!b) {

                    myapp.child("groups").child(String.valueOf(groupCount + 1)).setValue(groupname);

                    groups.child(groupname).child("number of messages").setValue(0);
                    groups.child(groupname).child("number of joiners").setValue(0);
                    groups.child(groupname).child("joiners").setValue("");
                    groups.child(groupname).child("messages").setValue("");


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
    public void addJoiner(String groupname) {


        Query group = groups.orderByKey().equalTo(groupname);

        group.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {

                DataSnapshot data = snapshot.getChildren().iterator().next();

                Integer joinercount = Integer.parseInt(data.child("number of joiners").getValue().toString());

                groups.child(groupname).child("number of joiners").setValue(++joinercount);


                groups.child(groupname).child("joiners").child(joinercount + "").setValue(currentUsername);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    @Override
    public void addMessage(String message, String groupname) {
        Query group = groups.orderByKey().equalTo(groupname);

        group.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("messy2", "groupListListener is created");

                DataSnapshot data = snapshot.getChildren().iterator().next();

                Integer messagecount;

                messagecount = Integer.parseInt(data.child("number of messages").getValue().toString());
                groups.child(groupname).child("number of messages").setValue(++messagecount);


                groups.child(groupname).child("messages").child(messagecount + "").setValue(currentUsername.getValue() + " : " + message);
                //myarrayofmessages.add( +message);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }



    @Override
    public MutableLiveData<ArrayList<String>> getGroups() {
        return Groups;
    }

    @Override
    public MutableLiveData<ArrayList<String>> getMessages() {
        return Messages;
    }

    @Override
    public void loadGroups() {
        myArrayOfGroups.clear();
        //Query group = myapp.child("groups");

        myapp.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //DataSnapshot data = snapshot.getChildren().iterator().next();
                //int groupcount = Integer.parseInt(data.getChildrenCount() + "");
                Log.i("messy2", "groupListListener is created");

                for (int i = 1; i < groupCount + 1; i++) {
                    Log.i("messy", "snapshot child number" + i + snapshot.child(String.valueOf(i)).getValue().toString());

                    myArrayOfGroups.add(snapshot.child(String.valueOf(i)).getValue().toString());
                }
                Groups.setValue(myArrayOfGroups);
                Log.i("messy", "Groups size is " + Groups.getValue().size());
                Log.i("messy", "groupcount is " + groupCount);

                isGroupDataLoaded = true;
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    @Override
    public void loadMessages(String groupname) {
        myArrayOfMessages.clear();

        Query group = groups.orderByKey().equalTo(groupname);

        group.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("messy2", "groupListListener is created");

                DataSnapshot data = snapshot.getChildren().iterator().next();
                Log.i("messy", "data.getChildrenCount()" + data.getChildrenCount() + "");

                int i;
                messageCount = Integer.parseInt(String.valueOf(data.child("number of messages").getValue()));
                if (messageCount != 0) {
                    for (i = 1; i < messageCount + 1; i++) {
                        myArrayOfMessages.add(String.valueOf(data.child("messages").child(String.valueOf(i)).getValue()));
                        Log.i("messy", Messages.getValue().get(i - 1));


                        Log.i("messii", Messages.getValue().size() + "");
                    }

                    Messages.setValue(myArrayOfMessages);

                }

                isMessagesDataLoaded = true;
                Log.i("messi", "usercount " + userCount);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }




}
