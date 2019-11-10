package ir.moovic.entertainment.app.newspaper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.app.newspaper.activities.NewsPageActivity;
import ir.moovic.entertainment.app.newspaper.model.NewsModel;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import ir.moovic.entertainment.utils.recyclerview.RtlGridLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class FrNewsRelated extends MyBaseFragment {
    @BindView(R.id.rv)          public RecyclerView rv;
    @BindView(R.id.main_view)   public View mainView;

    private ItemAdapter adapter;

    public static FrNewsRelated newInstance(long id) {
        FrNewsRelated fr = new FrNewsRelated();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fr.setArguments(args);
        return fr;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_news_related, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
//        rv.setHasFixedSize(true);
        rv.setLayoutManager(new RtlGridLayoutManager(getContext(), 2));
        rv.setNestedScrollingEnabled(false);
        int space = getResources().getDimensionPixelSize(R.dimen.space_2);
        SpacesItemDecoration spaceDecor = new SpacesItemDecoration(space, space, space, space);
        rv.addItemDecoration(spaceDecor);
        rv.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        retrofitManager.disableLoading();
        Bundle args = getArguments();
        if(args != null) {
            long id = args.getLong("id", 0);
            enqueue(App.getNewsApiService().related(id), new Callback1(this));
        }
    }

    @Override
    public void loading(boolean show) {

    }

    @Override
    public void error(String message) {
        super.error(message);
        mainView.setVisibility(View.GONE);
    }


    private static class Callback1 extends RetrofitCallback<List<NewsModel>> {

        private WeakReference<FrNewsRelated> frRef;

        public Callback1(FrNewsRelated fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
            FrNewsRelated fr = frRef.get();
            if(fr != null) {
                fr.adapter.setList(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }

    private static class ItemAdapter extends ItemAdapter2<NewsModel> {
        private WeakReference<FrNewsRelated> frRef;

        public ItemAdapter(FrNewsRelated fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.list_item_1;
        }

        @Override
        public void onclick(View v, int position) {
            FrNewsRelated fr = frRef.get();
            if(fr == null) return;
            NewsModel model = getItem(position);
            fr.startActivity(new Intent(fr.getContext(), NewsPageActivity.class).putExtra("id", model.getId()));
        }

    }


}
