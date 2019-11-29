package ir.moovic.entertainment.app.academy.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.academy.model.CourseModel;
import ir.moovic.entertainment.controller.AppConfig;
import ir.moovic.entertainment.ui.binding.BindingUtils;

public class DialogFrDescription extends DialogFragment implements View.OnClickListener {
    String desc;
    CourseModel model;
    @BindView(R.id.tv_academy_desc)
    AppCompatTextView tv;
    @BindView(R.id.toolbar_back)
    AppCompatImageView back;
    @BindView(R.id.course_image)
    AppCompatImageView iv;

    @BindView(R.id.cours_title)
    AppCompatTextView tvTitle;

    public static DialogFrDescription newInstance(CourseModel model) {
        DialogFrDescription fr = new DialogFrDescription();
        Bundle args = new Bundle();
        args.putParcelable("model", model);
        fr.setArguments(args);
        return fr;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        Bundle args = getArguments();
        if (args != null) {
            this.model = args.getParcelable("model");
            this.desc=model.description;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fr_descriprion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        lodaData();
        back.setOnClickListener(this);
    }


    void lodaData() {
        tv.setText(desc);
        tvTitle.setText(model.name);
        BindingUtils.bindImageUrlCircleCrop(iv,model.cover);

    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
}
