package com.mtn.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = "NewsDetailActivity";
    NewsItem newsItem = null;
   static boolean isFullScreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        newsItem = getIntent().getParcelableExtra("newsItem");
        TextView textView = findViewById(R.id.txtTitle);
        textView.setText(newsItem.getTitle());
        Log.d(TAG, "onCreate: "+newsItem.toString());
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(newsItem.getVideoUrl());
        videoView.start();
      ViewGroup viewGroup = findViewById(R.id.parentView);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+isFullScreen);
                if(!isFullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    ViewGroup vg = (ViewGroup) getWindow().getDecorView().getRootView();
                    viewGroup.removeView(videoView);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    videoView.setLayoutParams(layoutParams);
                    vg.addView(videoView);
                    isFullScreen = true;
                }
                else
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    videoView.setLayoutParams(layoutParams);
                    ((ViewGroup)videoView.getParent()).removeView(videoView);
                    viewGroup.addView(videoView);
                    isFullScreen = false;
                }

            }
        });
    }
}