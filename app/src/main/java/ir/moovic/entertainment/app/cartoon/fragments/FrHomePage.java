package ir.moovic.entertainment.app.cartoon.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.cartoon.activities.CartoonPageActivity;
import ir.moovic.entertainment.app.cartoon.model.ProductModel;
import ir.moovic.entertainment.app.cartoon.model.ResBundle1;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter1;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import ir.moovic.entertainment.utils.recyclerview.ItemViewHolder1;
import ir.moovic.entertainment.utils.recyclerview.RtlHorizontalLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

@SuppressLint("UnknownNullness")

public class FrHomePage extends MyBaseFragment {

    @BindView(R.id.rv)  RecyclerView rv;
    @Nullable @BindView(R.id.loading_pb)    View loadingProgressView;
    @Nullable @BindView(R.id.main_view)     View mainView;

    BundleAdapter bundleAdatepr;
    SpacesItemDecoration decor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundleAdatepr = new BundleAdapter(this);
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

        // load
        enqueue(App.getCartoonApiService().home(), new Callback1(this));
    }

    void display(List<ResBundle1> res) {

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        if(decor != null) {
            rv.removeItemDecoration(decor);
        }

        int space = getResources().getDimensionPixelSize(R.dimen.content_padding_2);
        decor = new SpacesItemDecoration(0, space, 0, space);
        rv.addItemDecoration(decor);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(bundleAdatepr);
        if(getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        bundleAdatepr.setList(res);
    }

    @Override
    public void loading(boolean show) {
        if(loadingProgressView != null) {
            loadingProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if(mainView != null) {
            mainView.setVisibility(!show ? View.VISIBLE : View.GONE);
        }

    }

    private static class Callback1 extends RetrofitCallback<List<ResBundle1>> {

        private WeakReference<FrHomePage> frRef;

        public Callback1(FrHomePage fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<List<ResBundle1>> call, Response<List<ResBundle1>> response) {
            FrHomePage fr = frRef.get();
            if(fr != null && response.body() != null) {
                fr.display(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }

    static class BundleAdapter extends ItemAdapter1<ResBundle1, BundleAdapter.VH> {
        private WeakReference<FrHomePage> frRef;

        public BundleAdapter(FrHomePage fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.cartoon_home_bundle_list_item_1;
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
            protected void setClickListeners(View itemView) {
                itemView.setOnClickListener(null);
            }

            @Override
            protected void onListClick(View v, int position) {

            }
        }

    }

    static class ProductAdapter extends ItemAdapter2<ProductModel> {
        private WeakReference<Context> contextRef;
        private int rowType = 1;

        public ProductAdapter(Context context) {
            this.contextRef = new WeakReference<>(context);
        }

        public void setList(List<ProductModel> items, int rowType) {
            this.rowType = rowType;
            super.setList(items);
        }


        @Override
        public void onclick(View v, int position) {
            ProductModel model = getItem(position);
            if(model != null) {
                contextRef.get().startActivity(
                        new Intent(contextRef.get(), CartoonPageActivity.class)
                                .putExtra("product", model)
                );
            }
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return this.rowType == 1 ? R.layout.cartoon_product_list_item_1 : R.layout.cartoon_product_list_item_2;
        }
    }

    @BindingAdapter("bind:cartoonProducts")
    public static void bindProducts(RecyclerView rv, ResBundle1 bundle) {
        Context context = rv.getContext();
        rv.setLayoutManager(new RtlHorizontalLayoutManager(context));
        if(rv.getItemDecorationCount() == 0) {
            int space = rv.getResources().getDimensionPixelSize(R.dimen.content_padding_2);
            SpacesItemDecoration decoration = new SpacesItemDecoration(space, SpacesItemDecoration.RTL_HORIZONTAL);
            rv.addItemDecoration(decoration);
        }
        ProductAdapter adapter = new ProductAdapter(context);
        adapter.setList(bundle.products, bundle.rowType);
        rv.setAdapter(adapter);
    }




}
