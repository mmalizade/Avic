package ir.moovic.entertainment.app.academy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.academy.activities.CoursePageActivity;
import ir.moovic.entertainment.app.academy.model.CourseModel;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.ui.helper.EndlessRecyclerOnScrollListener;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;
import ir.moovic.entertainment.utils.recyclerview.RtlGridLayoutManager;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class FrCourseList extends MyBaseFragment {

    public static final String QUERY_PREFIX = "q_";

    @BindView(R.id.rv)      public RecyclerView rv;

    ItemAdapter adapter;
    private HashMap<String,String> query = new HashMap<>();

    public static FrCourseList newInstance(HashMap<String,String> queryParams) {
        FrCourseList fr = new FrCourseList();
        Bundle args = new Bundle();
        if(queryParams !=null && !queryParams.isEmpty()) {
            Set<String> keys = queryParams.keySet();
            for (String key:keys) {
                args.putString(key, queryParams.get(key));
            }
        }
        fr.setArguments(args);
        return fr;
    }

    public static FrCourseList newInstance(Bundle args) {
        FrCourseList fr = new FrCourseList();
        fr.setArguments(args);
        return fr;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter(this);
        initQuery();
    }

    private void initQuery(){
        this.query = new HashMap<>();
        Bundle args = getArguments();
        if(args != null) {
            for (String key: args.keySet()) {
                try {
                    if(key.startsWith(QUERY_PREFIX)) {
                        this.query.put(
                                key.replaceFirst(QUERY_PREFIX, ""),
                                args.getString(key, "")
                        );
                    }

                } catch (Exception e){ e.printStackTrace(); }
            }
        }
        setPage(1);
    }

    private void setPage(int page) {
        int page1 = (page > 0) ? page : 1;
        if(this.query != null) {
            this.query.put("page", String.valueOf(page1));
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
        rv.setLayoutManager(new RtlGridLayoutManager(getContext(), 2));
        rv.setHasFixedSize(true);
        int space = getResources().getDimensionPixelSize(R.dimen.space_2);
        SpacesItemDecoration spaceDecor = new SpacesItemDecoration(space, space, space, space);
        rv.addItemDecoration(spaceDecor);
        rv.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                loadData(currentPage + 1);
            }
        });
        rv.setAdapter(adapter);
        loadData();
    }

    void loadData() {
        loadData(1);
    }

    void loadData(int page) {
        this.setPage(page);
        enqueue(App.getAcademyApiService().courseList(query), new Callback1(this));
    }

    static class Callback1 extends RetrofitCallback<List<CourseModel>> {

        private WeakReference<FrCourseList> frRef;

        public Callback1(FrCourseList fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public void success(Call<List<CourseModel>> call, Response<List<CourseModel>> response) {
            FrCourseList fr = frRef.get();
            if(fr != null) {
                fr.adapter.addItems(response.body());
            }
        }

        @Override
        public void failed(String message) {
            super.failed(message);
        }

    }

    private static class ItemAdapter extends ItemAdapter2<CourseModel> {

        private WeakReference<FrCourseList> frRef;

        public ItemAdapter(FrCourseList fr) {
            this.frRef = new WeakReference<>(fr);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.academy_course_list_item;
        }

        @Override
        public void onclick(View v, int position) {
            FrCourseList fr = frRef.get();
            if(fr == null) return;
            CourseModel model = getItem(position);
            fr.startActivity(new Intent(fr.getContext(), CoursePageActivity.class).putExtra("course", model));
        }
    }

}
