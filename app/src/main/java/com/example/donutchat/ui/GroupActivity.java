package com.example.donutchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.donutchat.databinding.ActivityGroupBinding;

import android.os.Bundle;
import android.util.Log;

import com.example.donutchat.R;
import com.example.donutchat.model.Group;
import com.example.donutchat.viewmodel.GroupViewModel;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity implements MyView {

    GroupViewModel groupViewModel;
    Group currentgroup;
    String currentuser;
    ArrayList<String> Messages;
    ActivityGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        groupViewModel.setCurrentgroup(new MutableLiveData<>(new Group(getIntent().getLongExtra("groupId",0),getIntent().getStringExtra("groupname"))));


        groupViewModel.initializeFirebase();
        getLifecycle().addObserver(groupViewModel);
        binding.setCVMI(groupViewModel);
        groupViewModel.loadMessages();

        currentgroup = groupViewModel.getCurrentgroup().getValue();

        currentuser = groupViewModel.getCurrentusername().getValue();


        Messages = new ArrayList<>();

        binding.groupnametv.setText(currentgroup.getGroupName());
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

        groupViewModel._messages.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> arraylistofmess) {



                Log.i("myMessage", "inside _messages observer");

                if (arraylistofmess.isEmpty()) {

                    return;
                }
                if (arraylistofmess.size() == 1) {
                    Messages.add(arraylistofmess.get(0));



                    myGroupAdapter.setMessages(Messages);
                    myGroupAdapter.notifyDataSetChanged();


                } else {

                    myGroupAdapter.setMessages(arraylistofmess);
                    Messages.addAll(arraylistofmess);
                    myGroupAdapter.notifyDataSetChanged();

                    ;
                }

                if (myGroupAdapter.getItemCount() != 0) {
                    binding.messagesrv.smoothScrollToPosition((myGroupAdapter.getItemCount()) - 1);
                }


            }


            ;


        });


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