package com.androideditiors.worldtripadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class emailsign extends AppCompatActivity {
    EditText email,password;
    Button signin;
    TextView forgot,signup;
    private FirebaseAuth mAuth;
    private static String TAG="Hey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_emailsign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Sign in");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(emailsign.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.txtEmail2);
        password = (EditText)findViewById(R.id.password);
        signin = (Button)findViewById(R.id.signin4);
        forgot = (TextView)findViewById(R.id.forgot);
        signup = (TextView)findViewById(R.id.signup2);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                if(TextUtils.isEmpty(Email)){
                    email.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    email.setError("Required");
                    return;
                }
                mAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(emailsign.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                        Toast.makeText(emailsign.this, "Login Failed", Toast.LENGTH_LONG).show();
                                } else {
                                    checkEmailVerification();
                                }
                            }
                        });
            }
        });
    }
    private void checkEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if(emailflag){
            finish();
            startActivity(new Intent(emailsign.this, mainpage.class));
        }else{
            Toast.makeText(this, "Please Verify your email", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }
}
