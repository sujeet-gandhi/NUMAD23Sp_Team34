//newaccount

package com.neu.numad23sp_team_34.wanderlust.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.sticktoem.models.User;
import com.neu.numad23sp_team_34.wanderlust.UserProfileActivity;

public class NewAccountActivity extends AppCompatActivity {

    EditText edtUsername;

    EditText edtEmail;

    EditText edtPassword;

    EditText edtConfirmPassword;

    Button signup;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    User users;

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
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (!(username.isEmpty() && email.isEmpty()
                        && password.isEmpty()
                        && confirmPassword.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Store the user details in firebase.
                            String userid = task.getResult().getUser().getUid();
                            users =new User(username,email,password);
                            firebaseDatabase.getReference().child("WanderLustUser").child(userid).setValue(users);

                            Toast.makeText(getApplicationContext(), "Registration Successfully completed", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            //email verification.
                            firebaseUser.sendEmailVerification();

                            //Opening an activity after registration.
                            Intent intent = new Intent(NewAccountActivity.this, UserProfileActivity.class);
//
                            //Once finish registering, clear out the activities behind.
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                            finish();
                            //close register activity.

                        } else {

                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }

        });
    }
}