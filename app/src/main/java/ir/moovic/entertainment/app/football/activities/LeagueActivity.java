package ir.moovic.entertainment.app.football.activities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.GlideApp;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.football.fragments.FrLeagueMatches;
import ir.moovic.entertainment.app.football.fragments.FrLeagueStanding;
import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.controller.Config;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import ir.moovic.entertainment.utils.recyclerview.RtlHorizontalLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;

public class LeagueActivity extends MyBaseActivity {
    @BindView(R.id.rv_top_leagues)  public RecyclerView rvTopLeagues;
    @BindView(R.id.tab_layout)      public TabLayout tabLayout;
    @BindView(R.id.view_pager)      public ViewPager viewPager;
    @BindView(R.id.iv_appbar_image) public AppCompatImageView ivAppbarImage;

    private LeagueModel league;
    private long id;

    private TopLeagueAdapter adapter0;
    private FrPagerAdatper pagerAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);
        initViews();
    }

    private void initViews() {
        if(getIntent().hasExtra("id")) {
            id = getIntent().getLongExtra("id", 0);
        }
        adapter0 = new TopLeagueAdapter(this);
        rvTopLeagues.setLayoutManager(new RtlHorizontalLayoutManager(this));
        int space = getResources().getDimensionPixelSize(R.dimen.space_0);
        rvTopLeagues.addItemDecoration(new SpacesItemDecoration(space, SpacesItemDecoration.RTL_HORIZONTAL));
        rvTopLeagues.setAdapter(adapter0);
//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(rvTopLeagues);
        rvTopLeagues.setNestedScrollingEnabled(true);

        pagerAdatper = new FrPagerAdatper(this);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdatper);
        if(appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                int scrollRange = -1;
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if(tvToolbarTitle == null) return;
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if(scrollRange == -verticalOffset) {
                        tvToolbarTitle.setAlpha(1f);
                    } else {
                        tvToolbarTitle.setAlpha(0f);
                    }
                }

            });
        }
        initData();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if(extras == null) return;
        if(extras.containsKey("top")) {
            ArrayList<TopHeaderModel> top = extras.getParcelableArrayList("top");
            adapter0.setList(top);
            int pos = adapter0.currentSelected;
            rvTopLeagues.scrollToPosition(pos);
        }
        if(extras.containsKey("league")) {
            league = extras.getParcelable("league");
            GlideApp.with(this).load(Config.mediaUrl(league.cover)).into(ivAppbarImage);
        }
        int tag = extras.getInt("tag", 0);
        viewPager.setCurrentItem(tag % pagerAdatper.getCount());

    }

    private void changeLeague(long id) {
        this.id = id;
        if(pagerAdatper != null) {
            pagerAdatper.notifyDataSetChanged();
        }
    }

    private static class TopLeagueAdapter extends ItemAdapter2<TopHeaderModel> {
        private WeakReference<LeagueActivity> ref;
        private int currentSelected = -1;

        public TopLeagueAdapter(LeagueActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void onclick(View v, int position) {
            LeagueActivity activity = ref.get();
            if(activity == null) return;
            if(select(position)) {
                TopHeaderModel model = getItem(position);
                activity.setToolbarTitle(model.title);
                activity.rvTopLeagues.scrollToPosition(position);
                activity.changeLeague(model.id);
            }
        }

        @Override
        public void setList(List<TopHeaderModel> items) {
            if(items != null) {
                int size = items.size();
                for(int pos = 0; pos < size; pos++){
                    TopHeaderModel item = items.get(pos);
                    if(item.selected) {
                        currentSelected = pos;
                    }
                }
            }
            super.setList(items);
        }

        public boolean select(int position) {
            if(position == currentSelected) return false;
            try {
                int prevSelected = currentSelected;
                if(prevSelected >= 0) {
                    getItem(prevSelected).setSelected(false);
                    notifyItemChanged(prevSelected);
                }
            } catch (Exception e){ }

            try {
                currentSelected = position;
                if(currentSelected >= 0) {
                    getItem(currentSelected).setSelected(true);
                    notifyItemChanged(currentSelected);
                }
            } catch (Exception e){ }
            return true;
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.league_page_top_list_item;
        }
    }

    public static class TopHeaderModel implements Parcelable {

        public long id;
        public String title;
        public String imageUrl;
        public boolean selected;

        public TopHeaderModel(long id, String title, String imageUrl) {
            this.id = id;
            this.title = title;
            this.imageUrl = imageUrl;
            this.selected = false;
        }

        protected TopHeaderModel(Parcel in) {
            id = in.readLong();
            title = in.readString();
            imageUrl = in.readString();
            selected = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(title);
            dest.writeString(imageUrl);
            dest.writeByte((byte) (selected ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TopHeaderModel> CREATOR = new Creator<TopHeaderModel>() {
            @Override
            public TopHeaderModel createFromParcel(Parcel in) {
                return new TopHeaderModel(in);
            }

            @Override
            public TopHeaderModel[] newArray(int size) {
                return new TopHeaderModel[size];
            }
        };

        public TopHeaderModel setSelected(boolean selected) {
            this.selected = selected;
            return this;
        }

    }

    private static class FrPagerAdatper extends FragmentPagerAdapter {
        private WeakReference<LeagueActivity> ref;
        private String[] titles;
        private List<Fragment> fragments;

        public FrPagerAdatper(LeagueActivity activity) {
            super(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.ref = new WeakReference<>(activity);
            this.titles = activity.getResources().getStringArray(R.array.league_tabs);
            this.fragments = new ArrayList<>();
            initFragments();
        }

        private void initFragments(){
            LeagueActivity activity = ref.get();
            if(activity == null) return;
            this.fragments.clear();
            this.fragments.add(FrLeagueStanding.newInstance(activity.id));
            this.fragments.add(FrLeagueMatches.newInstance(activity.id));
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            try {
                return fragments.get(position);
            } catch (Exception e){ e.printStackTrace(); }
            return null;

        }

        @Override
        public int getCount() {
            return titles == null ? 0 : titles.length;

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            try {
                return titles[position];
            } catch (Exception e){ e.printStackTrace(); }
            return null;
        }
    }

}
