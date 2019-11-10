package ir.moovic.entertainment.app.academy.activities;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.academy.fragments.FrCourseList;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;

public class CourseListActivity extends MyBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_course_list);

        Bundle extras = getIntent().getExtras();
        FrCourseList fr = FrCourseList.newInstance(extras);
        animatedReplaceFragment(fr, R.id.container, false);

        String t = getIntent().getStringExtra("listName");
        if(!TextUtils.isEmpty(t)) {
            setToolbarTitle(t);
        }
    }

}
