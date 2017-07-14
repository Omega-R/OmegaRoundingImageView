package com.omega_r.rounding_imageview;

import android.animation.Animator;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * A {@link Transition} based on {@link Transition}
 * that provides animation support between a circular
 * and rectangular ImageView (implemented as {@link RoundingImageView})
 * residing in two different activities.
 * Functionality is provided by {@link RoundingImageTransitionHelper}.
 */

public class RoundingImageTransition extends Transition{

    public RoundingImageTransition() {
    }

    public RoundingImageTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String[] getTransitionProperties() {
        return RoundingImageTransitionHelper.getTransitionProperties(super.getTransitionProperties());
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        RoundingImageTransitionHelper.captureValues(transitionValues.view, transitionValues.values);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        RoundingImageTransitionHelper.captureValues(transitionValues.view, transitionValues.values);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (endValues == null || startValues == null) {
            return null;
        }
        return RoundingImageTransitionHelper.createAnimator(endValues.view, startValues.values, endValues.values);
    }
}
