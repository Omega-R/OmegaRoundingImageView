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

import com.omega_r.rounding_imageview.RoundingImageTransitionUtil;
import com.omega_r.rounding_imageview.RoundingImageTransitionValues;


public class ViewPagerActivity extends BaseActivity {

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
        float startRounding = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_START_ROUNDING);
        float endRounding = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_END_ROUNDING);
        float startOffsetY = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_START_OFFSET);
        float endOffsetY = bundle.getFloat(RoundingImageTransitionValues.KEY_IMAGE_END_OFFSET);

        RoundingImageTransitionValues values = new RoundingImageTransitionValues(startOffsetY, endOffsetY, startRounding, endRounding);

        setEnterSharedElementCallback(RoundingImageTransitionUtil.getCallback(values));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter() {
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
        viewPager.invalidate();
    }
}
