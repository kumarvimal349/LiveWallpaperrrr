package com.vimal.me.wallpaper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.vimal.me.wallpaper.Interface.ItemClickListener;
import com.vimal.me.wallpaper.R;



public   class ListWallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    ItemClickListener itemClickListener;

    public ImageView wallpaper;

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }


    public ListWallpaperViewHolder(View itemView) {
        super(itemView);
        wallpaper =(ImageView)itemView.findViewById(R.id.image);
        itemView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        itemClickListener.onclick(v,getAdapterPosition());
    }
}
