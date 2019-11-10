package ir.moovic.entertainment.app.football.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.app.football.activities.LeagueActivity;
import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter1;
import ir.moovic.entertainment.utils.recyclerview.ItemViewHolder1;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class FrLeagues extends MyBaseFragment {

    @BindView(R.id.rv)      public RecyclerView rv;

    private ItemAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(this);
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
        SpacesItemDecoration spaceDecor = new SpacesItemDecoration(2*space, space, 2*space, space);
        rv.addItemDecoration(spaceDecor);
        if(getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        }
        rv.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        enqueue(App.getFootballApiService().leagues(), new Callback1(this));
    }


    private static class Callback1 extends RetrofitCallback<List<LeagueModel>> {

        private WeakReference<FrLeagues> frRef;

        public Callback1(FrLeagues fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<List<LeagueModel>> call, Response<List<LeagueModel>> response) {
            FrLeagues fr = frRef.get();
            if(fr != null) {
                fr.adapter.setList(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }

    private static class ItemAdapter extends ItemAdapter1<LeagueModel, ItemAdapter.ViewHolder> {

        private WeakReference<FrLeagues> frRef;

        public ItemAdapter(FrLeagues fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.league_list_item;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding binding = getBinding(parent, viewType);
            return new ViewHolder(binding);
        }

        public class ViewHolder extends ItemViewHolder1 {

            public ViewHolder(ViewDataBinding binding) {
                super(binding);
            }

            @Override
            protected void setClickListeners(View itemView) {
                View v = itemView.findViewById(R.id.card_prediction);
                if(v != null)
                    v.setOnClickListener(this);
                v = itemView.findViewById(R.id.card_prediction2);
                if(v != null)
                    v.setOnClickListener(this);
                v = itemView.findViewById(R.id.card_standing);
                if(v != null)
                    v.setOnClickListener(this);
                v = itemView.findViewById(R.id.card_ranking);
                if(v != null)
                    v.setOnClickListener(this);
            }

            @Override
            protected void onListClick(View v, int position) {
                FrLeagues fr = frRef.get();
                if(fr == null) return;
                LeagueModel model = getItem(position);
                String tag = "0";
                try {
                    tag = (String) v.getTag();
                } catch (Exception e){}
                List<LeagueModel> list = fr.adapter.getList();
                ArrayList<LeagueActivity.TopHeaderModel> top = new ArrayList<>();
                if(list != null && !list.isEmpty()){
                    for(LeagueModel league:list) {
                        top.add(new LeagueActivity.TopHeaderModel(league.id, league.title, league.cover).setSelected(league.id == model.id));
                    }
                }
                Intent intent = new Intent(fr.getContext(), LeagueActivity.class)
                        .putExtra("id", model.id)
                        .putExtra("league", model)
                        .putExtra("tag", Integer.valueOf(tag))
                        .putExtra(MyBaseActivity.KEY_PAGE_TITLE, model.title)
                        .putParcelableArrayListExtra("top", top);
                fr.startActivity(intent);
            }

        }
    }

}
