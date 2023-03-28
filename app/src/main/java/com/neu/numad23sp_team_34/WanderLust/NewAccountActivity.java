package com.neu.numad23sp_team_34.WanderLust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.neu.numad23sp_team_34.R;

public class NewAccountActivity extends AppCompatActivity {

    EditText edtUsername;

    EditText edtEmail;

    EditText edtPassword;

    EditText edtConfirmPassword;

    Button signup;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);


        edtUsername = (EditText) findViewById(R.id.Username);
        edtEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        edtPassword = (EditText) findViewById(R.id.editTextTextPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        signup = (Button) findViewById(R.id.Signup);





        signup.setOnClickListener(view -> {

            String username = edtUsername.getText().toString();
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();
            if(username.isEmpty()){
                edtUsername.setError("This field cannot be empty");
                edtUsername.requestFocus();
                Toast.makeText(NewAccountActivity.this,"Please enter a user " +
                        "name",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(email)){
                Toast.makeText(NewAccountActivity.this,"Please enter email-id" +
                        "!",Toast.LENGTH_SHORT).show();
                edtEmail.setError("This field cannot be empty");
                edtEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
                Toast.makeText(NewAccountActivity.this,"Please " +
                        "enter email-id in correct format!",Toast.LENGTH_SHORT).show();
                edtEmail.setError("Not correct format");
                edtEmail.requestFocus();
            }else if(password.isEmpty()) {
                Toast.makeText(NewAccountActivity.this, "Please " +
                        "enter the password!", Toast.LENGTH_SHORT).show();
                edtPassword.setError("This field cannot be empty");
                edtPassword.requestFocus();
            }else if(password.length()<6){
                Toast.makeText(NewAccountActivity.this,"Password must " +
                        "have at least 6 characters",Toast.LENGTH_SHORT).show();
                edtPassword.setError("More than 6 characters");
                edtPassword.requestFocus();
            } else if(confirmPassword.isEmpty()){
                edtConfirmPassword.setError("Please confirm the password");
                edtConfirmPassword.requestFocus();
            }else if(!password.equals(confirmPassword)){
                edtConfirmPassword.setError("Passwords don't match");
                edtConfirmPassword.requestFocus();
            } else {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (!(username.isEmpty() && email.isEmpty()
                        && password.isEmpty()
                        && confirmPassword.isEmpty())) {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseDatabase.getReference().child("WanderLustUser").setValue(new Users(username,email,password));
                            Toast.makeText(getApplicationContext(), "Registration Successfully completed", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }

        });
    }
}