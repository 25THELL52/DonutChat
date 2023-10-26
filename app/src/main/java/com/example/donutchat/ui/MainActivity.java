package com.example.donutchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.donutchat.R;
import com.example.donutchat.databinding.ActivityMainBinding;
import com.example.donutchat.viewmodel.CommunViewModel;
import com.example.donutchat.viewmodel.CommunViewModelimpl;
import com.example.donutchat.viewmodel.LoginViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
    LoginViewModel loginViewModel;
    ActivityMainBinding binding;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



         binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
         loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.initializeFirebase(this);
        binding.setCVMI(loginViewModel);

        // Default credentials:
        binding.emailedittext.setText("babe@gmail.com");
        binding.passwordedittext.setText("hithere");
        //communViewModelimpl.messagefromrepository.setValue(null);


        getLifecycle().addObserver(loginViewModel);
        binding.signuptv.setPaintFlags(binding.signuptv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        binding.loginpbtn.setOnClickListener(view -> {

            onLoginButtonClicked();}
      );

        binding.signuptv.setOnClickListener(view -> {

            //communViewModelimpl.onSignUpHereClicked();
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        loginViewModel.messageFromRepository.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });

        loginViewModel.isLoggedIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.equals(true)) {
                    loginViewModel.clearInfo();
                    Intent intent = new Intent(MainActivity.this, GroupListActivity.class);
                    startActivity(intent);
                }
            }

            ;


        });



    }

    private void onLoginButtonClicked() {

            String email = binding.emailedittext.getText().toString();
            String password = binding.passwordedittext.getText().toString();

            if (TextUtils.isEmpty(email)) {
                binding.emailedittext.requestFocus();
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();

            }

            //check if the email entered matches a normal email address pattern

            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailedittext.requestFocus();
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(password)) { //check if the password editText field is empty
                Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
                //this line simply states that no other action should happen unless the password editText field has been filled
            } else {
                loginViewModel.onLoginClicked(email, password);
            }
        }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginViewModel.clearInfo();
    }
}