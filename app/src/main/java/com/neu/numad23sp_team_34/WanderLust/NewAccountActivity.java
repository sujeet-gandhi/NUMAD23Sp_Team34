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

    EditText edtusername;
    EditText edtemail;
    EditText edtpassword;
    EditText edtconfirmPassword;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);


        edtusername = (EditText) findViewById(R.id.Username);
        edtemail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        edtpassword = (EditText) findViewById(R.id.editTextTextPassword);
        edtconfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        signup = (Button) findViewById(R.id.Signup);





        signup.setOnClickListener(view -> {

            String username = edtusername.getText().toString();
            String email = edtemail.getText().toString();
            String password = edtpassword.getText().toString();
            String confirmPassword = edtconfirmPassword.getText().toString();

            if(username.isEmpty()){
                edtusername.setError("This field cannot be empty");
                edtusername.requestFocus();
                Toast.makeText(NewAccountActivity.this,"Please enter a user " +
                        "name",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(email)){
                Toast.makeText(NewAccountActivity.this,"Please enter email-id" +
                        "!",Toast.LENGTH_SHORT).show();
                edtemail.setError("This field cannot be empty");
                edtemail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString()).matches()){
                Toast.makeText(NewAccountActivity.this,"Please " +
                        "enter email-id in correct format!",Toast.LENGTH_SHORT).show();
                edtemail.setError("Not correct format");
                edtemail.requestFocus();
            }else if(password.isEmpty()) {
                Toast.makeText(NewAccountActivity.this, "Please " +
                        "enter the password!", Toast.LENGTH_SHORT).show();
                edtpassword.setError("This field cannot be empty");
                edtpassword.requestFocus();
            }else if(password.length()<6){
                Toast.makeText(NewAccountActivity.this,"Password must " +
                        "have at least 6 characters",Toast.LENGTH_SHORT).show();
                edtpassword.setError("More than 6 characters");
                edtpassword.requestFocus();
            } else if(confirmPassword.isEmpty()){
                edtconfirmPassword.setError("Please confirm the password");
                edtconfirmPassword.requestFocus();
            }else if(!password.equals(confirmPassword)){
                edtconfirmPassword.setError("Passwords don't match");
                edtconfirmPassword.requestFocus();
            } else {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                if (!(username.isEmpty() && email.isEmpty()
                        && password.isEmpty()
                        && confirmPassword.isEmpty())) {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
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