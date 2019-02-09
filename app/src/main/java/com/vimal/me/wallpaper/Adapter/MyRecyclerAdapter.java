package com.vimal.me.wallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.vimal.me.wallpaper.Common.Common;
import com.vimal.me.wallpaper.Database.Recents;
import com.vimal.me.wallpaper.Interface.ItemClickListener;
import com.vimal.me.wallpaper.ListWallpaper;
import com.vimal.me.wallpaper.Model.WallpaperItem;
import com.vimal.me.wallpaper.R;
import com.vimal.me.wallpaper.ViewHolder.ListWallpaperViewHolder;
import com.vimal.me.wallpaper.ViewWallapaper;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {

    private Context context;
    private List<Recents> recents;

    public MyRecyclerAdapter(Context context, List<Recents> recents) {
        this.context = context;
        this.recents = recents;
    }

    @Override
    public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wallpaper_item, parent, false);
        int height = parent.getMeasuredHeight() / 2;
        itemView.setMinimumHeight(height);
        return new ListWallpaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListWallpaperViewHolder holder, final int position) {
        Picasso.get()
                .load(recents.get(position).getImageLink())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.wallpaper, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception ex) {
                        Picasso.get()
                                .load(recents.get(position).getImageLink())
                                .error(R.drawable.ic_terrain_black)
                                .into(holder.wallpaper, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception ex) {
                                        Log.e("error ", "Could not fetch image");
                                    }
                                });

                    }


                });

        holder.setItemClickListener(new ItemClickListener() {


            @Override
            public void onclick(View view, int position) {
                Intent intent = new Intent(context,ViewWallapaper.class);
                WallpaperItem wallpaperItem = new WallpaperItem();
                WallpaperItem .setCategoryId(recents.get(position).getCategoryId());
                WallpaperItem.setImageLink(recents.get(position).getImageLink());
                Common.select_background=wallpaperItem;
                Common.select_background_key =recents.get(position).getKey();
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return recents.size();
    }
}
