package com.harshbhanderi.hbscanner;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.oginotihiro.cropview.CropView;

import java.util.List;

public class RAdapter extends RecyclerView.Adapter<RAdapter.MyViewHolder> {

    private Context context;
    private List<String> image_uri;
    private List<String> image_name;

    public RAdapter(Context context, List<String> image_uri, List<String> image_name) {
        this.context = context;
        this.image_name = image_name;
        this.image_uri = image_uri;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextview.setText(image_name.get(position));
        holder.mImageview.setImageURI(Uri.parse(image_uri.get(position)));

    }


    @Override
    public int getItemCount() {
        return image_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageview;
        TextView mTextview;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageview=itemView.findViewById(R.id.cardImage);
            mTextview=itemView.findViewById(R.id.card_text);


        }
    }
}