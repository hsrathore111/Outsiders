package com.example.himmat.outsider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MyDashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;

     //this for cheaking the data of the app
    private FirebaseAuth.AuthStateListener mAuthstatellistner;
    private RecyclerView mbloglist;

    private DatabaseReference mdatabase;
    private static Context mContext;
    private static DatabaseReference mdatabaseUser;
    private Query query;
   static String  post_key;

    private FirebaseAuth.AuthStateListener mAuthListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dashboard);
        mContext = this;
    //    mdatabase = FirebaseDatabase.getInstance().getReference().child("Bolg_posts");
        mAuth=FirebaseAuth.getInstance();

      // View textEntryView = getLayoutInflater().inflate(R.layout.blog_raw1,  null);      //   for deleteing the post from database as well as from the list
      // ImageButton deletepost=(ImageButton)textEntryView.findViewById(R.id.deletepostbutton); //   for deleteing the post from database as well as from the list

        String user_id=mAuth.getCurrentUser().getUid();// this is for try perpose in android app
        mdatabaseUser=FirebaseDatabase.getInstance().getReference().child("user");//Reaches the root directory at the data base
       // String  mcurrentuserid=mAuth.getCurrentUser().getUid();
        query=mdatabaseUser.orderByChild("uid").equalTo(user_id);

        mbloglist = (RecyclerView) findViewById(R.id.blog_list);

        mbloglist.setHasFixedSize(true);

        mbloglist.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, MyDashboard.Blogviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog,MyDashboard.Blogviewholder>(

                Blog.class,
                R.layout.blog_raw,
                MyDashboard.Blogviewholder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(MyDashboard.Blogviewholder viewHolder, Blog model, int position) {
                post_key=getRef(position).getKey();
                viewHolder.setAvailable_bed(model.getAvailable_bed());
                viewHolder.setFor_gender(model.getFor_gender());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setPhone_number(getApplicationContext(), model.getPhone_number());
                viewHolder.setHouse_number(model.getHouse_number());

            }
        };
        mbloglist.setAdapter(firebaseRecyclerAdapter);

    }

    private static class Blogviewholder extends RecyclerView.ViewHolder {

        View mview;
        TextView post_phonenumber;
        ImageButton deletepostbutton;
        ImageButton favoriteicon;
        public Blogviewholder(View itemView) {
            super(itemView);
            mview = itemView;
            post_phonenumber = (TextView) mview.findViewById(R.id.post_phonenumber);

            favoriteicon = (ImageButton) mview.findViewById(R.id.favoriteicon);      //this is to hide the favorite ican
            favoriteicon.setVisibility(View.GONE);                               //thi is to hide the favorite ican



            deletepostbutton = (ImageButton) mview.findViewById(R.id.deletepostbutton);      // this is als for delete button

            deletepostbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder  alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Confirm Delete...");
                    alertDialog.setMessage("Are you sure you want to delete this?");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                             mdatabaseUser.child(post_key).removeValue();

                            // Write your code here to invoke YES event
                            // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }

                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event

                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }


            });

            //   deletepostbutton.setVisibility(View.GONE);                                  //for delete button


                    post_phonenumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = post_phonenumber.getText().toString();
                    Intent dialer = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    mContext.startActivity(dialer);
                }
            });
        }




        private void setFor_gender(String for_gender) {
            TextView post_for_gender = (TextView) mview.findViewById(R.id.post_for_gender);
            post_for_gender.setText(for_gender);

        }

        private void setHouse_number(String house_number) {
            TextView post_housenumber = (TextView) mview.findViewById(R.id.post_housenumber);
            post_housenumber.setText(house_number);

        }

        private void setPhone_number(Context applicationContext, String phone_number) {

            post_phonenumber.setText(phone_number);

        }

        private void setAvailable_bed(String available_bed) {

            TextView post_available = (TextView) mview.findViewById(R.id.post_available);
            post_available.setText(available_bed);
        }


        private void setImage(Context ctx, String Image) {

            ImageView post_image = (ImageView) mview.findViewById(R.id.post_image);
            Picasso.with(ctx).load(Image).into(post_image);
        }
    }
}