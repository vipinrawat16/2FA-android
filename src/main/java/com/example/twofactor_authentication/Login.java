package com.example.twofactor_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    public Integer REQUEST_EXIT = 9;
    private boolean passwordShowing = false;
    private static final String TAG = "SignInActivity";
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameET = findViewById(R.id.usernameET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final ImageView passwordIcon = findViewById(R.id.PasswordIcon);
        final Button register = findViewById(R.id.register);
        final Button googlesign = findViewById(R.id.googlesign);
        final Button signIn = findViewById(R.id.signInbtn);
        final Button Forget = findViewById(R.id.ForgetPasswordbtn);
        
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // checking if password is showing or not
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show);
                }
                else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                }

                passwordET.setSelection(passwordET.length());
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (usernameET.getText().toString().contentEquals("")) {
                                              Toast.makeText(Login.this, "user can't be empty", Toast.LENGTH_SHORT).show();
                                          } else if (passwordET.getText().toString().contentEquals("")) {
                                              Toast.makeText(Login.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                                          } else {
                                              mAuth.signInWithEmailAndPassword(usernameET.getText().toString(), passwordET.getText().toString())
                                                      .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<AuthResult> task) {
                                                              if (task.isSuccessful()) {
                                                                  // Sign in success, update UI with the signed-in user's information
                                                                  Log.d(TAG, "signInWithEmail:success");

                                                                  FirebaseUser user = mAuth.getCurrentUser();

                                                                  if (user != null) {
                                                                      if (user.isEmailVerified()) {


                                                                          System.out.println("Email Verified : " + user.isEmailVerified());
                                                                          Intent HomeActivity = new Intent(Login.this, MainActivity.class);
                                                                          setResult(RESULT_OK, null);
                                                                          startActivity(HomeActivity);
                                                                          Login.this.finish();


                                                                      } else {

                                                                          //

                                                                      }
                                                                  }

                                                              } else {
                                                                  // If sign in fails, display a message to the user.
                                                                  Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                                  Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                                  if (task.getException() != null) {
                                                                      Toast.makeText(Login.this, "hii", Toast.LENGTH_SHORT).show();
                                                                  }

                                                              }

                                                          }
                                                      });
                                          }
                                      }
                                  });

        Forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forget = new Intent(Login.this,ForgetPassword.class);
                startActivity(forget);
            }
        });

        googlesign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent click = new Intent(Login.this,googlelogin.class);
                    startActivity(click);
                }
            });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regi = new Intent(Login.this,Register.class);
                startActivity(regi);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }
}