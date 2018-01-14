package com.example.himmat.outsider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.himmat.outsider.R.id.mspinner;

public class Commonpostcity2 extends AppCompatActivity {

    private Spinner comspinner;
    private Button postnear;
    private RadioGroup radioGroup1,radioGroup2,radioGroup3,radioGroup4,radioGroup5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonpostcity2);

        comspinner=(Spinner)findViewById(R.id.comspinner);
        postnear=(Button)findViewById(R.id.postnear);
        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup2=(RadioGroup)findViewById(R.id.radioGroup2);
        radioGroup3=(RadioGroup)findViewById(R.id.radioGroup3);
        radioGroup4=(RadioGroup)findViewById(R.id.radioGroup4);
        radioGroup5=(RadioGroup)findViewById(R.id.radioGroup5);


        postnear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(radioGroup1.getCheckedRadioButtonId() != -1 && radioGroup2.getCheckedRadioButtonId()!=-1 &&radioGroup3.getCheckedRadioButtonId() != -1
                    && radioGroup4.getCheckedRadioButtonId()!=-1
                    &&radioGroup5.getCheckedRadioButtonId() != -1 )
                {
                      Intent postintent=new Intent(Commonpostcity2.this,Commonpostcity.class);

                    String Text  = comspinner.getSelectedItem().toString();
                    final String value =((RadioButton)findViewById(radioGroup1.getCheckedRadioButtonId())).getText().toString();
                    final String value1 =((RadioButton)findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString();
                    final String value2 =((RadioButton)findViewById(radioGroup3.getCheckedRadioButtonId())).getText().toString();
                    final String value3 =((RadioButton)findViewById(radioGroup4.getCheckedRadioButtonId())).getText().toString();
                    final String value4 =((RadioButton)findViewById(radioGroup5.getCheckedRadioButtonId())).getText().toString();

                    postintent.putExtra("",value);
                    postintent.putExtra("",value1);
                    postintent.putExtra("",value2);
                    postintent.putExtra("",value3);
                    postintent.putExtra("",value4);
                    postintent.putExtra("",Text);
                    startActivity(postintent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please provide all the information regarding this form ",Toast.LENGTH_LONG).show();
                }

            }
        });
        List<String> categorie = new ArrayList<String>();
        categorie.add("C sector");
        categorie.add("O sector");
        categorie.add("Bapu nager");
        categorie.add("D sector");
        categorie.add("patal nager");
        categorie.add("B sector");
        categorie.add("Dvarika koloni");
        categorie.add("shastri nager");


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categorie);

        comspinner.setAdapter(adapter);

        comspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s1 =parent.getItemAtPosition(position).toString();

                // Toast.makeText(getApplication(),"selected"+s1,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
