package ir.moovic.entertainment.app.newspaper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.newspaper.fragments.FrNewsList;
import ir.moovic.entertainment.app.newspaper.model.NewsCategoryModel;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import ir.moovic.entertainment.utils.recyclerview.RtlGridLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class NewsCategoriesActivity extends MyBaseActivity {

    @BindView(R.id.rv)      public RecyclerView rv;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_categories);

        adapter = new ItemAdapter(this);

        rv.setLayoutManager(new RtlGridLayoutManager(this, 2));
        rv.setHasFixedSize(true);
        int space = getResources().getDimensionPixelSize(R.dimen.space_1);
        SpacesItemDecoration spaceDecor = new SpacesItemDecoration(space, space, space, space);
        rv.addItemDecoration(spaceDecor);
        rv.setAdapter(adapter);
        loadData();
        setToolbarTitle(getString(R.string.news));
    }

    private void loadData() {
        enqueue(App.getNewsApiService().categories(), new NewsCategoriesActivity.Callback1(this));
    }


    private static class Callback1 extends RetrofitCallback<List<NewsCategoryModel>> {

        private WeakReference<NewsCategoriesActivity> frRef;

        public Callback1(NewsCategoriesActivity fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<List<NewsCategoryModel>> call, Response<List<NewsCategoryModel>> response) {
            NewsCategoriesActivity fr = frRef.get();
            if(fr != null) {
                fr.adapter.setList(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }
    }

    private static class ItemAdapter extends ItemAdapter2<NewsCategoryModel> {
        private WeakReference<NewsCategoriesActivity> activityRef;

        public ItemAdapter(NewsCategoriesActivity fr) {
            this.activityRef = new WeakReference<>(fr);
        }


        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.news_category_list_item;
        }

        @Override
        public void onclick(View v, int position) {
            NewsCategoriesActivity activity = activityRef.get();
            if(activity == null) return;
            NewsCategoryModel model = getItem(position);
            Bundle args = new Bundle();
            args.putString(FrNewsList.QUERY_PREFIX + "catId", String.valueOf(model.getId()));
            args.putString(MyBaseActivity.KEY_PAGE_TITLE, model.getTitle());
            activity.startActivity(new Intent(activity, NewsListActivity.class).putExtras(args));
        }

    }

}
