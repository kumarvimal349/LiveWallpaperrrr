package com.vimal.me.wallpaper.Database.LocalDatabase;

import com.vimal.me.wallpaper.Database.DataSource.IRecentsDataSource;
import com.vimal.me.wallpaper.Database.Recents;


import java.util.List;

import io.reactivex.Flowable;

public class RecentDataSource  implements IRecentsDataSource {

    private RecentsDAO recentsDAO;
    private static RecentDataSource instance;


    public  RecentDataSource(RecentsDAO recentsDAO)
    {
        this.recentsDAO=recentsDAO;
    }

    public static RecentDataSource getInstance(RecentsDAO recentsDAO)
    {
        if(instance== null)
            instance = new RecentDataSource(recentsDAO);
        return instance;


    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return recentsDAO.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
     recentsDAO.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
    recentsDAO.updateRecents(recents);
    }

    @Override
    public void deleteRecents(Recents... recents) {
  recentsDAO.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
     recentsDAO.deleteAllRecents();
    }
}
