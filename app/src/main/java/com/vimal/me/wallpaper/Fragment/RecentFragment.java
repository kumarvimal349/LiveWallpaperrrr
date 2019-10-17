package com.vimal.me.wallpaper.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vimal.me.wallpaper.Adapter.MyRecyclerAdapter;
import com.vimal.me.wallpaper.Database.DataSource.RecentRepository;
import com.vimal.me.wallpaper.Database.LocalDatabase.LocalDatabase;
import com.vimal.me.wallpaper.Database.LocalDatabase.RecentDataSource;
import com.vimal.me.wallpaper.Database.Recents;
import com.vimal.me.wallpaper.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@SuppressLint("ValidFragment")
public class RecentFragment extends Fragment {

    private  static RecentFragment  INSTANCE=null;

    RecyclerView recyclerView;

    List<Recents> recentsList;
    MyRecyclerAdapter adapter;
    Context context;


    //room database
    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;


    public RecentFragment(Context context) {
        // Required empty public constructor
        this.context= context;

        //init RoomDatabase
        compositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(context);
        recentRepository = RecentRepository.getInstance(RecentDataSource.getInstance(database.recentsDAO()));
    }



    public static RecentFragment getInstance(Context context)
    {
        if(INSTANCE==null)
            INSTANCE=new RecentFragment(context);
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_recent);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recentsList = new ArrayList<>();
        adapter = new MyRecyclerAdapter(context,recentsList);
        recyclerView.setAdapter(adapter);


        loadRecents();

        return view;
    }

    private void loadRecents() {

        Disposable disposable = recentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {
                        onGetAllRecentsSucess(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR",throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void onGetAllRecentsSucess(List<Recents>recents)
    {
        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}