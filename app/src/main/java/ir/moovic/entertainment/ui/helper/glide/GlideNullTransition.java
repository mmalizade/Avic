package ir.moovic.entertainment.ui.helper.glide;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;

public class GlideNullTransition implements TransitionFactory<Drawable> {

    @Override
    public Transition<Drawable> build(DataSource dataSource, boolean isFirstResource) {
        return NoTransition.get();
    }


}
