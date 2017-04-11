package com.example.varun.listpractise;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by AndroidBash on 10/07/16
 */

public class Signup extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AndroidBash";
    //Add YOUR Firebase Reference URL instead of the following URL
    private Firebase mRef;
    private User user;
    private EditText name;
    private EditText phoneNumber;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Firebase.setAndroidContext(this);



        mRef = new Firebase("https://boiling-fire-2749.firebaseio.com");


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        signup = (Button) findViewById(R.id.btn_signup);
        signup.setOnClickListener(this);
    }

    public void onClick(View v){
        if(v==signup){
            onSignUpClicked(v);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    //This method sets up a new User by fetching the user entered details.
    protected void setUpUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    public void onSignUpClicked(View view) {
        Log.e("step","1");
        createNewAccount(email.getText().toString(), password.getText().toString());
        showProgressDialog();
    }


    private void createNewAccount(String email, String password) {
        Log.d(TAG, "createNewAccount:" + email);
        if (!validateForm()) {
            return;
        }
        Log.e("step","2");

        //This method sets up a new User by fetching the user entered details.
        setUpUser();
        Log.e("step","3");

        //This method  method  takes in an email address and password, validates them and then creates a new user
        // with the createUserWithEmailAndPassword method.
        // If the new account was created, the user is also signed in, and the AuthStateListener runs the onAuthStateChanged callback.
        // In the callback, you can use the getCurrentUser method to get the user's account data.

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            onAuthenticationSucess(task.getResult().getUser());
                        }


                    }
                });
        Log.e("step","4");


    }

    private void onAuthenticationSucess(FirebaseUser mUser) {
        // Write new user
        saveNewUser(user.getEmail(), user.getPassword());
        signOut();
        Log.e("step","5");

        // Go to LoginActivity
        startActivity(new Intent(Signup.this, Login.class));
        finish();
    }

    private void saveNewUser(String email, String password) {
        User user = new User(email,password);
        Log.e("step","6");

        mRef.child("users").setValue(user);
        Log.e("step","7");

    }


    private void signOut() {
        mAuth.signOut();
    }
    //This method, validates email address and password
    private boolean validateForm() {
        boolean valid = true;

        String userEmail = email.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("hello yar");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}

//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.firebase.client.Firebase;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
///**
// * Created by silicon on 4/4/17.
// */
//
//
//public class Signup extends AppCompatActivity implements View.OnClickListener {
//    private static final String TAG = "AndroidBash";
//    //Add YOUR Firebase Reference URL instead of the following URL
//    private Firebase mRef = new Firebase("https://boiling-fire-2749.firebaseio.com");
//
//    private EditText editTextEmail;
//    private EditText editTextPassword;
//    private Button buttonSignup;
//    private TextView loginlink;
//    private Dialog progressDialog;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        //initializing views
//        editTextEmail = (EditText) findViewById(R.id.input_email);
//        editTextPassword = (EditText) findViewById(R.id.input_password);
//        loginlink= (TextView) findViewById(R.id.link_login);
//
//        buttonSignup = (Button) findViewById(R.id.btn_signup);
//
//        progressDialog = new ProgressDialog(this);
//
//        //attaching listener to button
//        buttonSignup.setOnClickListener(this);
//        loginlink.setOnClickListener(this);
//    }
//    public void registerUser(String email,String password){
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//                        hideProgressDialog();
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(Signup.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            onAuthenticationSucess(task.getResult().getUser());
//                        }
//
//
//                    }
//                });
//
//    }
//
//    private void onAuthenticationSucess(FirebaseUser mUser) {
//        // Write new user
//        saveNewUser(mUser.getUid(), user.getName(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
//        signOut();
//        // Go to LoginActivity
//        startActivity(new Intent(Signup.this, Login.class));
//        finish();
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        if(view == buttonSignup){
//            registerUser(editTextEmail.getText().toString(),editTextPassword.getText().toString());
//        }
//
//        if(view == loginlink){
//            //open login activity when user taps on the already registered textview
//            startActivity(new Intent(this, Login.class));
//        }
//
//    }
//}