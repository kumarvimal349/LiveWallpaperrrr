package com.vimal.me.wallpaper.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimal.me.wallpaper.Interface.ItemClickListener;
import com.vimal.me.wallpaper.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView category_name;
    public ImageView background_image;


    ItemClickListener itemClickListener;


    public CategoryViewHolder(View itemView)
    {
        super(itemView);
        background_image = (ImageView)itemView.findViewById(R.id.image);
        category_name=(TextView)itemView.findViewById(R.id.name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onclick(v,getAdapterPosition());


    }


    public void setItemClicklistener(ItemClickListener itemClickListener) {
        this.itemClickListener=itemClickListener;
    }
}

