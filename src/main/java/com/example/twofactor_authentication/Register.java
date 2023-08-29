package com.example.twofactor_authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twofactor_authentication.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {


    private boolean passwordShowing = false;
    private boolean conpasswordShowing = false;

    private static final String TAG = "SignUpActivity";
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        final EditText Fname = findViewById(R.id.fullnameET);
        final EditText email = findViewById(R.id.emailET);
        final EditText mobile = findViewById(R.id.mobileET);
        final EditText password = findViewById(R.id.passwordET);
        final EditText conpassword = findViewById(R.id.conpasswordET);
        final ImageView passwordIcon = findViewById(R.id.PasswordIcon);
        final ImageView conpasswordIcon = findViewById(R.id.conPasswordIcon);

        @SuppressLint({"WrongViewCast", "MissingInflatedId", "LocalSuppress"})
        final AppCompatButton signUpbtn = findViewById(R.id.SignUpbtn);
        final TextView signInbtn = findViewById(R.id.signInbtn);


        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {
                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show);
                }
                else {
                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                }

                password.setSelection(password.length());
            }
        });


        conpasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conpasswordShowing) {
                    conpasswordShowing = false;
                    conpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conpasswordIcon.setImageResource(R.drawable.show);
                }
                else {
                    conpasswordShowing = true;
                    conpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conpasswordIcon.setImageResource(R.drawable.hide);
                }

                conpassword.setSelection(conpassword.length());
            }
        });


        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Fname.getText().toString().contentEquals("")) {


                    Toast.makeText(Register.this, "name can't be empty", Toast.LENGTH_SHORT).show();


                } else if (email.getText().toString().contentEquals("")) {


                    Toast.makeText(Register.this, "Password can't be empty", Toast.LENGTH_SHORT).show();

                } else if (mobile.getText().toString().contentEquals("")) {

                    Toast.makeText(Register.this, "Mobile no can't be empty", Toast.LENGTH_SHORT).show();


                }else if (password.getText().toString().contentEquals("")) {

                    Toast.makeText(Register.this, "Password can't be empty", Toast.LENGTH_SHORT).show();


                }
                else if (conpassword==password) {

                    Toast.makeText(Register.this, "Password can't be empty", Toast.LENGTH_SHORT).show();


                } else {


                    mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                try {
                                    if (user != null)
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "Email sent.");

                                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                    Register.this);

                                                            // set title
                                                            alertDialogBuilder.setTitle("Please Verify Your EmailID");

                                                            // set dialog message
                                                            alertDialogBuilder
                                                                    .setMessage("A verification Email Is Sent To Your Registered EmailID, please click on the link and Sign in again!")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                            Intent signInIntent = new Intent(Register.this, Login.class);
                                                                            Register.this.finish();
                                                                        }
                                                                    });

                                                            // create alert dialog
                                                            AlertDialog alertDialog = alertDialogBuilder.create();

                                                            // show it
                                                            alertDialog.show();
                                                        }
                                                    }
                                                });

                                } catch (Exception e) {
                                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                if (task.getException() != null) {
                                    Toast.makeText(Register.this, "This account is already registered ! try another account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });


        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log = new Intent(Register.this,Login.class);
                startActivity(log);
            }
        });

    }
 }
