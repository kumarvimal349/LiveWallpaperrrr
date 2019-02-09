package com.vimal.me.wallpaper.Model;

public class WallpaperItem {

    public String imageLink;
    public String categoryId;
    public long viewCount;

    public WallpaperItem(){

    }

    public WallpaperItem(String imageLink, String categoryId) {
        this.imageLink = imageLink;
        this.categoryId = categoryId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public static void setImageLink(String imageLink) {
       imageLink = imageLink;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public static void setCategoryId(String categoryId) {
        categoryId = categoryId;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
