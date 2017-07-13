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


import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.omega_r.rounding_imageview.ImageTransitionValues;


public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RadioGroup mRadiogroup;
    private ImageView mImageView;

    private ImageView.ScaleType mEndScaleType;

    private String[] mScaleTypes = {
            RawScaleType.CENTER_CROP, RawScaleType.CENTER_INSIDE,
            RawScaleType.CENTER, RawScaleType.FIT_CENTER,
            RawScaleType.FIT_XY, RawScaleType.FIT_START,
            RawScaleType.FIT_END, RawScaleType.MATRIX
    };

    @Override
    protected int getTitleId() {
        return R.string.title_main_activity;
    }

    @Override
    protected Bundle getTransitionsArguments() {
        Bundle bundle = new Bundle();
        bundle.putFloat(ImageTransitionValues.KEY_IMAGE_START_ROUNDING, 0.5f);
        bundle.putFloat(ImageTransitionValues.KEY_IMAGE_END_ROUNDING, 0);
        bundle.putFloat(ImageTransitionValues.KEY_IMAGE_START_OFFSET, 0);
        bundle.putFloat(ImageTransitionValues.KEY_IMAGE_END_OFFSET, 0);
        return bundle;
    }

    @Override
    protected void updateTransitionsArguments(Bundle bundle) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRadiogroup = (RadioGroup) findViewById(R.id.radiogroup);

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setOnClickListener(this);

        Spinner startScaleTypeSpinner = (Spinner) findViewById(R.id.spinner_start_scaletype);
        Spinner endScaleTypeSpinner = (Spinner) findViewById(R.id.spinner_end_scaletype);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mScaleTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startScaleTypeSpinner.setAdapter(adapter);
        endScaleTypeSpinner.setAdapter(adapter);

        startScaleTypeSpinner.setOnItemSelectedListener(this);
        endScaleTypeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(AboutActivity.create(this));
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, getString(R.string.image));
                if (mRadiogroup.getCheckedRadioButtonId() == R.id.button_to_imageview) {
                    startActivity(ImageViewActivity.create(this, mEndScaleType), options.toBundle());
                } else {
                    startActivity(ViewPagerActivity.create(this, mEndScaleType), options.toBundle());
                }
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ImageView.ScaleType scaleType = ScaleTypeConverter.convert(mScaleTypes[position]);

        switch (parent.getId()) {
            case R.id.spinner_start_scaletype:
                mImageView.setScaleType(scaleType);
                break;
            case R.id.spinner_end_scaletype:
                mEndScaleType = scaleType;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    interface RawScaleType {
        String CENTER_CROP = "Center Crop";
        String CENTER_INSIDE = "Center Inside";
        String CENTER = "Center";
        String FIT_CENTER = "Fit Center";
        String FIT_XY = "Fit XY";
        String FIT_START = "Fit Start";
        String FIT_END = "Fit End";
        String MATRIX = "Matrix";
    }

    public static class ScaleTypeConverter {

        public static ImageView.ScaleType convert(String scaleType) {
            switch (scaleType) {
                case RawScaleType.CENTER_CROP:
                    return ImageView.ScaleType.CENTER_CROP;
                case RawScaleType.CENTER_INSIDE:
                    return ImageView.ScaleType.CENTER_INSIDE;
                case RawScaleType.CENTER:
                    return ImageView.ScaleType.CENTER;
                case RawScaleType.FIT_CENTER:
                    return ImageView.ScaleType.FIT_CENTER;
                case RawScaleType.FIT_END:
                    return ImageView.ScaleType.FIT_END;
                case RawScaleType.FIT_START:
                    return ImageView.ScaleType.FIT_START;
                case RawScaleType.FIT_XY:
                    return ImageView.ScaleType.FIT_XY;
                case RawScaleType.MATRIX:
                    return ImageView.ScaleType.MATRIX;
            }
            return null;
        }
    }

}
