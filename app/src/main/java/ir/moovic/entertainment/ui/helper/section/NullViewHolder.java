package ir.moovic.entertainment.ui.helper.section;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class NullViewHolder extends RecyclerView.ViewHolder {

    public NullViewHolder(View itemView) {
        super(itemView);
        throw new AssertionError("This is a dummy type. Do not construct NullViewHolder");
    }
}