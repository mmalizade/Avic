package ir.moovic.entertainment.network;

import android.content.Context;

import java.io.IOException;

import ir.moovic.entertainment.utils.Utils;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements okhttp3.Interceptor {
    private String appVersionName = "1.0";
    private String appVersionCode = "1";

    public BaseInterceptor(Context context) {
        appVersionCode = String.valueOf(Utils.getAppVersionCode(context));
        appVersionName = Utils.getAppVersionName(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .header("Content-Type", "application/json")
                .header("CacheControl", "no-cache")
                .header("App-Version-Name", appVersionName)
                .header("App-Version-Code", appVersionCode)
                .header("access-token", accessToken())
                .build();
        return chain.proceed(request);
    }

    public String accessToken(){
        return "123456";
    }

}