package com.neu.numad23sp_team_34.wanderlust.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.wanderlust.details.Activities.MainScreen;

public class LoginActivity extends AppCompatActivity {

    private Button login;

    private Button newAccount;

    EditText email;

    EditText password;

//    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        newAccount = (Button) findViewById(R.id.createAccount);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);

            //firebaseDatabase.getReference().child("WanderLustUser").child(String.valueOf(0)).child(email_id).setValue(pwd);

            login.setOnClickListener(view -> {
                String email_id = email.getText().toString();
                String pwd = password.getText().toString();
                if(TextUtils.isEmpty(email_id)){
                    Toast.makeText(LoginActivity.this,"Please enter email-id!",Toast.LENGTH_SHORT).show();
                    email.setError("This field cannot be empty");
                    email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email_id).matches()){
                    Toast.makeText(LoginActivity.this,"Please enter email-id in correct format!",Toast.LENGTH_SHORT).show();
                    email.setError("Not correct format");
                    email.requestFocus();
                }
//                else {
//                    Toast.makeText(LoginActivity.this,"One moment!",Toast.LENGTH_SHORT).show();
//                }
                else if(pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter the password!",Toast.LENGTH_SHORT).show();
                    password.setError("This field cannot be empty");
                    password.requestFocus();
                } else {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    if (!(email_id.isEmpty() && pwd.isEmpty())) {
                        firebaseAuth.signInWithEmailAndPassword(email_id, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this, MainScreen.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            });

        newAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,NewAccountActivity.class);
            startActivity(intent);
        });


    }

}