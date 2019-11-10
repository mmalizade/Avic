package ir.moovic.entertainment.app.football.network;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import ir.moovic.entertainment.app.football.config.Config;
import ir.moovic.entertainment.network.BaseInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FootballApiClient {

    private static Retrofit client;

    public static Retrofit client(Context context) {
        if(client == null) {
            Gson gson = new GsonBuilder()
                    .setLenient().create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new BaseInterceptor(context))
                    .build();

            client = new Retrofit.Builder()
                    .baseUrl(Config.API_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return client;
    }


    public static Retrofit UploadFileClient(){
        Gson gson = new GsonBuilder()
                .setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new okhttp3.Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .header("Content-Type", "multipart/form-data")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}
