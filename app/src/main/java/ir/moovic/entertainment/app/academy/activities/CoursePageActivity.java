package ir.moovic.entertainment.app.academy.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.academy.model.CourseModel;
import ir.moovic.entertainment.app.academy.model.VideoModel;
import ir.moovic.entertainment.app.academy.presenter.CoursePagePresenter;
import ir.moovic.entertainment.app.config.App;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.ui.helper.section.DynamicSectionsAdapter;
import ir.moovic.entertainment.ui.helper.section.NullViewHolder;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter1;
import ir.moovic.entertainment.utils.recyclerview.ItemViewHolder1;
import ir.moovic.entertainment.utils.recyclerview.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class CoursePageActivity extends MyBaseActivity implements CoursePagePresenter {

    @Nullable @BindView(R.id.rv_file_infos)         RecyclerView rvFiles;
    @Nullable @BindView(R.id.rv_related_products)   RecyclerView rvRelated;
    @Nullable @BindView(R.id.lyt_related)           View lytRelated;
    @BindView(R.id.rv) RecyclerView rv;
    @Nullable @BindView(R.id.loading_pb) View pb;

    ViewDataBinding binding;
    CourseModel course;
    ItemAdapter adapter;
    ItemSectionAdapter sectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_academy_course_page);
        ButterKnife.bind(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
//        int space = getResources().getDimensionPixelSize(R.dimen.space_0);
//        rv.addItemDecoration(new SpacesItemDecoration(0, space, 0, space));
        adapter = new ItemAdapter(this);
        sectionAdapter = new ItemSectionAdapter(this, adapter);
        rv.setAdapter(sectionAdapter);

        course = getIntent().getParcelableExtra("course");
        if (course != null) {
            setToolbarTitle(course.name);
        }
        binding.setVariable(BR.presenter, this);
        bind(course);
        if(course != null) {
            enqueue(App.getAcademyApiService().course(course.id), new Callback1(this));
        }

    }



    void bind(CourseModel model) {
        if(model != null) {
            this.course = model;
            binding.setVariable(BR.obj, this.course);
            binding.executePendingBindings();
            adapter.setList(model.videos);
            sectionAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void loading(boolean show) {
        super.loading(show);
        if(pb != null) {
            pb.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showInformation(View v) {

    }

    @Override
    public void payment(View v) {

    }

    private static class Callback1 extends RetrofitCallback<CourseModel> {

        private WeakReference<CoursePageActivity> ref;

        public Callback1(CoursePageActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void success(Call<CourseModel> call, Response<CourseModel> response) {
            CoursePageActivity activity = ref.get();
            if(activity != null) {
                activity.bind(response.body());
            }
        }

    }

    private static class ItemAdapter extends ItemAdapter1<VideoModel, ItemAdapter.VH> {

        WeakReference<CoursePageActivity> ref;

        public ItemAdapter(CoursePageActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.course_page_video_list_item;
        }


        @NonNull
        @Override
        public ItemAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding binding = getBinding(parent, viewType);
            return new ItemAdapter.VH(binding);
        }

        public class VH extends ItemViewHolder1 {

            public VH(ViewDataBinding binding) {
                super(binding);
            }

            @Override
            protected void onListClick(View v, int position) {
                CoursePageActivity activity = ref.get();
                if(activity == null) return;
                int pos = activity.sectionAdapter.getOriginalPositionForPosition(position);
                VideoModel model = getItem(pos);
            }
        }

    }

    private static class ItemSectionAdapter extends DynamicSectionsAdapter<ItemAdapter.VH, ItemSectionAdapter.HeaderViewHolder, NullViewHolder> {
        private WeakReference<CoursePageActivity> ref;
        private ItemAdapter1<VideoModel, ItemAdapter.VH> mItemsAdapter;

        public ItemSectionAdapter(CoursePageActivity activity, ItemAdapter1<VideoModel, ItemAdapter.VH> itemsAdapter) {
            super(itemsAdapter);
            mItemsAdapter = itemsAdapter;
            ref = new WeakReference<>(activity);
        }

        @Override
        protected long getSectionId(int originalAdapterPosition) {
            try {
                return mItemsAdapter.getItem(originalAdapterPosition).lectureIndex;
            } catch (Exception e){
                return -1;
            }
        }

        @Override
        protected boolean sectionHasHeader(long sectionId) {
            return true;
        }

        private int sectionHeaderLayoutViewId(){
            return R.layout.course_page_video_section_header_item_1;
        }

        @Override
        protected ItemSectionAdapter.HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(sectionHeaderLayoutViewId(), parent, false);
            return new ItemSectionAdapter.HeaderViewHolder(v);
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView tv;
            public HeaderViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
            }
        }

        @Override
        protected void onBindHeaderViewHolder(ItemSectionAdapter.HeaderViewHolder holder, long sectionId) {
            super.onBindHeaderViewHolder(holder, sectionId);
            holder.tv.setText(sectionTitle(sectionId));
        }

        private String sectionTitle(long sectionId){
            CoursePageActivity activity = ref.get();
            if(activity == null) return "";
            if(sectionId < 0) {
                return activity.getString(R.string.this_course_sessions);
            }
            try {
                return activity.course.lectures[(int) sectionId];
            } catch (Exception e){
                return activity.getString(R.string.this_course_sessions);
            }
        }

    }


}
