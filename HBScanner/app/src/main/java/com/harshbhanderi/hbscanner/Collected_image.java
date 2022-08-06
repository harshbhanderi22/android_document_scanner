package com.harshbhanderi.hbscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Collected_image extends AppCompatActivity {
    private List<String> images_uri;
    private List<String> image_name;
    String[] list;
    private ImageView collected_images;
    private Button crop_btn,bright_btn,light_btn,magic_btn,black_btn,next_btn,prev_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_image);


        //Buttons in Activity
        crop_btn=findViewById(R.id.crop);
        bright_btn=findViewById(R.id.bright);
        light_btn=findViewById(R.id.light);
        magic_btn=findViewById(R.id.magic);
        black_btn=findViewById(R.id.black);
        next_btn=findViewById(R.id.next);
        prev_btn=findViewById(R.id.prev);

        //Images in Activity
        collected_images=findViewById(R.id.collectedImage);

        //Intent from MainActivity
        Intent i=getIntent();
        int number=i.getIntExtra(MainActivity.image_count,10);
        list=i.getStringArrayExtra(MainActivity.list);

        //Initialize list for ImageUri and ImageName
        images_uri=new ArrayList<>();
        image_name=new ArrayList<>();

        //String[] to list
        for (int j=0;j<number;j++)
        {
            images_uri.add(list[j]);
            Log.d("imageuri",list[j]);
            image_name.add("Image" + (j+1));
            Log.d("imageuri", "Image"+(j+1));
        }

        collected_images.setImageURI(Uri.parse(images_uri.get(0)));
        final int[] current = {0};
        int min=0;
        int max=images_uri.size()-1;
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current[0] <max)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(current[0] +1)));
                    current[0]++;
                }
                else if (current[0]==max)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(0)));
                    current[0] =0;
                }
            }
        });
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current[0] >min)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(current[0] -1)));
                    current[0]--;
                }
                else if (current[0] ==min)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(max)));
                    current[0]=max;
                }
            }
        });
        crop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}