package com.vimal.me.wallpaper.Fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
//
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.vimal.me.wallpaper.Common.Common;
import com.vimal.me.wallpaper.Interface.ItemClickListener;
import com.vimal.me.wallpaper.ListWallpaper;
import com.vimal.me.wallpaper.Model.CategoryItem;
import com.vimal.me.wallpaper.R;
import com.vimal.me.wallpaper.ViewHolder.CategoryViewHolder;


public class CategoryFragment extends Fragment {




    //firebase
    FirebaseDatabase database;
    DatabaseReference categoryBackground;
    RecyclerView recyclerView;



    //Firebase adapter
    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem,CategoryViewHolder> adapter;

    private  static CategoryFragment  INSTANCE=null;




    public CategoryFragment() {
        database=FirebaseDatabase.getInstance();
        categoryBackground=database.getReference(Common.STR_CATEGORY_BACKGROUND);
        // Required empty public constructor


        options=new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(categoryBackground,CategoryItem.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {

            @Override
            protected void onBindViewHolder( final CategoryViewHolder holder, int position,  final CategoryItem model) {

                Picasso.get()
                        .load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.background_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception ex) {
                                Picasso.get()
                                        .load(model.getImageLink())
                                        .error(R.drawable.ic_terrain_black)
                                        .into(holder.background_image, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception ex) {
                                                Log.e("ERROR", "Couldn't fetch image");
                                            }
                                        });


                            }
                        });

                holder.category_name.setText(model.getName());
                holder.setItemClicklistener(new ItemClickListener() {

                    @Override
                    public void onclick(View view, int position) {
                        Common.CATEGORY_ID_SELECTED = adapter.getRef(position).getKey(); // get the key
                        Common.CATEGORY_SELECTED=model.getName();
                        Intent intent = new Intent(getActivity(),ListWallpaper.class);
                        startActivity(intent);

                    }
                });

            }


            @Override
            public CategoryViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_category_item,parent,false);

                return new CategoryViewHolder(itemview);
            }
        };

    }

    public static CategoryFragment getInstance()
    {
        if(INSTANCE==null)
            INSTANCE=new CategoryFragment();
        return INSTANCE;
    }



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_category);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        setCategory();

        return view;






    }





    private void setCategory() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

// ctrl o



    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }


    //ctrl + o


    @Override
    public void onStop() {
        if(adapter!=null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();

    }




}