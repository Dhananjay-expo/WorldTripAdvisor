package com.androideditiors.worldtripadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signuppage extends AppCompatActivity {

    EditText email,pass,cpass;
    Button signup;
    TextView signin;
    FirebaseAuth mAuth;
    private String TAG ="Hey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_signuppage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signuppage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        email = (EditText)findViewById(R.id.email3);
        pass  = (EditText)findViewById(R.id.password2);
        cpass = (EditText)findViewById(R.id.password3);
        signup= (Button)findViewById(R.id.signup3);
        signin=(TextView)findViewById(R.id.signin3);
        mAuth= FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signuppage.this,emailsign.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1= email.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String cpassword = cpass.getText().toString().trim();
                if(TextUtils.isEmpty(email1)){
                    email.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    pass.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(cpassword)){
                   cpass.setError("Required");
                    return;
                }
                if(password.equals(cpassword)==false){
                    Toast.makeText(getApplicationContext(),"password not matched",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(getApplicationContext(),"Password too short",Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email1,password)
                        .addOnCompleteListener(signuppage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(signuppage.this, "createUserWithEmail:onComplete:"+ task.isSuccessful(),Toast.LENGTH_SHORT).show();
                                if(!task.isSuccessful()){
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthWeakPasswordException e) {
                                        pass.setError("Weak Password");
                                        pass.requestFocus();
                                        return;
                                    } catch(FirebaseAuthInvalidCredentialsException e) {
                                        email.setError("Invalid Email");
                                        email.requestFocus();
                                        return;
                                    } catch(FirebaseAuthUserCollisionException e) {
                                       email.setError("Already account");
                                       email.requestFocus();
                                       return;
                                    } catch(Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        return;
                                    }
                                }else {
                                    if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                        FirebaseUser user= mAuth.getCurrentUser();
                                        String email=user.getEmail();
                                        String uid =user.getUid();
                                        HashMap<Object,String> hashMap=new HashMap<>();
                                        hashMap.put("email",email);
                                        hashMap.put("uid",uid);
                                        hashMap.put("name","");
                                        hashMap.put("username","");
                                        hashMap.put("image","");
                                        hashMap.put("cover","");
                                        hashMap.put("phone","");
                                        hashMap.put("location","");
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(uid).setValue(hashMap);
                                    }
                                    sendEmailVerification();
                                }
                            }
                        });
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(signuppage.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(signuppage.this,emailsign.class));
                    }else{
                        Toast.makeText(signuppage.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
