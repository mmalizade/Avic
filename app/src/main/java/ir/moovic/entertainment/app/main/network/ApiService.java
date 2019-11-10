package ir.moovic.entertainment.app.main.network;

import ir.moovic.entertainment.app.main.models.ResHomePage;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {


    @GET("home")
    Call<ResHomePage> homePage();


}
