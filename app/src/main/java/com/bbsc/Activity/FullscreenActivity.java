package com.bbsc.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bbsc.R;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class FullscreenActivity extends AppCompatActivity {


    private Animator currentAnimator;

    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        // calling the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        // showing the back button in action bar
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
        Intent callingActivityIntent = getIntent();
        if (callingActivityIntent != null) {
            Uri imageUri = callingActivityIntent.getData();
            if (imageUri != null && fullScreenImageView != null) {
                Glide.with(this)
                        .load(imageUri)
                        .into(fullScreenImageView);

            }
        }
    }

    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return true; }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}

