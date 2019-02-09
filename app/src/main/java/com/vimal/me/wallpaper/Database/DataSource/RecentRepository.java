package com.vimal.me.wallpaper.Database.DataSource;

import com.vimal.me.wallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

public class RecentRepository implements IRecentsDataSource {

    private IRecentsDataSource mLocalDatasource;
    private static RecentRepository instance;

    public RecentRepository(IRecentsDataSource mLocalDatasource) {
        this.mLocalDatasource = mLocalDatasource;
    }

    public static RecentRepository getInstance(IRecentsDataSource mLocalDatasource) {
       if(instance==null)
           instance = new RecentRepository(mLocalDatasource);
       return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return mLocalDatasource.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        mLocalDatasource.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
      mLocalDatasource.updateRecents(recents);
    }

    @Override
    public void deleteRecents(Recents... recents) {
    mLocalDatasource.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
     mLocalDatasource.deleteAllRecents();
    }
}
