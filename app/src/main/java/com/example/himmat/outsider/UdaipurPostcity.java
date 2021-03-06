package com.example.himmat.outsider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Ref;

import static com.example.himmat.outsider.R.mipmap.if_favorite;

public class UdaipurPostcity extends AppCompatActivity {

    private RecyclerView mbloglist;

    private DatabaseReference mdatabase;

    private DatabaseReference mdatabasefavorite;

    private static Context mContext;

    private FirebaseAuth mAuth;

    private  boolean mprocessfavorite=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udaipur_postcity);
        mContext = this;

        mAuth=FirebaseAuth.getInstance();

        mdatabasefavorite = FirebaseDatabase.getInstance().getReference().child("favorite");
        mdatabasefavorite.keepSynced(true);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Bolg_posts");
        mdatabase.keepSynced(true);
        mbloglist = (RecyclerView) findViewById(R.id.blog_list);

        mbloglist.setHasFixedSize(true);

        mbloglist.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, Blogviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, Blogviewholder>(

                Blog.class,
                R.layout.blog_raw,
                Blogviewholder.class,
                mdatabase

        ) {
            @Override
            protected void populateViewHolder(Blogviewholder viewHolder, final Blog model, int position) {

            final   String post_key= getRef(position).getKey();

                viewHolder.setAvailable_bed(model.getAvailable_bed());
                viewHolder.setFor_gender(model.getFor_gender());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setPhone_number(getApplicationContext(), model.getPhone_number());
                viewHolder.setHouse_number(model.getHouse_number());
                viewHolder.setFavorite(post_key);

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {   //  this is for whole blog post here i will use putExtramethos
                    @Override
                    public void onClick(View v) {

                        Intent singlepostintent=new Intent(UdaipurPostcity.this,SinglepostActivity.class);
                        singlepostintent.putExtra("post_id",post_key);
                        startActivity(singlepostintent);

                        //Toast.makeText(getApplicationContext(),post_key,Toast.LENGTH_LONG).show();

                    }
                });

                viewHolder.favoriteicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                         mprocessfavorite=true;

                           mdatabasefavorite.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {

                                   if(mprocessfavorite) {
                                       if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                           mdatabasefavorite.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                           mprocessfavorite = false;
                                       }
                                       else {


                                           DatabaseReference newpost= mdatabasefavorite.child(post_key);
                                           newpost.child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());
                                           newpost.child("image").setValue(model.getImage());
                                           newpost.child("house_number").setValue(model.getHouse_number());
                                           newpost.child("phone_number").setValue(model.getPhone_number());
                                           newpost.child("for_gender").setValue(model.getFor_gender());
                                           newpost.child("available_bed").setValue(model.getAvailable_bed());
                                          // newpost.child("uid").setValue(mAuth.getCurrentUser().getUid());

                                           mprocessfavorite = false;
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {


                               }
                           });
                    }
                });

                viewHolder.sharebutton.setOnClickListener(new View.OnClickListener() {    //this is for share option okk
                    @Override
                    public void onClick(View v) {
                        Intent shareintent =new Intent(Intent.ACTION_SEND);
                        shareintent.setType("text/plain");
                        String hy="himmat";
                        shareintent.putExtra(Intent.EXTRA_TEXT,hy);
                        startActivity(Intent.createChooser(shareintent,"share using"));
                    }
                });
            }

        };
        mbloglist.setAdapter(firebaseRecyclerAdapter);

        firebaseRecyclerAdapter.notifyDataSetChanged();  //  while testing the app

    }

    private static class Blogviewholder extends RecyclerView.ViewHolder {

        View mview;
        TextView post_phonenumber;
        ImageButton favoriteicon;

        ImageButton sharebutton;
        ImageButton deletepostbutton;
        DatabaseReference mdatabasefavorites;
        FirebaseAuth mauth;

        public Blogviewholder(View itemView) {
            super(itemView);
            mview = itemView;
            post_phonenumber = (TextView) mview.findViewById(R.id.post_phonenumber);

            favoriteicon=(ImageButton)mview.findViewById(R.id.favoriteicon);
            sharebutton=(ImageButton)mview.findViewById(R.id.sharebutton);         // this is for share option okkk

            deletepostbutton=(ImageButton)mview.findViewById(R.id.deletepostbutton);      // this is als for delete button

            deletepostbutton.setVisibility(View.GONE);                                  //for delete button

            mdatabasefavorites=FirebaseDatabase.getInstance().getReference().child("favorite");

            mauth=FirebaseAuth.getInstance();
            mdatabasefavorites.keepSynced(true);

            post_phonenumber.setOnClickListener(new View.OnClickListener() {                         //  this is for talipone number;d
               @Override
               public void onClick(View v) {
                   String  phone   = post_phonenumber.getText().toString();
                   Intent dialer= new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",phone,null));
                   mContext.startActivity(dialer);
               }
           });
        }

        public void setFavorite(final String post_key){

             mdatabasefavorites.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {

                     if(dataSnapshot.child(post_key).hasChild(mauth.getCurrentUser().getUid()))

                         favoriteicon.setImageResource(R.mipmap.if_favorite);

                     else {

                        favoriteicon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                     }
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

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

            TextView post_available=(TextView)mview.findViewById(R.id.post_available);
            post_available.setText(available_bed);
        }


        private void setImage(Context ctx, String Image) {

            ImageView post_image = (ImageView) mview.findViewById(R.id.post_image);
            Picasso.with(ctx).load(Image).into(post_image);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add)
        {
           startActivity(new Intent(UdaipurPostcity.this,Commonpostcity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}