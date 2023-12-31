package com.example.donutchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.donutchat.R;
import com.example.donutchat.databinding.ActivitySignUpBinding;
import com.example.donutchat.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    SignUpViewModel signUpViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        ActivitySignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);



        binding.setCVMI(signUpViewModel);

        binding.signupwGbtn.setOnClickListener(view -> {


            signUpViewModel.onSignUpWithGoogleClicked();

        });
        binding.signupbtn.setOnClickListener(view -> {

            onSignupClicked(binding);
        });

        signUpViewModel.isSignedUp.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.equals(true)) {
                    signUpViewModel.clearInfo();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        signUpViewModel.messageFromViewModel.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();

            }
        });

        signUpViewModel.messageFromRepository.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s!=null) {
                    Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        }

    private  void onSignupClicked(ActivitySignUpBinding binding) {
        String email = binding.emailsignupedittext.getText().toString();
        String password = binding.passwordsignupedittext.getText().toString();
        String username = binding.usernameedittext.getText().toString();


//check if the email editText field is empty

        if (TextUtils.isEmpty(username)) {
            binding.usernameedittext.requestFocus();
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(email)) {
            binding.emailsignupedittext.requestFocus();
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();

        }

        //check if the email entered matches a normal email address pattern

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailsignupedittext.requestFocus();
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(password)) { //check if the password editText field is empty
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            //this line simply states that no other action should happen unless the password editText field has been filled
        }

        else if(password.length()<6){//check if the value in password editText filed is less than 6 characters
            Toast.makeText(this, " Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
             //this line simply states that no other action should happen unless the value in password editText field is more than 6 characters
        }
         else {

            signUpViewModel.onSignUpClicked(email, password, username);

        //}
    }
    }

    }