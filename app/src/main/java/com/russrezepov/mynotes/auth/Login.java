package com.russrezepov.mynotes.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.russrezepov.mynotes.MainActivity;
import com.russrezepov.mynotes.R;
import com.russrezepov.mynotes.Splash;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView loginForgotPassword, loginCreateAccount;
    ProgressBar loginProgressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginCreateAccount = findViewById(R.id.loginCreateAccount);
        loginForgotPassword = findViewById(R.id.loginForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        displayWarning();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = loginEmail.getText().toString();
                String userPassword = loginPassword.getText().toString();

                if (userEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(Login.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Deleting Notes First
                loginProgressBar.setVisibility(View.VISIBLE);

                if (fAuth.getCurrentUser().isAnonymous()) {
                    FirebaseUser user = fAuth.getCurrentUser();

                    fStore.collection("notes").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "All Temp Notes Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //Deleting Anonymous User
                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "Temp User Deleted", Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    });
                }

                fAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Login Failed Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void displayWarning() {
        AlertDialog.Builder warnignAlert = new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Linking Existing user Will Delete All Temp Notes. Please Create New Account To Save Notes")
                .setPositiveButton("New Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Register.class));
                        finish();
                    }
                }).setNegativeButton("Link User", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(), Splash.class));
                                finish();
                            }
                        });
                    }
                });
        warnignAlert.show();
    }
}
