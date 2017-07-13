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
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * A {@link Transition} based on {@link ChangeBounds}
 * that provides animation support between a circular
 * and rectangular ImageView (implemented as {@link TransitionImageView})
 * residing in two different activities.
 * Functionality is provided by {@link ImageTransitionCompatHelper}.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class ImageTransition extends Transition{

    public ImageTransition() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String[] getTransitionProperties() {
        return ImageTransitionCompatHelper.getTransitionProperties(super.getTransitionProperties());
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        ImageTransitionCompatHelper.captureValues(transitionValues.view, transitionValues.values);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        ImageTransitionCompatHelper.captureValues(transitionValues.view, transitionValues.values);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (endValues == null || startValues == null) {
            return null;
        }
        return ImageTransitionCompatHelper.createAnimator(endValues.view, startValues.values, endValues.values);
    }
}
