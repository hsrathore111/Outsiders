package com.example.himmat.outsider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;

import static com.example.himmat.outsider.R.id.imageset;
import static com.example.himmat.outsider.R.id.selectimage;
import static com.example.himmat.outsider.R.id.start;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

     @SuppressWarnings("VisibleForTests") Uri downlodeuri ;//  this is for user profile  in android studio
    private FirebaseAuth mAuth;
    NavigationView navigationView;
    private   CircleImageView circleImageViewprofile;
    private FirebaseAuth.AuthStateListener mAuthstatellistner;
    String email;
    String name;

    String currentUserID;
    View hView;
    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;
    IslandAdapter islandAdapter;
    public List<IslandModel> islandList;
     DatabaseReference mRef ;
 //   ImageView nav_photo;
    private Uri mimageuri=null;

    private StorageReference mstorage;                     //this is for profile image set up

   // StorageReference filepaths;
    //this is for profile setup.

    private DatabaseReference mdatabaseUser;               //this is for immage set up for profile

    private static final  int CAMERA_PIC_REQUEST =1;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            mstorage = FirebaseStorage.getInstance().getReference();  // thisis for profile perpose


            mdatabaseUser= FirebaseDatabase.getInstance().getReference().child("usergroup");  // this is for profile of the user

            //   hView =  navigationView.getHeaderView(0);
                circleImageViewprofile= (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageset);

                 circleImageViewprofile.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                       gallaryIntent.setType("image/*");
                       startActivity(gallaryIntent);
                       startActivityForResult(gallaryIntent,CAMERA_PIC_REQUEST);

                   }
              });

       /*    mRef = FirebaseDatabase.getInstance().getReference().child("usergroup");
           mRef.keepSynced(true);
             currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference newRef = mRef.child(currentUserID);
            newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  //  hView =  navigationView.getHeaderView(0);
                    name = String.valueOf(dataSnapshot.child("name").getValue());

                    email = String.valueOf(dataSnapshot.child("email").getValue());

                    String imageurl=String.valueOf(dataSnapshot.child("Image").getValue());

   //            Uri profile_picture= Uri.parse(String.valueOf(dataSnapshot.child("image").getValue()));


                 //  Toast.makeText(getApplicationContext(),profile_picture.toString(),Toast.LENGTH_LONG).show();


                    TextView nav_user = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nameset);
                    TextView nav_email=(TextView)navigationView.getHeaderView(0).findViewById(R.id.emailset);

                    circleImageViewprofile=(CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageset);

                    nav_user.setText(name);

                    nav_email.setText(email);

                  Picasso.with(MainActivity.this).load(imageurl).into(circleImageViewprofile);
                  //     Toast.makeText(getApplicationContext(),downlodeuri.toString(),Toast.LENGTH_LONG).show();
                   // circleImageViewprofile.setImageURI(profile_picture);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                   // Toast.makeText(getApplicationContext(),"Data is not fatching",Toast.LENGTH_LONG).show();
                }
            });
*/
            FabSpeedDial fabSpeedDial=(FabSpeedDial)findViewById(R.id.fabbutton);
            fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
                @Override
                public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                    return true;
                }

                @Override
                public boolean onMenuItemSelected(MenuItem menuItem)  {
                    if(menuItem.getItemId()==R.id.action_post) {

                      startActivity( new Intent(MainActivity.this,Commonpostcity1.class));

                    }
                    return true;
                }

                @Override
                public void onMenuClosed() {

                }
            });

            mAuth=FirebaseAuth.getInstance();

            mAuthstatellistner= new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                     if(firebaseAuth.getCurrentUser()==null) {

                        Intent loginintent=new Intent(MainActivity.this,Owner_telnant.class);

                        startActivity(loginintent);
                     }
                }
            };


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

             DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
             ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
             drawer.addDrawerListener(toggle);
             toggle.syncState();

            recyclerView = (RecyclerView) findViewById(R.id.islandRecyclerView);

            recyclerView.setHasFixedSize(true);

            GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(gridLayoutManager);
            // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            // recyclerView.setLayoutManager(linearLayoutManager);

            islandList = new ArrayList<>();

            islandList.add(new IslandModel("CTAE udaipur", R.mipmap.udaipur));
            islandList.add(new IslandModel("Antigua & Barbuda",R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Aruba", R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Bahamas", R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Barbados", R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Bonaire", R.mipmap.ic_launcher));
            islandList.add(new IslandModel("British Virgin Islands",R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Cayman Islands", R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Cuba", R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Curacao", R.mipmap.ic_launcher));
            islandList.add(new IslandModel("Dominica", R.mipmap.ic_launcher));

            islandAdapter = new IslandAdapter(islandList, this);
            recyclerView.setAdapter(islandAdapter);


        }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {

            Uri imageuri = data.getData();
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mimageuri = result.getUri();
                //  Toast.makeText(getApplicationContext(),"Request code id failed....",Toast.LENGTH_LONG).show();
                 circleImageViewprofile.setImageURI(mimageuri);
                 startpostprofileimage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthstatellistner);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_logout)
        {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_slideshow) {

         Intent mydashboard=new Intent(MainActivity.this,MyDashboard.class);
             startActivity(mydashboard);

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

          Intent my_favorite=new Intent(MainActivity.this,My_Favorites.class);
           startActivity(my_favorite);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//----------------------------------------------------user profile perpose-----------------------------------------

   private void startpostprofileimage() {
       StorageReference filepath = mstorage.child("blog image").child(mimageuri.getLastPathSegment());
       filepath.putFile(mimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                 downlodeuri = taskSnapshot.getDownloadUrl();
               final DatabaseReference newpost=mdatabaseUser.child(currentUserID);
               mdatabaseUser.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                    newpost.child("Image").setValue(downlodeuri.toString());

                   }
                   @Override
                   public void onCancelled(DatabaseError databaseError) {
                         Toast.makeText(getApplicationContext(),"Error in user profile",Toast.LENGTH_LONG).show();
                   }
               });

             }
        });
   }
 //-------------------------------------------------------------------------------------------------------------
}
