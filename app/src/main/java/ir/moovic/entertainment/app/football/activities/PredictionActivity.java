package ir.moovic.entertainment.app.football.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.GlideApp;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.football.fragments.FrMatchPrediction;
import ir.moovic.entertainment.app.football.model.MatchModel;
import ir.moovic.entertainment.controller.Config;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.ui.pager.CardScaleTransformer;
import retrofit2.Call;
import retrofit2.Response;

@SuppressLint("UnknownNullness")
public class PredictionActivity extends MyBaseActivity {

    @BindView(R.id.view_pager)          public ViewPager viewPager;
    @BindView(R.id.iv_appbar_image)     public AppCompatImageView ivAppbarImage;

    List<MatchModel> matches;
    PagerAdapter adapter;
    long firstMatchId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        setToolbarTextColor(Color.WHITE);
        matches = new ArrayList<>();
        adapter = new PagerAdapter(this);

        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.content_padding_1));
        int baseElevation = getResources().getDimensionPixelSize(R.dimen.prediction_card_base_elevation);
        int raisingElevation = getResources().getDimensionPixelSize(R.dimen.prediction_card_raising_elevation);
        float smallerScale = 0.75f;
        viewPager.setPageTransformer(false, new CardScaleTransformer(baseElevation, raisingElevation, smallerScale));
        init();
    }


    private void init() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String cover = extras.getString("cover", null);
            GlideApp.with(this).load(Config.mediaUrl(cover)).into(ivAppbarImage);
        }
        load();
    }

    private void load() {
        MatchModel match = getIntent().getParcelableExtra("match");
        firstMatchId = match.id;
        App.getFootballApiService().leaguePeriodMatches(match.leagueId, match.period).enqueue(new Callback1(this));
    }

    void selectPage() {
        if(matches == null || matches.isEmpty()) return;
        for(int pos = 0; pos < matches.size(); pos++) {
            if(firstMatchId == matches.get(pos).id){
                viewPager.setCurrentItem(pos, false);
                break;
            }
        }
    }

    @Override
    protected boolean isAppBarElavationEnabled() {
        return false;
    }

    private static class Callback1 extends RetrofitCallback<List<MatchModel>> {

        private WeakReference<PredictionActivity> ref;

        public Callback1(PredictionActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void success(Call<List<MatchModel>> call, Response<List<MatchModel>> response) {
            PredictionActivity activity = ref.get();
            if(activity != null) {
                activity.matches = response.body();
                activity.viewPager.setAdapter(activity.adapter);
                activity.selectPage();
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private WeakReference<PredictionActivity> activityRef;

        public PagerAdapter(PredictionActivity activity) {
            super(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.activityRef = new WeakReference<>(activity);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            PredictionActivity activity = activityRef.get();
            return FrMatchPrediction.newInstance(activity.matches.get(position));
        }

        @Override
        public int getCount() {
            try {
                return activityRef.get().matches.size();
            } catch (Exception e){
                return 0;
            }
        }
    }

}
