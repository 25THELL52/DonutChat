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
import com.example.donutchat.viewmodel.CommunViewModelimpl;
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

        groupListViewModel.loadGroups();
        //groups =;
        binding.setLifecycleOwner(this);

        binding.createbtn.setOnClickListener(view -> {

            String groupname = binding.newgroupedittext.getText().toString();
            binding.newgroupedittext.setText("");
            groupListViewModel.onCreateClicked(groupname);


        });

        groupListViewModel.getGroups().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> aBoolean) {
                if (aBoolean.size() > 0) {
                    Log.i("messy", "aBoolean.get(0)" + aBoolean.get(aBoolean.size() - 1));
                }
                myGroupListAdapter.setArray(aBoolean);

                myGroupListAdapter.notifyDataSetChanged();
                //notify groupListAdapter
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

    public void onItemclick(String groupename) {
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra("groupname", groupename);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //communViewModelimpl.clearJoiner();
    }
}