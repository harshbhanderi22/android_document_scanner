package com.harshbhanderi.hbscanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DisplayImage extends AppCompatActivity {

    private RecyclerView rv;
    private List<String> images_uri;
    private List<String> image_name;
    private RAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
//        rv=findViewById(R.id.recyclerView);
//        Intent i=getIntent();
//        int number=i.getIntExtra(MainActivity.image_count,10);
//        Log.d("count", String.valueOf(number));
//        String[] list=new String[number];
//        list=i.getStringArrayExtra(MainActivity.list);
//        images_uri=new ArrayList<>();
//        image_name=new ArrayList<>();
//        for (int j=0;j<number;j++)
//        {
//            images_uri.add(list[j]);
//            Log.d("imageuri",list[j]);
//            image_name.add("Image" + (j+1));
//            Log.d("imageuri", "Image"+(j+1));
//        }
//        adapter=new RAdapter(DisplayImage.this,images_uri,image_name);
//        GridLayoutManager gridLayoutManager=new GridLayoutManager(DisplayImage.this,2,GridLayoutManager.VERTICAL,false);
//        rv.setLayoutManager(gridLayoutManager);
//        rv.setHasFixedSize(false);
//        rv.setAdapter(adapter);



    }
}