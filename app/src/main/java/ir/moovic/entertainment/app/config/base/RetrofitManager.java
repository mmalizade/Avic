package ir.moovic.entertainment.app.config.base;


import android.annotation.SuppressLint;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ir.moovic.entertainment.network.RetrofitCallback;
import retrofit2.Call;

@SuppressLint("UnknownNullness")
public class RetrofitManager {
    private boolean enableCallLoading = true;

    private WeakReference<IRetroComponent> componentRef;

    private List<Call> calls;

    public RetrofitManager(IRetroComponent component) {
        calls = new ArrayList<>();
        this.componentRef = new WeakReference<>(component);
    }

    public <T> void enqueue(Call<T> call, RetrofitCallback<T> callback) {
        if(call == null || callback == null) return;
        loading(true);
        addCall(call);
        call.enqueue(callback);
    }

    public synchronized void loading(boolean show) {
        IRetroComponent host = getComponent();
        if(host != null && enableCallLoading) {
            host.loading(show);
        }
    }

    public synchronized void error(String message) {
        IRetroComponent host = getComponent();
        if(host != null) {
            host.error(message);
        }
    }

    public synchronized RetrofitManager enableLoading() {
        this.enableCallLoading = true;
        return this;
    }

    public synchronized RetrofitManager disableLoading() {
        this.enableCallLoading = false;
        return this;
    }

    public synchronized void addCall(Call call) {
            if(calls == null) {
                calls = new ArrayList<>();
            }
            calls.add(call);
    }

    public synchronized void removeCall(Call call) {
            if(calls != null) {
                try {
                    calls.remove(call);
                } catch (Exception e){}
            }
    }

    public synchronized void cancelAllRequests() {
            if(calls != null && !calls.isEmpty()){
                for(Call call : calls){
                    if( !call.isCanceled() ){
                        try {
                            call.cancel();
                        } catch (Exception e){}
                    }
                }
                calls.clear();
            }
    }

    private synchronized IRetroComponent getComponent() {
        if(componentRef == null) return null;
        return componentRef.get();
    }

    public synchronized boolean canTouchUi() {
        return getComponent() != null;
    }

}
