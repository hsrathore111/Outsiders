package com.example.himmat.outsider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Commonpostcity1 extends AppCompatActivity {

    RecyclerView recyclerView;
    IslandAdapter1 islandAdapter1;
    public List<IslandModel1> islandList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonpostcity1);

        recyclerView = (RecyclerView) findViewById(R.id.islandRecyclerView1);

        recyclerView.setHasFixedSize(true);

         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(linearLayoutManager);

        islandList1 = new ArrayList<>();

        islandList1.add(new IslandModel1( R.mipmap.udaipur));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));
        islandList1.add(new IslandModel1(R.mipmap.owner));

        islandAdapter1 = new IslandAdapter1(islandList1,this);
        recyclerView.setAdapter(islandAdapter1);

    }
}


