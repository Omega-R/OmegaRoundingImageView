# RoundingImageView

Walkthrough
-----------

<p align="center">
    <img src="https://github.com/Omega-R/OmegaRoundingImageView/blob/master/img/sample.gif?raw=true" width="300" height="533" />
</p>

Installation
------------

To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
**Step 2.** Add the dependency
```
dependencies {
    compile 'com.github.Omega-R:OmegaRoundingImageView:v1.0'
}
```

Usage
-----

Use com.omega_r.rounding_imageview.TransitionImageView in place of ImageView. The `rounding` value can be set using `app:rounding` attribute. Value must be within [0,1] - **0** for no rounding, **1** for perfect rounding. Set `android:transitionName` attribute.

```
<com.omega_r.rounding_imageview.TransitionImageView
    android:id="@+id/image"
    android:layout_width="80dp"
    android:layout_height="80dp"
    android:scaleType="centerCrop"
    android:transitionName="@string/iv_transition_name"
    app:tiv_rounding="0"/>
```    

Transition
----------

Provide `@transition/itl_image_transition` as the value for `android:windowSharedElementEnterTransition` & `android:windowSharedElementExitTransition` under your Activity theme in `styles.xml`:

```
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
	<item name="colorPrimary">...</item>
    <item name="colorPrimaryDark">...</item>
    <item name="colorAccent">...</item>

    ....
    ....

    <!-- @transition/itl_image_transition is provided by ImageTransition library -->
    <item name="android:windowSharedElementEnterTransition">@transition/itl_image_transition</item>
    <item name="android:windowSharedElementExitTransition">@transition/itl_image_transition</item>
</style>
```

To transfer transition values between activity's you should extent `BaseTransitionActivity` and override `Bundle getTransitionsArguments()` & `void updateTransitionsArguments(Bundle bundle)`:

```
public class SecondActivity extends BaseTransitionActivity {

    ....
    ....

    /*If transition starts from this Activity,
     you need to return Bundle with start and end transition parameters (see sample)*/

    @Override
    protected Bundle getTransitionsArguments() {
        return null;
    }

    /*If transition ends in this Activity,
    you need to create RoundingImageTransitionValues and set it to callback (see sample) */

    @Override
    protected void updateTransitionsArguments(Bundle bundle) {
        ...
        setEnterSharedElementCallback(RoundingImageTransitionUtil.getCallback(values));
    }

    ....
    ....

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
```

The sample app shows this approach.

If you'd like to change the duration of the transition, or use the transition within your own set of transitions, or use a different interpolator, include the following:

```
<transitionSet
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:interpolator/decelerate_cubic"
    android:duration="500"> //Set duration

    // Provides bounds transition
    <changeBounds/>

    // Provides transition between different scale types
    <changeImageTransform/>

    // Provides rounding transition
    <transition class="com.omega_r.rounding_imageview.RoundingImageTransition"/>

</transitionSet>
```

The library declares its min SDK version as 21

API version requirements
------------------------
For image transition between activities, version 21 or above is required.

License
-------
`Copyright 2017 Omega-R

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.`