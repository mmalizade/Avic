package ir.moovic.entertainment.utils.recyclerview;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


public abstract class ItemAdapter1<MODEL, VH extends ItemViewHolder1> extends RecyclerView.Adapter<VH> {

    protected List<MODEL> list;

    public ItemAdapter1() {
        this.list = new ArrayList<>();
    }

    public void clearAll(){
        if(list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public void setList(List<MODEL> items){
        this.list = items;
        notifyDataSetChanged();
    }



    public List<MODEL> getList(){
        return this.list;
    }

    public void removeItem(int position) {
        try {
            list.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e){}
    }

    public void addItem(MODEL item) {
        if(list == null) {
            list = new ArrayList<>();
        }
        try {
            int prevCount = getItemCount();
            list.add(item);
            int count = getItemCount();
            notifyItemRangeInserted(prevCount, count - prevCount);
        } catch (Exception e){}
    }

    public void addItems(List<MODEL> items) {
        if(items == null || items.isEmpty()) return;
        try {
            if(list == null) {
                list = new ArrayList<>();
            }
            int prevCount = getItemCount();
            list.addAll(items);
            int count = getItemCount();
            notifyItemRangeInserted(prevCount, count - prevCount);
        } catch (Exception e){
            Log.e("ItemAdapter1", "addItems: Exception => " + e.getMessage());
            notifyDataSetChanged();
        }
    }

    public MODEL getItem(int position) {
        try {
            return list.get(position);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public int getRealItemCount(){
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MODEL model = getItem(position);
        holder.bind(model);
    }

    protected ViewDataBinding getBinding(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(layoutInflater, getLayoutViewId(viewType), parent, false);
    }

    @LayoutRes
    public abstract int getLayoutViewId(int viewType);

}
