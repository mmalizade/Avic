package ir.moovic.entertainment.app.academy.network;


import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;

import ir.moovic.entertainment.app.academy.model.CourseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

@SuppressLint("UnknownNullness")
public interface AcademyApiService {

    @GET("course/{id}")
    Call<CourseModel> course(@Path("id") long id);

    @GET("courseList")
    Call<List<CourseModel>> courseList(@QueryMap HashMap<String, String> query);

}
