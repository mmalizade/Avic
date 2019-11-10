package ir.moovic.entertainment.app.football.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.app.football.model.StandingModel;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import retrofit2.Call;
import retrofit2.Response;

public class FrLeagueStanding extends MyBaseFragment {

    @BindView(R.id.rv)                      public RecyclerView rv;
    @Nullable @BindView(R.id.loading_pb)    public View loadingProgressView;
    @Nullable @BindView(R.id.main_view)     public View mainView;

    private ItemAdapter adapter;
    private long id;

    public static FrLeagueStanding newInstance(long id) {
        FrLeagueStanding fr = new FrLeagueStanding();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fr.setArguments(args);
        return fr;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(this);
        Bundle args = getArguments();
        if(args != null) {
            this.id = args.getLong("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_league_standing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        rv.setHasFixedSize(true);

        if(getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        }
        rv.setAdapter(adapter);
        loadData();
    }

    public void loadData() {
        enqueue(App.getFootballApiService().leagueStanding(id), new Callback1(this));
    }

    private void bindData(LeagueModel model){
        if(model == null) return;
        if(model.standing != null) {
            this.adapter.setList(model.standing.table);
        }
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
        if(mainView != null) {
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private static class Callback1 extends RetrofitCallback<LeagueModel> {

        private WeakReference<FrLeagueStanding> frRef;

        public Callback1(FrLeagueStanding fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<LeagueModel> call, Response<LeagueModel> response) {
            FrLeagueStanding fr = frRef.get();
            if(fr != null) {
                fr.bindData(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }


    private static class ItemAdapter extends ItemAdapter2<StandingModel.StandingTableItemModel> {

        private WeakReference<FrLeagueStanding> frRef;

        public ItemAdapter(FrLeagueStanding fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.standing_table_row_1;
        }

        @Override
        public void onclick(View v, int position) {

        }

    }

}
