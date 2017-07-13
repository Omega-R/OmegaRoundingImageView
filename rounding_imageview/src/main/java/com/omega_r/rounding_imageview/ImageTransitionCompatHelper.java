package com.omega_r.rounding_imageview;/*
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


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Provides functionality for {@link ImageTransition}
 * and {@link ImageTransitionCompat}.
 */
class ImageTransitionCompatHelper {

    private static final String PROPNAME_ROUNDING_PROGRESS = "roundImage:roundingProgress";
    private static final String PROPNAME_SCALE_TYPE = "roundImage:scaleType";
    private static final String PROPNAME_END_SCALE_TYPE = "roundImage:endScaleType";
    private static final String PROPNAME_OFFSET_X = "roundImage:offsetX";
    private static final String PROPNAME_OFFSET_Y = "roundImage:offsetY";

    static String[] getTransitionProperties(String[] parentTransitionProperties) {
        String[] properties = {PROPNAME_ROUNDING_PROGRESS, /*PROPNAME_SCALE_TYPE, PROPNAME_END_SCALE_TYPE,*/ PROPNAME_OFFSET_X, PROPNAME_OFFSET_Y};

        if (parentTransitionProperties == null || parentTransitionProperties.length == 0) {
            return properties;
        }

        ArrayList<String> list = new ArrayList<>(Arrays.asList(parentTransitionProperties));
        Collections.addAll(list, properties);

        return list.toArray(new String[list.size()]);
    }

    static void captureValues(View view, Map<String, Object> values) {
        if (view instanceof TransitionImageView) {
            TransitionImageView transitionImageView = (TransitionImageView) view;
            values.put(PROPNAME_ROUNDING_PROGRESS,
                    transitionImageView.getRoundingProgress());

//            values.put(PROPNAME_SCALE_TYPE, transitionImageView.getScaleType());
//            values.put(PROPNAME_END_SCALE_TYPE, transitionImageView.getTransitionEndScaleType());

            values.put(PROPNAME_OFFSET_X, transitionImageView.getImageOffsetX());
            values.put(PROPNAME_OFFSET_Y, transitionImageView.getImageOffsetY());
        }
    }

    static Animator createAnimator(final View endValuesView,
                                   Map<String, Object> startValues,
                                   Map<String, Object> endValues) {

        if (endValuesView instanceof TransitionImageView) {
            Float startRoundingProgress = (Float) startValues.get(PROPNAME_ROUNDING_PROGRESS);
            Float endRoundingProgress = (Float) endValues.get(PROPNAME_ROUNDING_PROGRESS);

            Float startOffsetX = (Float) startValues.get(PROPNAME_OFFSET_X);
            Float startOffsetY = (Float) startValues.get(PROPNAME_OFFSET_Y);

            Float endOffsetX = (Float) endValues.get(PROPNAME_OFFSET_X);
            Float endOffsetY = (Float) endValues.get(PROPNAME_OFFSET_Y);

            final TransitionImageView view = (TransitionImageView) endValuesView;

            final ObjectAnimator roundingProgressAnimator = ObjectAnimator.ofFloat(endValuesView,
                    TransitionImageView.ROUNDING_PROGRESS_PROPERTY,
                    startRoundingProgress == null ? 0 : startRoundingProgress, endRoundingProgress == null ? 0 : endRoundingProgress);

            ValueAnimator offsetXAnimator = ValueAnimator.ofFloat(startOffsetX, endOffsetX);
            offsetXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setImageOffsetX((Float) animation.getAnimatedValue());
                }
            });

            ValueAnimator offsetYAnimator = ValueAnimator.ofFloat(startOffsetY, endOffsetY);
            offsetYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setImageOffsetY((Float) animation.getAnimatedValue());
                }
            });

            AnimatorSet set = new AnimatorSet();
            set.playTogether(roundingProgressAnimator, offsetXAnimator, offsetYAnimator);
            return set;
        }
        return null;
    }
}
