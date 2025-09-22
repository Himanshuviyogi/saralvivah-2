package com.advance.sanatani_vivah.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.advance.sanatani_vivah.R;

public class VideoViewDialogFragment extends DialogFragment {

    private String videoUrl;

    public static VideoViewDialogFragment newInstance(String url) {
        VideoViewDialogFragment videoViewDialogFragment = new VideoViewDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        videoViewDialogFragment.setArguments(bundle);
        return videoViewDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoUrl = getArguments().getString("URL");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video_view_dialog, container, false);

//        UniversalMediaController universalMediaController = rootView.findViewById(R.id.mediaController);
//        UniversalVideoView universalVideoView = rootView.findViewById(R.id.videoView);

//        universalVideoView.setMediaController(universalMediaController);
//        universalVideoView.setVideoPath(videoUrl);
//        universalVideoView.start();
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}