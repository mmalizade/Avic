package ir.moovic.entertainment.app.config;

import androidx.multidex.MultiDexApplication;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.ad.TapsellHelper;
import ir.moovic.entertainment.app.academy.network.AcademyApiClient;
import ir.moovic.entertainment.app.academy.network.AcademyApiService;
import ir.moovic.entertainment.app.cartoon.network.CartoonApiClient;
import ir.moovic.entertainment.app.cartoon.network.CartoonApiService;
import ir.moovic.entertainment.app.football.network.FootballApiClient;
import ir.moovic.entertainment.app.football.network.FootballApiService;
import ir.moovic.entertainment.app.main.network.ApiClient;
import ir.moovic.entertainment.app.main.network.ApiService;
import ir.moovic.entertainment.app.newspaper.network.NewsApiClient;
import ir.moovic.entertainment.app.newspaper.network.NewsApiService;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends MultiDexApplication {

    private static ApiService apiService;
    private static NewsApiService newsApiService;
    private static FootballApiService footballApiService;
    private static CartoonApiService cartoonApiService;
    private static AcademyApiService academyApiService;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.default_font))
                .setFontAttrId(R.attr.fontPath)
                .build());

        apiService = ApiClient.client(this).create(ApiService.class);
        newsApiService = NewsApiClient.client(this).create(NewsApiService.class);
        footballApiService = FootballApiClient.client(this).create(FootballApiService.class);
        cartoonApiService = CartoonApiClient.client(this).create(CartoonApiService.class);
        academyApiService = AcademyApiClient.client(this).create(AcademyApiService.class);
        TapsellHelper.init(this);

    }

    public static ApiService getApiService() {
        return apiService;
    }

    public static NewsApiService getNewsApiService() {
        return newsApiService;
    }

    public static FootballApiService getFootballApiService() {
        return footballApiService;
    }

    public static CartoonApiService getCartoonApiService() {
        return cartoonApiService;
    }

    public static AcademyApiService getAcademyApiService(){
        return academyApiService;
    }

}
