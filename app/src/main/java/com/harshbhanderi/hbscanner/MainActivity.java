package com.harshbhanderi.hbscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CardView ScanCard, PdfCard;
    public final static String list = "com.harshbhanderi.hbscanner.URILIST";
    public final static String image_count = "com.harshbhanderi.hbscanner.COUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //Dialog to show advice to user
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isFinishing()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("User Advice")
                            .setMessage("It is Advised To Our Dear User That Please Capture All Images First By Your Mobile Camera Or Make It Available In Your Phone Storage, So That You Can Easily Retrieve It.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                }
                            }).show();
                }
            }
        });

        //Cards
        ScanCard = findViewById(R.id.scan_card);
        PdfCard = findViewById(R.id.pdf_card);
        getSupportActionBar().hide();

        //Permission Manager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }


        //OnClickListener for Taking images from storage by giving option in AlertBox 1.Select from Storage 2.Exit
        ScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] optionsMenu = {"Choose from Gallery", "Exit"}; // create a menuOption Array
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (optionsMenu[i].equals("Choose from Gallery")) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
                        } else if (optionsMenu[i].equals("Exit")) {
                            dialogInterface.dismiss();
                        }
                    }

                });
                builder.show();
            }
        });
    }


    //ActivityResult To Capture Images that are selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this, DisplayImage.class);
        if (resultCode != RESULT_CANCELED) {
            if (data != null && requestCode == 1) {
                ClipData cd;
                cd = data.getClipData();
                int count=cd.getItemCount();
                String[] uriList=new String[count];
                for (int i = 0; i <count; i++) {
                    Uri imageUri = cd.getItemAt(i).getUri();
                    uriList[i]=imageUri.toString();
                    Log.d("URI", imageUri.toString());
                }

                intent.putExtra(list,uriList);
                intent.putExtra(image_count,count);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                Log.d("count", String.valueOf(count));
            }
            }
        }

    }

