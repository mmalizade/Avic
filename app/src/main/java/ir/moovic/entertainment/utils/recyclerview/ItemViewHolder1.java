package ir.moovic.entertainment.utils.recyclerview;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;


public abstract class ItemViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ViewDataBinding binding;

    public ItemViewHolder1(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        setClickListeners(this.itemView);
    }

    public ItemViewHolder1(View view) {
        super(view);
    }

    public void bind(Object obj) {
        binding.setVariable(BR.obj, obj);
        binding.executePendingBindings();
    }

    @Override
    public void onClick(View v) {
        onListClick(v, getLayoutPosition());
    }

    protected void setClickListeners(View itemView) {
        itemView.setOnClickListener(this);
    }

    protected abstract void onListClick(View v, int position);

}
