package com.neu.numad23sp_team_34.wanderlust.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.neu.numad23sp_team_34.MainActivity;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.wanderlust.WanderLust_MainActivity;
import com.neu.numad23sp_team_34.wanderlust.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText email;

    private EditText password;

    private static final String TAG = "LoginActivity";

    FirebaseAuth firebaseAuth;

//    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.login);
        Button newAccount = (Button) findViewById(R.id.createAccount);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);

            //firebaseDatabase.getReference().child("WanderLustUser").child(String.valueOf(0)).child(email_id).setValue(pwd);


        //password hide and show
        ImageView pwd_show_hide = (ImageView) findViewById(R.id.pwd_hide);
        pwd_show_hide.setImageResource(R.drawable.ic_pwdhide);
        pwd_show_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {

                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    pwd_show_hide.setImageResource(R.drawable.ic_pwdhide);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwd_show_hide.setImageResource(R.drawable.ic_pwdshow);
                }

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

            login.setOnClickListener(view -> {
                String email_id = email.getText().toString();
                String pwd = password.getText().toString();
                if(TextUtils.isEmpty(email_id)) {
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
                    //Login credentials are checked.
                    if (!(email_id.isEmpty() && pwd.isEmpty())) {
                        firebaseAuth.signInWithEmailAndPassword(email_id, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    //Email verification before login.
                                    if (firebaseUser.isEmailVerified()) {
                                        Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else {

                                        showEmailVerificationDialog(firebaseUser);
                                        firebaseAuth.signOut();
                                    }

//                                    Intent intent = new Intent(LoginActivity.this, StickItToEm.class);
//                                    startActivity(intent);
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        email.setError("User does not exist. Please create a new account before login!");
                                        email.requestFocus();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        email.setError("Invalid credentials. Kindly re-enter!");
                                        email.requestFocus();
                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
//                                {
//                                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                }

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
    private void showEmailVerificationDialog(FirebaseUser firebaseUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Login Verification");
        builder.setMessage("Kindly verify your email before logging in");

        builder.setPositiveButton("OK", (dialogInterface, i) -> {});
        builder.setNegativeButton("Re-send Verification Email", (dialogInterface, i) -> {
            firebaseUser.sendEmailVerification();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(LoginActivity.this, WanderLust_MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}