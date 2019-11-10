package ir.moovic.entertainment.utils.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public abstract class ItemAdapter2<T> extends ItemAdapter1<T, ItemAdapter2.VH> {


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
        protected void onListClick(View v, int position) {
            try {
                onclick(v, position);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public abstract void onclick(View v, int position);
}
