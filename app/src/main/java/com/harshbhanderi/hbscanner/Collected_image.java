package com.harshbhanderi.hbscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Collected_image extends AppCompatActivity {

    private List<String> images_uri;
    private List<String> image_name;
    String[] list;
    private ImageView collected_images;
    private Button crop_btn,bright_btn,light_btn,magic_btn,black_btn,next_btn,prev_btn,done_btn,cancel_btn;
    private CropView cropView;
    private int current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_image);

        getSupportActionBar().hide();

        //Buttons in Activity
        crop_btn=findViewById(R.id.crop);
        light_btn=findViewById(R.id.light);
        magic_btn=findViewById(R.id.magic);
        black_btn=findViewById(R.id.black);
        next_btn=findViewById(R.id.next);
        prev_btn=findViewById(R.id.prev);
        done_btn=findViewById(R.id.crop_done);
        cancel_btn=findViewById(R.id.crop_cancel);
        cropView = (CropView) findViewById(R.id.cropView);

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
        current=0;
        int min=0;
        int max=images_uri.size()-1;
        collected_images.setImageURI(Uri.parse(images_uri.get(current)));

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current <max)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(current +1)));
                    current++;
                }
                else if (current==max)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(0)));
                    current =0;
                }
            }
        });
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current >min)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(current -1)));
                    current--;
                }
                else if (current ==min)
                {
                    collected_images.setImageURI(Uri.parse(images_uri.get(max)));
                    current=max;
                }
            }
        });
        crop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropView.of(Uri.parse(images_uri.get(current)))
                        .withAspect(2, 2)
                        .withOutputSize(1000,1000)
                        .initialize(Collected_image.this);

                next_btn.setVisibility(View.GONE);
                prev_btn.setVisibility(View.GONE);
                done_btn.setVisibility(View.VISIBLE);
                cancel_btn.setVisibility(View.VISIBLE);

            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap croppedBitmap = cropView.getOutput();
                grantUriPermission("com.harshbhanderi.hbscanner", getImageUri(Collected_image.this,croppedBitmap), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                CropUtil.saveOutput(Collected_image.this, Uri.parse(images_uri.get(current)),croppedBitmap, 100);
                grantUriPermission("com.harshbhanderi.hbscanner", getImageUri(Collected_image.this,croppedBitmap), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            }
        });

//        light_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bitmap bitmap=uriToBitmap(Uri.parse(images_uri.get(current)));
//                Bitmap filterbit = convertToMutable(bitmap);
//                Filter myFilter = new Filter();
//                myFilter.addSubFilter(new BrightnessSubFilter(30));
//                myFilter.addSubFilter(new ContrastSubFilter(1.1f));
//                Bitmap outputImage = myFilter.processFilter(filterbit);
//                image_name.set(current,getImageUri(getApplicationContext(),outputImage).toString());
//            }
//        });

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height =imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgIn;
    }
}