package com.neu.numad23sp_team_34.WanderLust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button login;

    private Button newAccount;

    EditText email;

    EditText password;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        newAccount = (Button) findViewById(R.id.createAccount);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);

        String email_id = email.getText().toString();
        String pwd = password.getText().toString();


            firebaseDatabase.getReference().child("WanderLustUser").child(String.valueOf(0)).child(email_id).setValue(pwd);

            login.setOnClickListener(view -> {
                if(TextUtils.isEmpty(email_id)){
                    Toast.makeText(LoginActivity.this,"Please enter email-id!",Toast.LENGTH_SHORT).show();
                    email.setError("This field cannot be empty");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email_id).matches()){
                    Toast.makeText(LoginActivity.this,"Please enter email-id in correct format!",Toast.LENGTH_SHORT).show();
                    email.setError("Not correct format");
                    email.requestFocus();
                }
                if(pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter the password!",Toast.LENGTH_SHORT).show();
                    password.setError("This field cannot be empty");
                    password.requestFocus();
                }

            });


        newAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,NewAccountActivity.class);
            startActivity(intent);
        });


    }

}