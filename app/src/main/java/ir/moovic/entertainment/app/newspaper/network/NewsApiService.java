package ir.moovic.entertainment.app.newspaper.network;

import java.util.HashMap;
import java.util.List;

import ir.moovic.entertainment.app.newspaper.model.NewsCategoryModel;
import ir.moovic.entertainment.app.newspaper.model.NewsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface NewsApiService {

    @GET("category")
    Call<List<NewsCategoryModel>> categories();

    @GET("newslist")
    Call<List<NewsModel>> newslist(@QueryMap HashMap<String,String> queryMap);

    @GET("news/{id}")
    Call<NewsModel> news(@Path("id") long id);

    @GET("news/{id}/related")
    Call<List<NewsModel>> related(@Path("id") long id);

}
