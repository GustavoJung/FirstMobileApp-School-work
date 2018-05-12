package com.gustavojung.pin1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {
    private String[] idImages;

   public HorizontalAdapter(String[]itens){
       this.idImages = itens;
   }

    @NonNull
    @Override
    public HorizontalAdapter.HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout,parent,false);
        return new HorizontalViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalAdapter.HorizontalViewHolder holder, int position) {
    holder.imgView.setImageResource(Integer.parseInt(idImages[position]));
    }

    @Override
    public int getItemCount() {
        return idImages.length;
    }


    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.series);
        }
    }
}