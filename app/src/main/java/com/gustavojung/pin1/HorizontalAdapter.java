package com.gustavojung.pin1;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HorizontalAdapter extends  RecyclerView.Adapter<HorizontalAdapter.ViewHolder>{

    private final static String TAG = "RecyclerViewAdapter";

    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context mContext;

    public HorizontalAdapter(Context context, ArrayList<Integer> imageUrls) {

            mImages = imageUrls;
            mContext = context;
             }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
           ViewHolder vh = new ViewHolder(view);
           return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext).asBitmap().load(mImages.get(position)).into(holder.imgView);


        holder.imgView.setImageResource(mImages.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on" );
                Toast.makeText(mContext,"Clicado", Toast.LENGTH_SHORT).show();
                v = new View(mContext);
                v.setBackground(holder.imgView.getDrawable());
                Dialog dialog;
                dialog= new Dialog(mContext);
                dialog.setContentView(R.layout.content_clicked);
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private ImageView imgView;
        public ViewHolder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.series);
            layout = itemView.findViewById(R.id.layoutItem);
        }
    }
}