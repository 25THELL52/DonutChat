package com.example.donutchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.donutchat.databinding.ActivityGroupBinding;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.donutchat.R;
import com.example.donutchat.viewmodel.CommunViewModelimpl;
import com.example.donutchat.viewmodel.GroupViewModel;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity implements MyView {

    GroupViewModel groupViewModel;
    String currentgroup;
    String currentuser;
    ArrayList<String> Messages;
    ActivityGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        groupViewModel.setCurrentgroup(new MutableLiveData<>(getIntent().getStringExtra("groupname")));


        groupViewModel.initializeFirebase();
        getLifecycle().addObserver(groupViewModel);
        binding.setCVMI(groupViewModel);
        groupViewModel.loadMessages();

        currentgroup = groupViewModel.getCurrentgroup().getValue();
        currentuser = groupViewModel.getCurrentusername().getValue();
        Messages = new ArrayList<>();

        binding.groupnametv.setText(currentgroup);
        binding.usernametv.setText(currentuser);

        groupViewModel.addJoiner();

        MyGroupAdapter myGroupAdapter = new MyGroupAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        binding.messagesrv.setLayoutManager(linearLayoutManager);
        binding.messagesrv.setAdapter(myGroupAdapter);

        binding.sendbtn.setOnClickListener(view -> {

            String message = binding.newmessageedittext.getText().toString();
            binding.newmessageedittext.setText("");
            groupViewModel.onSendClicked(message);
        });

        groupViewModel.getMessages().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> arraylistofmess) {

                Log.i("messy", String.valueOf(arraylistofmess.size()));

                Log.i("messii", "inside messages observer");

                if (arraylistofmess.size() == 1) {
                    Messages.add(arraylistofmess.get(0));
                    Log.i("messy", String.valueOf(Messages.size()));
                    Log.i("messy", "Messages address" + Messages);
                    Log.i("messy", "arraylistofmess address" + arraylistofmess);

                    myGroupAdapter.setMessages(Messages);


                } else {
                    myGroupAdapter.setMessages(arraylistofmess);
                    Messages.addAll(arraylistofmess);

                    ;
                }

                Log.i("messy", "adapter item count " + myGroupAdapter.getItemCount());
                if (myGroupAdapter.getItemCount() != 0) {
                    binding.messagesrv.smoothScrollToPosition((myGroupAdapter.getItemCount()) - 1);
                }

                /*
                if (arraylistofmess.size() == 1) {
                    Messages =myGroupAdapter.getMessages();
                    if(Messages!= null){
                        Messages.add(arraylistofmess.get(0));
                    myGroupAdapter.setMessages(Messages);

                }}
                else {myGroupAdapter.setMessages(arraylistofmess);}

                 */
            }


            ;


        });

        //communViewModelimpl.onSendClicked("has joined");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupViewModel.clearInfo();
        // delete the joiner from the joiners array
    }


    @Override
    public String getCurrentuser() {
        return currentuser;
    }


}