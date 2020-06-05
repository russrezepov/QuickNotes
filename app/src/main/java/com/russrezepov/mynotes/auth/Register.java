package com.russrezepov.mynotes.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.russrezepov.mynotes.MainActivity;
import com.russrezepov.mynotes.R;

public class Register extends AppCompatActivity {
    EditText regUserName, regEmail, regPassword, regConPassword;
    Button syncAccount;
    TextView loginHere;
    ProgressBar regProgressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Create New Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regUserName = findViewById(R.id.userName);
        regEmail = findViewById(R.id.userEmail);
        regPassword = findViewById(R.id.password);
        regConPassword = findViewById(R.id.passwordConfirm);
        syncAccount = findViewById(R.id.createAccount);
        loginHere = findViewById(R.id.loginHere);
        regProgressBar = findViewById(R.id.regProgressBar);

        fAuth = FirebaseAuth.getInstance();

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        syncAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String syncUserName = regUserName.getText().toString();
                String syncEmail = regEmail.getText().toString();
                String syncPassword = regPassword.getText().toString();
                String syncConPassword = regConPassword.getText().toString();

                if (syncUserName.isEmpty() || syncEmail.isEmpty() || syncPassword.isEmpty() || syncConPassword.isEmpty()) {
                    Toast.makeText(Register.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!syncPassword.equals(syncConPassword)) {
                    regConPassword.setError("Passwords Do Not Match");
                }

                //if Login Successful Display Progress Bar

                regProgressBar.setVisibility(View.VISIBLE);

                AuthCredential credentials = EmailAuthProvider.getCredential(syncEmail,syncPassword);
                fAuth.getCurrentUser().linkWithCredential(credentials).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Register.this, "Account Created Notes Synced", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        //This is the user Successfully Created Account and Synced Notes
                        FirebaseUser fusr = fAuth.getCurrentUser();
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(syncUserName)
                                .build();
                        fusr.updateProfile(request);

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Failed to Connect. Try Again", Toast.LENGTH_SHORT).show();
                        //If the New Account Creation is Failed. Hide Progress Bar
                        regProgressBar.setVisibility(View.INVISIBLE);
                    }
                });

                }

        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}
