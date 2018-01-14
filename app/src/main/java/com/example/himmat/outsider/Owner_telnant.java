package com.example.himmat.outsider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



public class Owner_telnant extends AppCompatActivity {

    Button userbutton;
    private RadioButton cheakbox1;
    private RadioButton cheakbox2;
    private  RadioButton radioButton;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_telnant);
        userbutton=(Button)findViewById(R.id.userbutton);


        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        cheakbox1=(RadioButton) findViewById(R.id.cheakbox1);
        cheakbox2=(RadioButton) findViewById(R.id.ckeakbox2);
        userbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  int selectedid=radioGroup.getCheckedRadioButtonId();

             //  radioButton =(RadioButton)findViewById(selectedid);

                if(cheakbox2.isChecked()) {

                    Intent owner=new Intent(Owner_telnant.this,LoginActivity.class);
                    startActivity(owner);
                }

                else if(cheakbox1.isChecked()){

                 startActivity(new Intent(Owner_telnant.this,Owner_activity.class));

                }
                else {

                    Toast.makeText(getApplicationContext(),"please let us know what you are",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
