package ir.moovic.entertainment.app.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.app.main.models.ResBundle;
import ir.moovic.entertainment.app.main.models.ResHomePage;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.ui.adapter.AdapterConstant;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import ir.moovic.entertainment.utils.recyclerview.RtlGridLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class FrHomePage2 extends MyBaseFragment {

    @BindView(R.id.rv)  public RecyclerView rv;
    ItemAdapter adapter;
    private SpacesItemDecoration decor;

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
        load();
    }

    private void load(){
        enqueue(App.getApiService().homePage(), new Callback1(this));
    }

    void display(ResHomePage res) {
        RtlGridLayoutManager lman = new RtlGridLayoutManager(getContext(),
                res.rowConfig != null ? res.rowConfig.span: AdapterConstant.Grid.HOME_PAGE_GRID_FULL_SPAN_COUNT);
        lman.setSpanSizeLookup(new AdapterSpanLookup(this));
        rv.setLayoutManager(lman);

//        if(decor != null) {
//            rv.removeItemDecoration(decor);
//        }

        int space = getResources().getDimensionPixelSize(R.dimen.content_padding_2);
        decor = new SpacesItemDecoration(space, space, space, space);
        rv.addItemDecoration(decor);

        adapter.setRowConfig(res.rowConfig);
        rv.setAdapter(adapter);


        adapter.setList(res.bundles);
    }

    private static class Callback1 extends RetrofitCallback<ResHomePage> {

        private WeakReference<FrHomePage2> frRef;

        public Callback1(FrHomePage2 fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<ResHomePage> call, Response<ResHomePage> response) {
            FrHomePage2 fr = frRef.get();
            if(fr != null && response.body() != null) {
                fr.display(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }

    private static class ItemAdapter extends ItemAdapter2<ResBundle> {
        private WeakReference<FrHomePage2> frRef;
        private ResHomePage.RowConfig rowConfig;

        public ItemAdapter(FrHomePage2 fr) {
            this.frRef = new WeakReference<>(fr);
            rowConfig = new ResHomePage.RowConfig();
            rowConfig.ratioW = 16;
            rowConfig.ratioH = 9;
            rowConfig.span = AdapterConstant.Grid.HOME_PAGE_GRID_FULL_SPAN_COUNT;
        }

        public void setRowConfig(ResHomePage.RowConfig rowConfig) {
            if(rowConfig != null) {
                this.rowConfig = rowConfig;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.homepage_bundle_list_item;
        }

        @Override
        public void onclick(View v, int position) {
            FrHomePage2 fr = frRef.get();
            if(fr != null) {
                ResBundle item = getItem(position);
                if(item != null) {
                    fr.IPageController.controlHomeTarget(item);
                }
            }
        }

    }

    private static class AdapterSpanLookup extends GridLayoutManager.SpanSizeLookup {

        private WeakReference<FrHomePage2> frRef;
        public AdapterSpanLookup(FrHomePage2 fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getSpanSize(int position) {
            FrHomePage2 fr = frRef.get();
            if(fr != null) {
                ResBundle item = fr.adapter.getItem(position);
                return item.span;
            }
            return 0;
        }
    }

}
