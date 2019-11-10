package ir.moovic.entertainment.app.config.base;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ir.moovic.entertainment.network.RetrofitCallback;
import retrofit2.Call;

public  class MyBaseFragment extends Fragment implements IFrBackPressed, IRetroComponent {

    protected RetrofitManager retrofitManager;
    public IPageController IPageController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitManager = new RetrofitManager(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(retrofitManager != null) {
            retrofitManager.cancelAllRequests();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if(parentFragment instanceof MyBaseFragment) {
            IPageController = ((MyBaseFragment) parentFragment).IPageController;
        } else if(context instanceof IPageController){
            IPageController = (IPageController) context;
        } else {
            IPageController = null;
        }
    }

    public <T> void enqueue(Call<T> call, RetrofitCallback<T> callback) {
        if(retrofitManager == null) {
            retrofitManager = new RetrofitManager(this);
        }
        callback.setRetrofitManager(retrofitManager);
        retrofitManager.enqueue(call, callback);
    }

    @Override
    public boolean onBackPressed() {
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        for(Fragment f : fragmentList) {
            if(f instanceof IFrBackPressed) {
                boolean handled = ((IFrBackPressed)f).onBackPressed();
                if(handled) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void loading(boolean show) {

    }

    @Override
    public void error(String message) {

    }
}
