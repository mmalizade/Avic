package ir.moovic.entertainment.app.newspaper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.ad.TapsellHelper;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.newspaper.fragments.FrNewsList;
import ir.moovic.entertainment.app.newspaper.fragments.FrNewsRelated;
import ir.moovic.entertainment.app.newspaper.model.NewsModel;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.ui.adapter.TagAdapter1;
import ir.moovic.entertainment.utils.recyclerview.RtlHorizontalLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import ir.tapsell.sdk.bannerads.TapsellBannerViewEventListener;
import retrofit2.Call;
import retrofit2.Response;

public class NewsPageActivity extends MyBaseActivity {
    @BindView(R.id.rv_tags)                 public RecyclerView rvTags;

    @Nullable @BindView(R.id.main_view)     public View mainView;
    @Nullable @BindView(R.id.loading_pb)    public View loadingProgressView;
    @Nullable @BindView(R.id.tapsell_1)     public TapsellBannerView tapsellBanner1;

    private NewsModel model;
    private ViewDataBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_page);
        ButterKnife.bind(this);
        TagAdapter tagAdapter = new TagAdapter(this);
        rvTags.setLayoutManager(new RtlHorizontalLayoutManager(this));
        int space = getResources().getDimensionPixelSize(R.dimen.space_1);
        rvTags.addItemDecoration(new SpacesItemDecoration(space, SpacesItemDecoration.RTL_HORIZONTAL));
        rvTags.setAdapter(tagAdapter);
        loadData();
        ad();
    }

    private void ad() {
        if(tapsellBanner1 != null) {
            tapsellBanner1.setEventListener(new TapsellBannerViewEventListener() {
                @Override
                public void onNoAdAvailable() {
                    tapsellBanner1.setVisibility(View.GONE);
                }

                @Override
                public void onNoNetwork() {

                }

                @Override
                public void onError(String s) {

                }

                @Override
                public void onRequestFilled() {
                    if(mainView != null && mainView instanceof NestedScrollView) {
                        NestedScrollView nsv = (NestedScrollView) mainView;
                        nsv.scrollTo(0, 0);
                    }
                }

                @Override
                public void onHideBannerView() {
                }
            });
            tapsellBanner1.loadAd(this, TapsellHelper.Zone.TAPSELL_STANDARD_BANNER, TapsellHelper.NEWS_PAGE_BANNER_TYPE);
        }
    }

    private void loadData() {
        Bundle args = getIntent().getExtras();
        if(args != null) {
            long id = args.getLong("id", 0);
            enqueue(App.getNewsApiService().news(id), new NewsPageActivity.Callback1(this));
            FrNewsRelated frNewsRelated = FrNewsRelated.newInstance(id);
            animatedReplaceFragment(frNewsRelated, R.id.container_1, false);
        }
    }

    @Override
    public void loading(boolean show) {
        if(loadingProgressView != null) {
            loadingProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if(mainView != null) {
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void bind(NewsModel model){
        this.model = model;
        binding.setVariable(BR.obj, this.model);
        binding.executePendingBindings();
    }

    private static class Callback1 extends RetrofitCallback<NewsModel> {

        private WeakReference<NewsPageActivity> acitvityRef;

        public Callback1(NewsPageActivity acitvity) {
            this.acitvityRef = new WeakReference<>(acitvity);
        }

        @Override
        public void success(Call<NewsModel> call, Response<NewsModel> response) {
            NewsPageActivity acitvity = acitvityRef.get();
            if(acitvity != null) {
                acitvity.bind(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }

    private static class TagAdapter extends TagAdapter1 {
        private WeakReference<NewsPageActivity> acitvityRef;

        public TagAdapter(NewsPageActivity acitvity) {
            this.acitvityRef = new WeakReference<>(acitvity);
        }

        @Override
        public void onclick(View v, int position) {
            NewsPageActivity activity = acitvityRef.get();
            if(activity != null) {
                String model = getItem(position);
                if(!TextUtils.isEmpty(model)) {
                    Bundle args = new Bundle();
                    args.putString(FrNewsList.QUERY_PREFIX + "tag", model);
                    args.putString(MyBaseActivity.KEY_PAGE_TITLE, model);
                    activity.startActivity(new Intent(activity, NewsListActivity.class).putExtras(args));
                }
            }
        }
    }


}
