package ir.moovic.entertainment.network;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;

import ir.moovic.entertainment.ad.AdHeaderParams;
import ir.moovic.entertainment.app.config.base.RetrofitManager;
import ir.moovic.entertainment.utils.Utils;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallback<T> implements Callback<T> {
    private static final boolean DEBUG = false;

    private AdHeaderParams adHeaderParams;

    private WeakReference<RetrofitManager> retrofitManagerRef;

    public void setRetrofitManager(RetrofitManager retrofitManager) {
        this.retrofitManagerRef = new WeakReference<>(retrofitManager);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        boolean precheck = preCheck(call, response, null);
        if(!precheck || call.isCanceled()) return;
        if(response.isSuccessful() && response.body() != null){
            success(call, response);
        } else {
            String message = errorBody(response.errorBody());
            if(response.code() == 401){
                unAuthorized(message);
            } else if(response.code() == 402){
                paymentRequired(message);
            } else {
                failed(message);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("Retrofit Failure", t.toString());
        boolean precheck = preCheck(call, null, t);
        if(precheck && !call.isCanceled()) {
            failed("");
        }
    }

    protected boolean preCheck(Call<T> call, Response<T> response, Throwable t) {
        checkHeaders(response);
        if(retrofitManagerRef == null) return true;
        RetrofitManager manager = retrofitManagerRef.get();
        if(manager == null) return (response != null);
        if(manager.canTouchUi()) {
            manager.removeCall(call);
            manager.loading(false);
            return (response != null);
        } else {
            if(call.isCanceled()) {
                manager.cancelAllRequests();
                return false;
            } else {
                call.cancel();
            }
        }
        return response != null;
    }



    public String errorBody(ResponseBody errorBody) {
        try {
            String str = errorBody.string();
            if(TextUtils.isEmpty(str)) return "";
            ErrorJson error = new Gson().fromJson(str, ErrorJson.class);
            if(error == null) {
                return str;
            } else {
                return DEBUG ? error.errorMessage : (TextUtils.isEmpty(error.message) ? error.statusMessage : error.message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void unAuthorized(String message){
        if(TextUtils.isEmpty(message)){
            message = "UnAuthorized";
        }
        failed(message);
    }

    public void paymentRequired(String message) {
        if(TextUtils.isEmpty(message)){
            message = "Payment Required";
        }
        failed(message);
    }


    public abstract void success(Call<T> call, Response<T> response);
    protected void failed(String message) {
        if(TextUtils.isEmpty(message)) {
            message = "";
        }
        if(retrofitManagerRef != null) {
            RetrofitManager manager = retrofitManagerRef.get();
            if(manager != null) {
                manager.error(message);
            }
        }
    }

    public AdHeaderParams getAdHeaderParams() {
        return adHeaderParams;
    }

    protected void checkHeaders(Response<T> response) {
        if(adHeaderParams == null) adHeaderParams = new AdHeaderParams();
        if(response == null) return;
        String val = "";
        try {
            Headers headers = response.headers();
            adHeaderParams.en = "1".equalsIgnoreCase(headers.get("x-ad-en"));
            val = headers.get("x-ad-firstpos");
            if(Utils.isInteger(val)) {
                adHeaderParams.firstPos = Integer.valueOf(val);
            }
            val = headers.get("x-ad-repeater");
            if(Utils.isInteger(val)) {
                adHeaderParams.repeater = Integer.valueOf(val);
            }
            val = headers.get("x-ad-bannermode");
            if(Utils.isInteger(val)) {
                adHeaderParams.bannerMode = Integer.valueOf(val);
            }
        } catch (Exception e){}
    }
}