package ir.moovic.entertainment.controller;


import android.text.TextUtils;

public class AppConfig {


    public static final String BASE_URL = "http://avic.moovic.ir/";
    public static final String MEDIA_BASE_URL = BASE_URL;

    public static final String API_BASE_URL = BASE_URL + "api/";
    public static final String NEWS_API_BASE_URL = API_BASE_URL + "newsapp/";
    public static final String FOOTBALL_API_BASE_URL = API_BASE_URL + "football/";
    public static final String CARTOON_API_BASE_URL = API_BASE_URL + "cartoon/";
    public static final String ACADEMY_API_BASE_URL = API_BASE_URL + "academy/";


    public static String mediaUrl(String url) {
        if(!TextUtils.isEmpty(url)){
            if(url.startsWith("http")){
                return url;
            } else {
                return MEDIA_BASE_URL + url;
            }
        }
        return "";
    }

    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}
