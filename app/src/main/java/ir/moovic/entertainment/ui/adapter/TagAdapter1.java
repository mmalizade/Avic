package ir.moovic.entertainment.ui.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.moovic.entertainment.R;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter2;

public abstract class TagAdapter1 extends ItemAdapter2<String> {
    public static final String TAG = TagAdapter1.class.getSimpleName();

    public void setTags(String[] tags) {
        if(tags == null || tags.length == 0) {
            setList(null);
        } else {
            List<String> list = new ArrayList<>(Arrays.asList(tags));
            setList(list);
        }
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.news_tag_list_item_1;
    }

}
