package com.example.movieinfo.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.StaticParameter;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

public class ImageDisplayActivity extends AppCompatActivity {

    private ImageView displayImg;
    private ScaleGestureDetector scaleGestureDetector;
    private Context context;

    // Define image scale factor
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        context = this;

        displayImg = findViewById(R.id.image_display);

        // Initialize scale gesture detector for zoom in and out for image
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // get imgUrl from intent
        String imgFilePath = getIntent().getStringExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_IMAGE_PATH_KEY);
        if (imgFilePath != null && !imgFilePath.isEmpty()) {

            // region Create image placeholder animation using shimmer

            // Initialize Shimmer Animation
            Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                    .setBaseColor(ContextCompat.getColor(context, R.color.gray))
                    .setBaseAlpha(1)
                    .setHighlightColor(ContextCompat.getColor(context, R.color.lightGray))
                    .setHighlightAlpha(1)
                    .setDropoff(50)
                    .build();

            // Initialize Shimmer Drawable - placeholder for image
            ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
            shimmerDrawable.setShimmer(shimmer);

            // endregion


            String imgUrl = StaticParameter.getImageUrl(StaticParameter.BackdropSize.ORIGINAL, imgFilePath);
            Glide.with(this)
                    .load(imgUrl)
                    .placeholder(shimmerDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_not_found)
                    .fitCenter()
                    .into(displayImg);
        } else {
            finish();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // Let the ScaleGestureDetector inspect all events.
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            // setting scale for image
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            float minScaleFactor = 1.0f;
            float maxScaleFactor = 10.0f;
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, maxScaleFactor));

            // set scale x and scale y to image view.
            displayImg.setScaleX(scaleFactor);
            displayImg.setScaleY(scaleFactor);
            return true;
        }
    }
}