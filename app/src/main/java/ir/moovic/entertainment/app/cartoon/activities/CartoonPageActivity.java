package ir.moovic.entertainment.app.cartoon.activities;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.cartoon.model.CartoonFileInfo;
import ir.moovic.entertainment.app.cartoon.model.CategoryModel;
import ir.moovic.entertainment.app.cartoon.model.ProductModel;
import ir.moovic.entertainment.app.cartoon.presenter.CartoonPagePresenter;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import ir.moovic.entertainment.utils.recyclerview.RtlHorizontalLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class CartoonPageActivity extends MyBaseActivity implements CartoonPagePresenter {

    @Nullable @BindView(R.id.rv_file_infos)         RecyclerView rvFiles;
    @Nullable @BindView(R.id.rv_related_products)   RecyclerView rvRelated;
    @Nullable @BindView(R.id.lyt_related)           View lytRelated;

    ViewDataBinding binding;
    ProductModel product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cartoon_page);
        ButterKnife.bind(this);
        product = getIntent().getParcelableExtra("product");
        if (product != null) {
            setToolbarTitle(product.name);
        }
        binding.setVariable(BR.presenter, this);
        bind(product);
        if(product != null) {
            enqueue(App.getCartoonApiService().product(product.id), new Callback1(this));
            enqueue(App.getCartoonApiService().relatedProducts(product.id), new Callback2(this));
        }


        if(appBarLayout != null) {

            if(tvToolbarTitle != null) {
                tvToolbarTitle.setAlpha(0f);
            }

            appBarLayout.addOnOffsetChangedListener(new AppBarOnOffsetChargedListener(this));
        }
    }

    private static class AppBarOnOffsetChargedListener implements AppBarLayout.OnOffsetChangedListener {
        private WeakReference<CartoonPageActivity> ref;
        int scrollRange = -1;
        private int elevation = 0;
        private StateListAnimator elevationAnimatorOn;
        private StateListAnimator elevationAnimatorOff;

        public AppBarOnOffsetChargedListener(CartoonPageActivity activity) {
            this.ref = new WeakReference<>(activity);
            elevation = activity.getResources().getDimensionPixelSize(R.dimen.toolbar_elevation);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevationAnimatorOn = AnimatorInflater.loadStateListAnimator(activity, R.animator.appbar_elevation);
                elevationAnimatorOff = AnimatorInflater.loadStateListAnimator(activity, R.animator.appbar_elevation_disable);
            }
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            CartoonPageActivity activity = ref.get();
            if(activity == null || activity.tvToolbarTitle == null) return;
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if(scrollRange == -verticalOffset) {
                activity.tvToolbarTitle.setAlpha(1f);
                if(activity.btnToolbarBack != null) {
                    activity.btnToolbarBack.setColorFilter(Color.BLACK);
                }
                toolbarElevation(true);
            } else {
                activity.tvToolbarTitle.setAlpha(0f);
                if(activity.btnToolbarBack != null) {
                    activity.btnToolbarBack.setColorFilter(Color.WHITE);
                }
                toolbarElevation(false);
            }
        }

        public void toolbarElevation(boolean on) {
            CartoonPageActivity activity = ref.get();
            if(activity == null || activity.appBarLayout == null) return;
            if(on) {
                if(activity.isAppBarElavationEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.appBarLayout.setStateListAnimator(elevationAnimatorOn);
                    } else {
                        ViewCompat.setElevation(activity.appBarLayout, elevation);
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.appBarLayout.setStateListAnimator(elevationAnimatorOff);
                } else {
                    ViewCompat.setElevation(activity.appBarLayout, 0);
                }
            }
        }
    }

    @Override
    protected boolean isAppBarElavationEnabled() {
        return false;
    }

    void bind(ProductModel model) {
        if(model != null) {
            this.product = model;
            binding.setVariable(BR.obj, this.product);
            binding.executePendingBindings();
        }
    }

    void displayRelated(List<ProductModel> list) {
        if(lytRelated != null) {
            lytRelated.setVisibility((list != null && !list.isEmpty()) ? View.VISIBLE : View.GONE);
        }
        if(rvRelated != null) {
            rvRelated.setLayoutManager(new RtlHorizontalLayoutManager(this));
            if(rvRelated.getItemDecorationCount() == 0) {
                int space = rvRelated.getResources().getDimensionPixelSize(R.dimen.content_padding_2);
                SpacesItemDecoration decoration = new SpacesItemDecoration(space, 0, space, 0);
                rvRelated.addItemDecoration(decoration);
            }
            ProductAdapter adapter = new ProductAdapter(this);
            adapter.setList(list);
            rvRelated.setAdapter(adapter);
        }
    }

    @Override
    public void onClickWatchButton(View v) {
        if(product != null) {
            if(product.hasFileSerie()) {
                if(rvFiles != null) {
                    rvFiles.requestFocus();
                }
            } else {

            }
        }
    }


    private static class Callback1 extends RetrofitCallback<ProductModel> {

        private WeakReference<CartoonPageActivity> ref;

        public Callback1(CartoonPageActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void success(Call<ProductModel> call, Response<ProductModel> response) {
            CartoonPageActivity activity = ref.get();
            if(activity != null) {
                activity.bind(response.body());
            }
        }
    }

    private static class Callback2 extends RetrofitCallback<List<ProductModel>> {

        private WeakReference<CartoonPageActivity> ref;

        public Callback2(CartoonPageActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void success(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
            CartoonPageActivity activity = ref.get();
            if(activity != null) {
                activity.displayRelated(response.body());
            }
        }
    }


    public static class FileInfoAdapter extends ItemAdapter2<CartoonFileInfo> {
        private WeakReference<Context> contextRef;

        public FileInfoAdapter(Context context) {
            this.contextRef = new WeakReference<>(context);
        }

        @Override
        public void onclick(View v, int position) {

        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.cartoon_file_info_list_item;
        }

    }

    static class CategoryAdapter extends ItemAdapter2<CategoryModel> {
        private WeakReference<Context> contextRef;

        public CategoryAdapter(Context context) {
            this.contextRef = new WeakReference<>(context);
        }


        @Override
        public void onclick(View v, int position) {
            CategoryModel model = getItem(position);
            contextRef.get().startActivity(
                    new Intent(contextRef.get(), CartoonProductListActivity.class)
                            .putExtra("listName", model.title)
                            .putExtra("q_catId", model.id)
            );
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.cartoon_category_tag_list_item;
        }
    }

    static class ProductAdapter extends ItemAdapter2<ProductModel> {
        private WeakReference<CartoonPageActivity> ref;

        public ProductAdapter(CartoonPageActivity activity) {
            this.ref = new WeakReference<>(activity);
        }


        @Override
        public void onclick(View v, int position) {
            ProductModel model = getItem(position);
            CartoonPageActivity activity = ref.get();
            if(model != null) {
                activity.startActivity(
                        new Intent(activity, CartoonPageActivity.class)
                                .putExtra("product", model)
                );
            }
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.cartoon_product_list_item_1;
        }
    }


    @BindingAdapter("bind:cartoonFileInfos")
    public static void bindCartoonFileInfos(RecyclerView rv, List<CartoonFileInfo> files) {
        if(files != null && files.size() > 1) {
            rv.setVisibility(View.VISIBLE);
            Context context = rv.getContext();
            rv.setLayoutManager(new LinearLayoutManager(context));
            if(rv.getItemDecorationCount() == 0) {
                int space = rv.getResources().getDimensionPixelSize(R.dimen.content_padding_1);
                SpacesItemDecoration decoration = new SpacesItemDecoration(0, space, 0, space);
                rv.addItemDecoration(decoration);
            }
            rv.setNestedScrollingEnabled(false);
            FileInfoAdapter adapter = new FileInfoAdapter(context);
            adapter.setList(files);
            rv.setAdapter(adapter);
        } else {
            rv.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("bind:cartoonCategories")
    public static void bindCartoonCategories(RecyclerView rv, List<CategoryModel> categories) {
        if(categories != null && !categories.isEmpty()) {
            rv.setVisibility(View.VISIBLE);
            Context context = rv.getContext();
            rv.setLayoutManager(new RtlHorizontalLayoutManager(context));
            if(rv.getItemDecorationCount() == 0) {
                int space = rv.getResources().getDimensionPixelSize(R.dimen.content_padding_1);
                SpacesItemDecoration decoration = new SpacesItemDecoration(space, SpacesItemDecoration.RTL_HORIZONTAL);
                rv.addItemDecoration(decoration);
            }
            rv.setNestedScrollingEnabled(false);
            CategoryAdapter adapter = new CategoryAdapter(context);
            adapter.setList(categories);
            rv.setAdapter(adapter);
        } else {
            rv.setVisibility(View.GONE);
        }
    }


}
