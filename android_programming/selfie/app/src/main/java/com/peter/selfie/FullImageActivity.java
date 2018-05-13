package com.peter.selfie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_img);

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);

        Intent callingActivityIntent = getIntent();
        if (callingActivityIntent != null) {
            Uri imageUri = callingActivityIntent.getData();
            if (imageUri != null && fullScreenImageView != null) {
//                Glide.with(this).load(imageUri).into(fullScreenImageView);
                fullScreenImageView.setImageURI(imageUri);
            }
        }
    }
}