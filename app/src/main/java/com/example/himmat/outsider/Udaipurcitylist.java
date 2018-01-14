package com.example.himmat.outsider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Udaipurcitylist extends AppCompatActivity {

    private ListView listviewudaipur;
    Toolbar toolbar;
    private final String[] value = new String[]{"C-sector", "D-sector", "O-sector", "A-sector", "Bapu nager", "patal nager", "shastri nager"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udaipurcitylist);
        listviewudaipur = (ListView) findViewById(R.id.listviewudaipur);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.udaipurlistview, R.id.udaipurlistviewid, value);

        listviewudaipur.setAdapter(adapter);

        listviewudaipur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position==0) {

                Intent myintent=new Intent(view.getContext(),UdaipurPostcity.class);
                    startActivity(myintent);

                }


            }
        });


     }
  }