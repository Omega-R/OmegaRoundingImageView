package com.omega_r.rounding_imageview_sample;/*
 * Copyright 2016 Vikram Kakkar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.omega_r.rounding_imageview.ImageTransitionUtil;
import com.omega_r.rounding_imageview.ImageTransitionValues;

/**
 * This sample is based on:
 * <p>
 * http://stackoverflow.com/q/39749404
 * <p>
 * Sample code was provided by Simon:
 * <p>
 * https://github.com/Winghin2517/TransitionTest
 * <p>
 * Second Activity
 */
public class ImageViewActivity extends BaseActivity {

    private static final String EXTRA_SCALE_TYPE = "scaleType";

    public static Intent create(Context context, ImageView.ScaleType scaleType) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra(EXTRA_SCALE_TYPE, scaleType);
        return intent;
    }



    @Override
    protected int getTitleId() {
        return R.string.title_image_view_activity;
    }

    @Override
    protected Bundle getTransitionsArguments() {
        return null;
    }

    @Override
    protected void updateTransitionsArguments(Bundle bundle) {
        float startRounding = bundle.getFloat(ImageTransitionValues.KEY_IMAGE_START_ROUNDING);
        float endRounding = bundle.getFloat(ImageTransitionValues.KEY_IMAGE_END_ROUNDING);
        float startOffsetY = bundle.getFloat(ImageTransitionValues.KEY_IMAGE_START_OFFSET);
        float endOffsetY = bundle.getFloat(ImageTransitionValues.KEY_IMAGE_END_OFFSET);

        ImageTransitionValues values = new ImageTransitionValues(startOffsetY, endOffsetY, startRounding, endRounding);
        setEnterSharedElementCallback(ImageTransitionUtil.getCallback(values));
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
