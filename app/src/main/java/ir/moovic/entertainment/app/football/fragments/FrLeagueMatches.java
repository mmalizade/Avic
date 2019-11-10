package ir.moovic.entertainment.app.football.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.app.football.activities.PredictionActivity;
import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.app.football.model.MatchModel;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.ui.helper.RecyclerViewScrollToPosition;
import ir.moovic.entertainment.ui.helper.section.DynamicSectionsAdapter;
import ir.moovic.entertainment.ui.helper.section.NullViewHolder;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter1;
import ir.moovic.entertainment.utils.recyclerview.ItemViewHolder1;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class FrLeagueMatches extends MyBaseFragment {

    @BindView(R.id.rv)                      public RecyclerView rv;
    @Nullable @BindView(R.id.loading_pb)    public View loadingProgressView;

    private ItemAdapter adapter;
    private LeagueModel leagueModel;
    private ItemSectionAdapter sectionAdapter;
    private RecyclerViewScrollToPosition rvScrollHelper;
    private long id;
    public static FrLeagueMatches newInstance(long id){
        FrLeagueMatches fr = new FrLeagueMatches();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fr.setArguments(args);
        return fr;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(this);
        sectionAdapter = new ItemSectionAdapter(this, adapter);
        Bundle args = getArguments();
        if(args != null) {
            this.id = args.getLong("id");
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
        rvScrollHelper = new RecyclerViewScrollToPosition(rv);
//        rv.setHasFixedSize(true);
        int space = getResources().getDimensionPixelSize(R.dimen.space_2);
        SpacesItemDecoration spaceDecor = new SpacesItemDecoration(space, space, space, space);
        rv.addItemDecoration(spaceDecor);
        rv.setAdapter(sectionAdapter);

        loadData();
    }

    public void loadData() {
        adapter.clearAll();
        sectionAdapter.notifyDataSetChanged();
        enqueue(App.getFootballApiService().league(id), new Callback1(this));
    }

    private void bindData(LeagueModel model){
        if(model == null) return;
        this.leagueModel = model;
        this.adapter.setList(model.matches);
        this.sectionAdapter.notifyDataSetChanged();
        scrollToCurrentPeriod();
    }

    private void scrollToCurrentPeriod() {
        if(leagueModel == null) return;
        int pos = sectionAdapter.getOriginalPositionForFirstItemInSection(leagueModel.periodShow) + leagueModel.periodShow-1;
        if(pos > 0) {
            rvScrollHelper.scrollToPosition(pos);
        }
    }

    void onMatchClick(MatchModel model) {
        Intent intent = new Intent(getContext(), PredictionActivity.class)
                .putExtra("match", model)
                .putExtra("cover", leagueModel != null ? leagueModel.cover : "");
        startActivity(intent);
    }

    public void setId(long id) {
        if(id > 0) {
            this.id = id;
        }
    }

    @Override
    public void loading(boolean show) {
        if(loadingProgressView != null) {
            loadingProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if(rv != null) {
            rv.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private static class Callback1 extends RetrofitCallback<LeagueModel> {

        private WeakReference<FrLeagueMatches> frRef;

        public Callback1(FrLeagueMatches fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<LeagueModel> call, Response<LeagueModel> response) {
            FrLeagueMatches fr = frRef.get();
            if(fr != null) {
                fr.bindData(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }


    private static class ItemAdapter extends ItemAdapter1<MatchModel,ItemAdapter.VH> {

        private WeakReference<FrLeagueMatches> frRef;

        public ItemAdapter(FrLeagueMatches fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.match_list_item_2;
        }


        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding binding = getBinding(parent, viewType);
            return new VH(binding);
        }

        public class VH extends ItemViewHolder1 {

            public VH(ViewDataBinding binding) {
                super(binding);
            }

            @Override
            protected void onListClick(View v, int position) {
                FrLeagueMatches fr = frRef.get();
                if(fr == null) return;
                int pos = fr.sectionAdapter.getOriginalPositionForPosition(position);
                MatchModel model = getItem(pos);
                fr.onMatchClick(model);
            }
        }

    }

    private static class ItemSectionAdapter extends DynamicSectionsAdapter<ItemAdapter.VH, ItemSectionAdapter.HeaderViewHolder, NullViewHolder> {
        private WeakReference<FrLeagueMatches> frRef;
        private ItemAdapter1<MatchModel, ItemAdapter.VH> mItemsAdapter;

        public ItemSectionAdapter(FrLeagueMatches fr, ItemAdapter1<MatchModel, ItemAdapter.VH> itemsAdapter) {
            super(itemsAdapter);
            mItemsAdapter = itemsAdapter;
            frRef = new WeakReference<>(fr);
        }

        @Override
        protected long getSectionId(int originalAdapterPosition) {
            try {
                return mItemsAdapter.getItem(originalAdapterPosition).period;
            } catch (Exception e){
                return -1;
            }
        }

        @Override
        protected boolean sectionHasHeader(long sectionId) {
            return sectionId > 0;
        }

        private int sectionHeaderLayoutViewId(){
            return R.layout.league_period_match_header_item_1;
        }

        @Override
        protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(sectionHeaderLayoutViewId(), parent, false);
            return new HeaderViewHolder(v);
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView tv;
            public HeaderViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
            }
        }

        @Override
        protected void onBindHeaderViewHolder(HeaderViewHolder holder, long sectionId) {
            super.onBindHeaderViewHolder(holder, sectionId);
            holder.tv.setText(sectionTitle(sectionId));
        }

        private String sectionTitle(long sectionId){
            FrLeagueMatches fr = frRef.get();
            if(fr == null || sectionId <= 0) return "";
            String[] array = fr.getResources().getStringArray(R.array.number_to_word);
            try {
                return fr.getString(R.string.week_s, array[(int) (sectionId-1)]);
            } catch (Exception e){
                return fr.getString(R.string.week_s, String.valueOf(sectionId));
            }
        }

    }

}
