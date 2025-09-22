package com.androidbuts.multispinnerfilter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;


public class FullScreenDialog extends DialogFragment {
    private String title = "";
    private RelativeLayout layoutProgressBar;

    private View view;
    private TextView txtContent;

    public static FullScreenDialog newInstance(String cmsTag) {
        FullScreenDialog fragment = new FullScreenDialog();
        Bundle args = new Bundle();
        args.putString("cms_tag", cmsTag);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.dialog_full_screen, container, false);

        Bundle b = this.getArguments();
        if (getArguments() != null) {

        }

        txtContent = view.findViewById(R.id.txtContent);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_search);
        toolbar.setNavigationOnClickListener(view1 -> getDialog().dismiss());
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.black));

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
