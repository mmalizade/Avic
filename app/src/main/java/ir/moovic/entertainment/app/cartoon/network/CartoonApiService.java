package ir.moovic.entertainment.app.cartoon.network;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;

import ir.moovic.entertainment.app.cartoon.model.ProductModel;
import ir.moovic.entertainment.app.cartoon.model.ResBundle1;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


@SuppressLint("UnknownNullness")
public interface CartoonApiService {

    @GET("home")
    Call<List<ResBundle1>> home();

    @GET("product/{id}")
    Call<ProductModel> product(@Path("id") long id);

    @GET("product/{id}/related")
    Call<List<ProductModel>> relatedProducts(@Path("id") long id);

    @GET("productList")
    Call<List<ProductModel>> productList(@QueryMap HashMap<String, String> queryMap);

}
