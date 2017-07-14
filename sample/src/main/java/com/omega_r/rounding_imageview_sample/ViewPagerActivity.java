package com.omega_r.rounding_imageview_sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.omega_r.rounding_imageview.ImageTransitionUtil;
import com.omega_r.rounding_imageview.ImageTransitionValues;


public class ViewPagerActivity extends BaseTransitionActivity {

    private ViewPager mViewPager;

    private static final String EXTRA_SCALE_TYPE = "scaleType";

    public static Intent create(Context context, ImageView.ScaleType scaleType) {
        Intent intent = new Intent(context, ViewPagerActivity.class);
        intent.putExtra(EXTRA_SCALE_TYPE, scaleType);
        return intent;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_transition_image, container, false);

                ImageView.ScaleType scaleType = (ImageView.ScaleType) getIntent().getSerializableExtra(EXTRA_SCALE_TYPE);
                view.setScaleType(scaleType);

                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mViewPager.invalidate();
    }
}
