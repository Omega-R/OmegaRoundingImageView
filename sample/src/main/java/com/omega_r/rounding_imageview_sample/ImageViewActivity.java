package com.omega_r.rounding_imageview_sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.omega_r.rounding_imageview.RoundingImageTransitionUtil;
import com.omega_r.rounding_imageview.RoundingImageTransitionValues;

public class ImageViewActivity extends BaseActivity {

    private static final String EXTRA_SCALE_TYPE = "scaleType";

    public static Intent create(Context context, ImageView.ScaleType scaleType) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra(EXTRA_SCALE_TYPE, scaleType);
        return intent;
    }

    @Override
    protected Bundle getTransitionsArguments() {
        return null;
    }

    @Override
    protected void updateTransitionsArguments(Bundle bundle) {
        float startRounding = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_START_ROUNDING);
        float endRounding = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_END_ROUNDING);
        float startOffsetY = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_START_OFFSET);
        float endOffsetY = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_END_OFFSET);

        RoundingImageTransitionValues values = new RoundingImageTransitionValues(startOffsetY, endOffsetY, startRounding, endRounding);
        setEnterSharedElementCallback(RoundingImageTransitionUtil.getCallback(values));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        ImageView.ScaleType scaleType = (ImageView.ScaleType) getIntent().getSerializableExtra(EXTRA_SCALE_TYPE);

        imageView.setScaleType(scaleType);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
