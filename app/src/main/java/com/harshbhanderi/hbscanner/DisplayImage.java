package com.harshbhanderi.hbscanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DisplayImage extends AppCompatActivity {

    private RecyclerView rv;
    private List<String> images_uri;
    private List<String> image_name;
    private RAdapter adapter;
    private Button create;
    String YouEditTextValue;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        rv = findViewById(R.id.recyclerView);
        create = findViewById(R.id.create);
        Intent i = getIntent();
        int number = i.getIntExtra(MainActivity.image_count, 0);
        String[] list;
        list = i.getStringArrayExtra(MainActivity.list);
        images_uri = new ArrayList<>();
        image_name = new ArrayList<>();
        for (int j = 0; j < number; j++) {
            images_uri.add(list[j]);
            image_name.add("Image" + (j + 1));
        }


        adapter = new RAdapter(DisplayImage.this, images_uri, image_name);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DisplayImage.this, 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(false);
        rv.setAdapter(adapter);

        create.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                   AlertDialog.Builder alert = new AlertDialog.Builder(DisplayImage.this);
                   final EditText edittext = new EditText(getApplicationContext());
                   alert.setMessage("Enter Pdf name Here:");
                   alert.setTitle("Enter Name");

                   alert.setView(edittext);

                   alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {
                           YouEditTextValue = edittext.getText().toString();
                           Toast.makeText(getApplicationContext(), YouEditTextValue + ".pdf is created successfully", Toast.LENGTH_SHORT).show();
                           try {
                               createpdf();
                           } catch (FileNotFoundException e) {
                               e.printStackTrace();
                           }
                       }
                   });
                   alert.show();
           }
       });
    }

    public void createpdf() throws FileNotFoundException {
        String pdfpath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath,YouEditTextValue + ".pdf");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.mkdir();

        }
        PdfWriter writer=new PdfWriter(file);
        PdfDocument pdfDocument=new PdfDocument(writer);
        Document document=new Document(pdfDocument);

        for (int k=0;k<images_uri.size();k++) {
            Bitmap bitmap = uriToBitmap(Uri.parse(images_uri.get(k)));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bitmapdata = byteArrayOutputStream.toByteArray();

            ImageData imageData = ImageDataFactory.create(bitmapdata);
            Image image = new Image(imageData);

            document.add(image);
        }
        document.close();
        //Toast.makeText(getApplicationContext(), "Pdf is created successfully", Toast.LENGTH_SHORT).show();
    }


    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}