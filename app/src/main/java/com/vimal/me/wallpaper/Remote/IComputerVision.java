package com.vimal.me.wallpaper.Remote;

import com.vimal.me.wallpaper.Model.AnalyzeModel.ComputerVision;
import com.vimal.me.wallpaper.Model.AnalyzeModel.URLUpload;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IComputerVision {

    @Headers({
            "Content-Type:application/json",
            "0cp-Apim-Subscription-key:6a4316875fd6410a9e3cbe4b2ecd5deb"
    })
    @POST
    Call<ComputerVision>analyzeImage(@Url String apiEndpoint, @Body URLUpload url);


}
