package com.example.himmat.outsider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class LoginActivity extends AppCompatActivity {

    private EditText loginemail;
    private EditText loginpassword;
    private StorageReference mstorage;

    private Button loginbutton;

    private Button loginregister;

    private FirebaseAuth mAuth;

    private ProgressDialog mprogress;

    private SignInButton mgoogleloginbutton;

    private DatabaseReference mDatabaseUsers;

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 1;

    private DatabaseReference mdatabaseUser;


    String user_id=null;

    String personName;
    String personEmail;
    Uri personPhotourl=null;
     private @SuppressWarnings("VisibleForTests") Uri downlodeuri;
    StorageReference filepath;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mprogress = new ProgressDialog(this);

        loginemail = (EditText) findViewById(R.id.loginemail);

        loginregister = (Button) findViewById(R.id.loginregister);

        mgoogleloginbutton = (SignInButton) findViewById(R.id.googleloginbutton);

        mdatabaseUser= FirebaseDatabase.getInstance().getReference().child("usergroup");  // User group of the database

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("user");

        mAuth = FirebaseAuth.getInstance();

        loginpassword = (EditText) findViewById(R.id.loginpassword);

        loginbutton = (Button) findViewById(R.id.loginbutton);



        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cheaklogin();

            }
        });


        loginregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntnt = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntnt);

            }
        });

        //----------------------------------------------google login option---------------------------------------------//

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

            mgoogleloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            mprogress.setMessage("Starting sign in....");
            mprogress.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                  personName = account.getDisplayName();
                  personEmail = account.getEmail();
                personPhotourl  = account.getPhotoUrl();
                  firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                mprogress.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {

                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.  ", Toast.LENGTH_SHORT).show();


                        } else {
                            mprogress.dismiss();
                            user_id=mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user=mDatabaseUsers.child(user_id);
                            current_user.child("name").setValue(personName);
                            current_user.child("email").setValue(personEmail);



                             DatabaseReference usergroup=mdatabaseUser.child(user_id);
                             usergroup.child("name").setValue(personName);
                             usergroup.child("email").setValue(personEmail);
                             CheakuserExist();



                        }
                    }});
    }

    private void cheaklogin() {

      final String email = loginemail.getText().toString().trim();
      final   String password = loginpassword.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mprogress.setMessage("login.....");
            mprogress.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        CheakuserExist();
                        mprogress.dismiss();

                    } else
                        {
                            mprogress.dismiss();
                            Toast.makeText(getApplicationContext(), "You are not the registered user.", Toast.LENGTH_LONG).show();
                        }

                }
            });
        }
    }
    private void CheakuserExist() {

        if (mAuth.getCurrentUser()!= null) {

            final String user_uid = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_uid)) {

                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);


                    }
                    else {


                   //   Toast.makeText(getApplicationContext(),"Error Login..", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {


                }});
        }
    }
}


