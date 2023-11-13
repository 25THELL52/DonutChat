package com.example.donutchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.donutchat.R;
import com.example.donutchat.databinding.ActivityGroupListBinding;
import com.example.donutchat.model.Group;
import com.example.donutchat.viewmodel.GroupListViewModel;

import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity {

    LiveData<ArrayList<String>> groups;
    GroupListViewModel groupListViewModel;
    ActivityGroupListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_list);
        groupListViewModel = new ViewModelProvider(this).get(GroupListViewModel.class);

        binding.setCVMI(groupListViewModel);

        MyGroupListAdapter myGroupListAdapter = new MyGroupListAdapter(this);
        binding.grouplistrv.setLayoutManager(new LinearLayoutManager(this));
        binding.grouplistrv.setAdapter(myGroupListAdapter);
        getLifecycle().addObserver(groupListViewModel);
        groupListViewModel.initializeFirebase();

        //load groups ;
        binding.setLifecycleOwner(this);

        binding.createbtn.setOnClickListener(view -> {

            String groupname = binding.newgroupedittext.getText().toString();
            binding.newgroupedittext.setText("");
            groupListViewModel.onCreateClicked(groupname);


        });



        groupListViewModel._groups.observe(this, new Observer<ArrayList<Group>>() {
            @Override
            public void onChanged(ArrayList<Group> aBoolean) {

                myGroupListAdapter.setArray(aBoolean);

                //notify groupListAdapter
                myGroupListAdapter.notifyDataSetChanged();
            }


            ;


        });

        groupListViewModel.messageFromRepository.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(GroupListActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        groupListViewModel.messageFromViewmmodel.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(GroupListActivity.this, s, Toast.LENGTH_LONG).show();
                }

            }
        });




    }







    public void onItemclick(Group group) {
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra("groupname", group.getGroupName());
        intent.putExtra("groupId",group.getGroupID());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupListViewModel.loadGroups();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("myMessage", "GroupListActivity Destroyed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("myMessage", "GroupListActivity Stopped");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("myMessage", "GroupListActivity Paused");

    }


}