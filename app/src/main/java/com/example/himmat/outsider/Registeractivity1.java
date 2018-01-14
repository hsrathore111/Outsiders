package com.example.himmat.outsider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.design.widget.NavigationView.*;
import static com.example.himmat.outsider.R.layout.nav_header_main;

public class Registeractivity1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText mnamefield;

    private EditText memailfield;


    private EditText mpasswordfield;

    private Button mregisterbutton;

    private FirebaseAuth mAuth;

    private ProgressDialog mprogress;

    private DatabaseReference mdatabase;

    private TextView nav_mail;

    TextView nav_name;

    public NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Owner");

        mprogress= new ProgressDialog(this);

        mnamefield=(EditText)findViewById(R.id.namefild);

        memailfield=(EditText)findViewById(R.id.emailfield);

        mpasswordfield=(EditText)findViewById(R.id.passwordfield);


        mregisterbutton=(Button)findViewById(R.id.registerbutton);


        navigationView = (NavigationView) findViewById(R.id.nav_view);



        //  View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        //  TextView tv = (TextView)hView.findViewById(R.id.emailset);
        //        tv.setText("himmat11.hsr@gmail.com");

        mregisterbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startResister();

            }
        });

    }

    private void startResister() {

        final   String name=mnamefield.getText().toString().trim();
        final   String email=memailfield.getText().toString().trim();
        String password=mpasswordfield.getText().toString().trim();

        if(!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)) {

            mprogress.setMessage("signing Up......");

            mprogress.show();

            mAuth.createUserWithEmailAndPassword(email ,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        String user_id=mAuth.getCurrentUser().getUid();

                        DatabaseReference current_user=mdatabase.child(user_id);
                        current_user.child("name").setValue(name);
                        current_user.child("default").setValue("default");
                        mprogress.dismiss();

                        Intent mainintent=new Intent(Registeractivity1.this,MainActivity.class);
                        mainintent.putExtra("name",name);
                        mainintent.putExtra("email",email);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainintent);

                    }

                }

            });

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
