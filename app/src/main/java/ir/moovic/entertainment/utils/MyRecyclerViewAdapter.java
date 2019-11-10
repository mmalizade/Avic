package ir.moovic.entertainment.utils;


import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MyRecyclerViewAdapter<DATA extends Comparable<DATA>, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<DATA> datalist;
    protected RequestManager glide;

    public MyRecyclerViewAdapter(RequestManager glide, List<DATA> list){
        this.glide = glide;
        if(list == null){
            this.datalist = new ArrayList<>();
        } else {
            this.datalist = list;
        }
    }

    public void setDatalist(List<DATA> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(getDiffUtilCallback(newData, datalist));
        if(datalist == null){
            datalist = new ArrayList<>();
        } else {
            this.datalist.clear();
        }
        if(newData != null) {
            this.datalist.addAll(newData);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    protected DiffUtil.Callback getDiffUtilCallback(List<DATA> newList, List<DATA> oldList){
        return new MyDiffUtilCallback<>(newList, oldList);
    }

    public static class MyDiffUtilCallback<T extends Comparable<T>> extends DiffUtil.Callback {
        private List<T> oldList;
        private List<T> newList;

        public MyDiffUtilCallback(List<T> newList, List<T> oldList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T newObject = newList.get(newItemPosition);
            T oldObject = oldList.get(oldItemPosition);
            return newObject != null && newObject.compareTo(oldObject) == 0;
        }

    }
}
