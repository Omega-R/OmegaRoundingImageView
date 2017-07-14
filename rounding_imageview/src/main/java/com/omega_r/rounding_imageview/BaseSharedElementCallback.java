package com.omega_r.rounding_imageview;

import android.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;
import java.util.Map;

public class BaseSharedElementCallback extends SharedElementCallback {
    private RoundingImageTransitionValues mValues;

    public BaseSharedElementCallback(RoundingImageTransitionValues values) {
        mValues = values;
    }

    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        View sharedElement = sharedElements.values().iterator().next();
        if (sharedElement instanceof ViewPager) {
            ViewPager view = (ViewPager) sharedElement;

            sharedElements.clear();

            View child = view.getChildAt(view.getCurrentItem());
            sharedElements.put(names.get(0), child);
        } else {
            super.onMapSharedElements(names, sharedElements);
        }
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);

        for (View sharedElement : sharedElements) {
            if (sharedElement instanceof RoundingImageView) {
                RoundingImageView imageView = (RoundingImageView) sharedElement;

                imageView.setRoundingProgress(mValues.getStartRounding());
                imageView.setImageOffsetY(mValues.getStartOffsetY());
            }
        }
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);

        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);

        for (View sharedElement : sharedElements) {
            if (sharedElement instanceof RoundingImageView) {
                RoundingImageView tiv = (RoundingImageView) sharedElement;

                if (tiv.getRoundingProgress() == mValues.getStartRounding()) {
                    tiv.setRoundingProgress(mValues.getEndRounding());
                    tiv.setImageOffsetY(mValues.getEndOffsetY());
                }
            }
        }
    }
}
