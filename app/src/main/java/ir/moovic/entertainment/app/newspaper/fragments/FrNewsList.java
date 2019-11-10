package ir.moovic.entertainment.app.newspaper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.ad.AdHeaderParams;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.app.newspaper.activities.NewsPageActivity;
import ir.moovic.entertainment.app.newspaper.model.NewsModel;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.ui.adapter.ItemAdapterWithNatvieBanner;
import ir.moovic.entertainment.ui.helper.EndlessRecyclerOnScrollListener;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;


public class FrNewsList extends MyBaseFragment {

    public static final String QUERY_PREFIX = "q_";

    @BindView(R.id.rv)      public RecyclerView rv;

    private ItemAdapter adapter;
    private HashMap<String,String> query = new HashMap<>();
    private int page = 0;

    public static FrNewsList newInstance(HashMap<String,String> queryParams) {
        FrNewsList fr = new FrNewsList();
        Bundle args = new Bundle();
        if(queryParams !=null && !queryParams.isEmpty()) {
            Set<String> keys = queryParams.keySet();
            for (String key:keys) {
                args.putString(key, queryParams.get(key));
            }
        }
        fr.setArguments(args);
        return fr;
    }

    public static FrNewsList newInstance(Bundle args) {
        FrNewsList fr = new FrNewsList();
        fr.setArguments(args);
        return fr;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(this);
        initQuery();
    }

    private void initQuery(){
        this.query = new HashMap<>();
        Bundle args = getArguments();
        if(args != null) {
            for (String key: args.keySet()) {
                try {
                    if(key.startsWith(QUERY_PREFIX)) {
                        this.query.put(
                                key.replaceFirst(QUERY_PREFIX, ""),
                                args.getString(key, "")
                        );
                    }

                } catch (Exception e){ e.printStackTrace(); }
            }
        }
        setPage(1);
    }

    private void setPage(int page) {
        this.page = (page > 0) ? page: 1;
        if(this.query != null) {
            this.query.put("page", String.valueOf(this.page));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        int space = getResources().getDimensionPixelSize(R.dimen.space_2);
        SpacesItemDecoration spaceDecor = new SpacesItemDecoration(space, space, space, space);
        rv.addItemDecoration(spaceDecor);
        rv.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                loadData(currentPage + 1);
            }
        });
        rv.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        loadData(1);
    }

    private void loadData(int page) {
        this.setPage(page);
        enqueue(App.getNewsApiService().newslist(query), new Callback1(this));
    }

    private static class Callback1 extends RetrofitCallback<List<NewsModel>> {

        private WeakReference<FrNewsList> frRef;

        public Callback1(FrNewsList fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
            FrNewsList fr = frRef.get();
            if(fr != null) {
                AdHeaderParams adParams = getAdHeaderParams();
                if(adParams != null && fr.adapter.getRealItemCount() == 0) {
                    fr.adapter.setAdParams(adParams.en, adParams.firstPos, adParams.repeater, adParams.bannerMode);
                }
                fr.adapter.addItems(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }

    }

    private static class ItemAdapter extends ItemAdapterWithNatvieBanner<NewsModel> {

        private WeakReference<FrNewsList> frRef;

        public ItemAdapter(FrNewsList fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.news_list_item;
        }

        @Override
        public void onclick(View v, int position) {
            FrNewsList fr = frRef.get();
            if(fr == null) return;
            NewsModel model = getItem(position);
            fr.startActivity(new Intent(fr.getContext(), NewsPageActivity.class).putExtra("id", model.getId()));
        }

        @Override
        public int getNativeBannerLayoutId() {
            int mode = getBannerMode();
            if(mode == 0) {
                mode = new Random().nextBoolean() ? 1: 2;
            }

            switch (mode) {
                case 2:
                    return R.layout.native_banner_2;
                default:
                case 1:
                    return R.layout.native_banner_1;
            }
        }
    }

}
