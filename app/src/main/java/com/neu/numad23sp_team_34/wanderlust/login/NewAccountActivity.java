package com.neu.numad23sp_team_34.wanderlust.login;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.wanderlust.home.HomeActivity;

public class NewAccountActivity extends AppCompatActivity {

    EditText edtUsername;

    EditText edtEmail;

    EditText edtPassword;

    EditText edtConfirmPassword;

    Button signup;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        edtUsername = findViewById(R.id.Username);
        edtEmail = findViewById(R.id.editTextTextEmailAddress);
        edtPassword = findViewById(R.id.editTextTextPassword);
        edtConfirmPassword = findViewById(R.id.confirmPassword);
        signup = findViewById(R.id.Signup);

        signup.setOnClickListener(view -> {

            String username = edtUsername.getText().toString();
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if(username.isEmpty()) {
                edtUsername.setError("This field cannot be empty");
                edtUsername.requestFocus();
                Toast.makeText(NewAccountActivity.this,"Please enter a user " +
                        "name", Toast.LENGTH_SHORT).show();
            }

            else if(TextUtils.isEmpty(email)) {
                Toast.makeText(NewAccountActivity.this,"Please enter email-id" +
                        "!", Toast.LENGTH_SHORT).show();
                edtEmail.setError("This field cannot be empty");
                edtEmail.requestFocus();
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                Toast.makeText(NewAccountActivity.this,"Please " +
                        "enter email-id in correct format!", Toast.LENGTH_SHORT).show();
                edtEmail.setError("Not correct format");
                edtEmail.requestFocus();
            }

            else if(password.isEmpty()) {
                Toast.makeText(NewAccountActivity.this, "Please " +
                        "enter the password!", Toast.LENGTH_SHORT).show();
                edtPassword.setError("This field cannot be empty");
                edtPassword.requestFocus();
            }

            else if(password.length() < 6) {
                Toast.makeText(NewAccountActivity.this,"Password must " +
                        "have at least 6 characters", Toast.LENGTH_SHORT).show();
                edtPassword.setError("More than 6 characters");
                edtPassword.requestFocus();
            }

            else if(confirmPassword.isEmpty()) {
                edtConfirmPassword.setError("Please confirm the password");
                edtConfirmPassword.requestFocus();
            }

            else if(!password.equals(confirmPassword)) {
                edtConfirmPassword.setError("Passwords don't match");
                edtConfirmPassword.requestFocus();
            }

            else {
                auth = FirebaseAuth.getInstance();
                if (!(username.isEmpty() && email.isEmpty()
                        && password.isEmpty()
                        && confirmPassword.isEmpty())) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            if (user != null) {
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Registration Successfully completed", Toast.LENGTH_SHORT).show();
                                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                                    //email verification.
                                                    firebaseUser.sendEmailVerification();

                                                    //Opening an activity after registration.
                                                    Intent intent = new Intent(NewAccountActivity.this, HomeActivity.class);

                                                    //Once finish registering, clear out the activities behind.
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                            | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                    startActivity(intent);
                                                    finish();
                                                    //close register activity.
                                                }
                                            }
                                        });
                            }
                        }
                        else {
                            try{
                                throw task.getException();
                            }
                            catch(FirebaseAuthWeakPasswordException e) {
                                edtPassword.setError("Your password is too weak.");
                                edtPassword.requestFocus();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e) {
                                edtPassword.setError("Your email is invalid or already used. Kindly re-enter.");
                                edtPassword.requestFocus();
                            }
                            catch (FirebaseAuthUserCollisionException e) {
                                edtPassword.setError("User is already registered with this email. Please use another email.");
                                edtPassword.requestFocus();
                            }
                            catch (Exception e) {
                                Log.d(TAG,e.getMessage());
                                Toast.makeText(NewAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
                }
            }

        });
    }
}