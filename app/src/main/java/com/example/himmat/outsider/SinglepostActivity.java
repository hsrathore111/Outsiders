package com.example.himmat.outsider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglepostActivity extends AppCompatActivity {

    private String mpost_key=null;

    private DatabaseReference mdatabase;
    private ImageView singlepostimage;
    private TextView singlepostforgender;
    private TextView  singlepostforbad;
    private  TextView singlepostforphone;
    private  TextView singleposthouseno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepost);


         mpost_key   =getIntent().getExtras().getString("post_id");

        singlepostforbad =(TextView)findViewById(R.id.singlepostforbad);
        singlepostforgender=(TextView)findViewById(R.id.singlepostforgender);
        singlepostforphone=(TextView)findViewById(R.id.singlepostforphone);
        singlepostimage=(ImageView)findViewById(R.id.singlepostimage);
       singleposthouseno =(TextView)findViewById(R.id.singleposthouseno);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Bolg_posts");

       // Toast.makeText(getApplicationContext(),post_key,Toast.LENGTH_LONG).show();
           mdatabase.child(mpost_key).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 String post_image=dataSnapshot.child("Image").getValue().toString();
                 String gender=dataSnapshot.child("for_gender").getValue().toString();
                 String bad=dataSnapshot.child("available_bed").getValue().toString();
                 String house=dataSnapshot.child("house_number").getValue().toString();
                 String mobile=dataSnapshot.child("phone_number").getValue().toString();
                 String post_uid=dataSnapshot.child("uid").getValue().toString();

                singlepostforphone.setText(mobile);

                singlepostforbad.setText(bad);

                singlepostforgender.setText(gender);

                singleposthouseno .setText(house);

                 Picasso.with(SinglepostActivity.this).load(post_image).into(singlepostimage);

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {



             }
         });
    }
}
