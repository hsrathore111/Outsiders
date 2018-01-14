package com.example.himmat.outsider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Commonpostcity extends AppCompatActivity {

    private ImageButton selectimage;
    private StorageReference mstorage;
    private EditText decription;

    private FirebaseUser mcurrentusers;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabaseusers;
    private DatabaseReference mdatabaseUser;
    TextView tvIsValidPhone;

    EditText edtPhone;

    CountryCodePicker ccp;

    Button btnValidate;


    private EditText house_number;

    private DatabaseReference mdatabase;

    private static final int CAMERA_PIC_REQUEST = 1;

    private Button post_button;

    private Uri mimageuri=null;

    private Spinner cspinner;

    private Spinner mspinner;

    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonpostcity);

        mAuth=FirebaseAuth.getInstance();

        mcurrentusers=mAuth.getCurrentUser();
        mdatabaseUser= FirebaseDatabase.getInstance().getReference().child("user");  //   this is for try perpose in android app

        mdatabaseusers=FirebaseDatabase.getInstance().getReference().child("user").child(mcurrentusers.getUid());

        mdatabase= FirebaseDatabase.getInstance().getReference().child("Bolg_posts");

        selectimage = (ImageButton) findViewById(R.id.selectimage);

        mspinner=(Spinner)findViewById(R.id.mspinner);

        List<String> categorie = new ArrayList<String>();
        categorie.add("bed available");
        categorie.add("no bed available");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categorie);

        mspinner.setAdapter(adapter);

        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s1 =parent.getItemAtPosition(position).toString();

               // Toast.makeText(getApplication(),"selected"+s1,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cspinner=(Spinner)findViewById(R.id.cspinner);//spinner view


        List<String> categories = new ArrayList<String>();
        categories.add("for boys");
        categories.add("for girls");

        ArrayAdapter<String> cadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categories);
        cspinner.setPrompt("select");
        cspinner.setAdapter(cadapter);

        cspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      // String s1 =parent.getItemAtPosition(position).toString();

                   //   Toast.makeText(getApplication(),"selected"+s1,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ccp=(CountryCodePicker)findViewById(R.id.ccp);
        tvIsValidPhone = (TextView) findViewById(R.id.tvIsValidPhone);
        edtPhone = (EditText) findViewById(R.id.edtPhoneNumber);
        btnValidate = (Button) findViewById(R.id.btnValidate);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(getApplicationContext(), " please chouse country as india only ", Toast.LENGTH_SHORT).show();
            }
        });

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String countryCode = 91+"";
                // String countryCode=String.valueOf(91);
                String phoneNumber = edtPhone.getText().toString().trim();
                if(countryCode.length() > 0 && phoneNumber.length() > 0){
                    if(isValidPhoneNumber(phoneNumber)){
                        boolean status = validateUsing_libphonenumber(countryCode, phoneNumber);
                        if(status){

                           // tvIsValidPhone.setText("Valid Phone Number (libphonenumber)");


                        } else {
                            tvIsValidPhone.setText("Invalid Phone Number (libphonenumber)");
                        }
                    }
                    else {
                        tvIsValidPhone.setText("Invalid Phone Number (isValidPhoneNumber)");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Country Code and Phone Number is required", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mstorage = FirebaseStorage.getInstance().getReference();


        mprogress = new ProgressDialog(this);

        house_number=(EditText)findViewById(R.id.house_number);



      //  post_button = (Button) findViewById(R.id.post_button);

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                gallaryIntent.setType("image/*");

                startActivityForResult(gallaryIntent,CAMERA_PIC_REQUEST);

            }
        });
    }
    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            // phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (NumberParseException e) {
            System.err.println(e);
        }

        boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        if (isValid) {
            String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

         //  Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            Startposting();


            return true;
        } else {
            Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void Startposting() {

          final String house_value = house_number.getText().toString().trim();
          final String bed_value = mspinner.getSelectedItem().toString().trim();
          final String phone_number= edtPhone.getText().toString().trim();
          final String for_gender = cspinner.getSelectedItem().toString().trim();

        if (!TextUtils.isEmpty(phone_number) && !TextUtils.isEmpty(for_gender) && !TextUtils.isEmpty(house_value)&&
                !TextUtils.isEmpty(bed_value)&&mimageuri != null) {

            mprogress.setMessage("your data is loading.......");
            mprogress.show();

            StorageReference filepath = mstorage.child("blog image").child(mimageuri.getLastPathSegment());
            filepath.putFile(mimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   final @SuppressWarnings("VisibleForTests") Uri downlodeuri = taskSnapshot.getDownloadUrl();
                   final DatabaseReference newpost=mdatabase.push();

                 mdatabaseusers.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         newpost.child("house_number").setValue(house_value);
                         newpost.child("Image").setValue(downlodeuri.toString());
                         newpost.child("phone_number").setValue(phone_number);
                         newpost.child("for_gender").setValue(for_gender);
                         newpost.child("available_bed").setValue(bed_value);

//------------------------------------------------------this data will add to the user directory-----------------------------

                         String user_id=mAuth.getCurrentUser().getUid();// this is for try perpose in android app
                         DatabaseReference current_user=mdatabaseUser.push();  // this is for try perpose in android app
                         current_user.child("uid").setValue(user_id);  //his is for try perpose in android app
                         current_user.child("house_number").setValue(house_value);// this is for try perpose in android app
                         current_user.child("Image").setValue(downlodeuri.toString());// this is for try perpose in android app
                         current_user.child("phone_number").setValue(phone_number);// this is for try perpose in android app
                         current_user.child("for_gender").setValue(for_gender);// this is for try perpose in android app
                         current_user.child("available_bed").setValue(bed_value);

                         newpost.child("uid").setValue(mcurrentusers.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {

                                 if(task.isSuccessful())
                                 {

                                     startActivity(new Intent(Commonpostcity.this,UdaipurPostcity.class));

                                 }else{
                                     Toast.makeText(getApplicationContext(),"you got an Error while loadig data..",Toast.LENGTH_LONG).show();

                                 }
                             }
                         });    //uid of the user i database

                         mprogress.dismiss();
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
           }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(),"please filed all the field  of the form.... ",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {

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
                selectimage.setImageURI(mimageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
             }
         }
    }
}