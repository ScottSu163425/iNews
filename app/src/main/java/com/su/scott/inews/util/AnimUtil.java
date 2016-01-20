package com.su.scott.inews.util;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;

import com.su.scott.inews.R;

public class AnimUtil {
    private static final int DEFAULT_DURATION = 400;

    public static ObjectAnimator rotateY(View view, float from, float to, long duration) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "rotationY", from, to);
        oa.setInterpolator(new DecelerateInterpolator());
        oa.setDuration(duration).start();
        return oa;
    }

    public static ObjectAnimator rotateY(View view, float from, float to, long duration, TimeInterpolator interpolator) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "rotationY", from, to);
        oa.setInterpolator(interpolator);
        oa.setDuration(duration).start();
        return oa;
    }

    public static void rotateX(View view, float from, float to, long duration) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "rotationX", from, to);
        oa.setInterpolator(new DecelerateInterpolator());
        oa.setDuration(duration).start();
    }

    public static void rotateX(View view, float from, float to, long duration, TimeInterpolator interpolator) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "rotationX", from, to);
        if (null != interpolator) {
            oa.setInterpolator(interpolator);
        }
        oa.setDuration(duration).start();
    }

    public static Animation rotateSelf(View target, float from, float to, long duration) {
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        animation.setInterpolator(new DecelerateInterpolator());
        target.startAnimation(animation);

        return animation;
    }

    //
    public static Animation inEastOvershot(Context context, View target, long duration) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.in_east_overshot);
        a.setDuration(duration);
        target.startAnimation(a);
        return a;
    }

    public static void inWest(Context context, View target, long duration) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.in_west);
//        a.setInterpolator(new OvershootInterpolator());
        a.setDuration(duration);
        target.startAnimation(a);
    }

    public static Animation inWestOvershot(Context context, View target, long duration) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.in_west_overshot);
        a.setDuration(duration);
        target.startAnimation(a);
        return a;
    }

    /**
     * 左右晃动
     *
     * @return
     */
    public static void shake(View target) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        oa.setDuration(DEFAULT_DURATION);
        oa.start();
    }

    /**
     * 嗒哒！
     *
     * @param target
     */
    public static void tada(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleX", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleY", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(target, "rotation", 0, -3, -3, 3, -3, 3, -3, 3, -3, 0);
        set.playTogether(oa1, oa2, oa3);
        set.setDuration(1000);
        set.start();
    }

    /**
     * 橡皮筋
     *
     * @param target
     */
    public static void rubberBand(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleX", 1, 1.25f, 0.75f, 1.15f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleY", 1, 0.75f, 1.25f, 0.85f, 1);
        set.playTogether(oa1, oa2);
        set.setDuration(DEFAULT_DURATION);
        set.start();
    }

    /**
     * 单侧抖动（左）
     *
     * @param target
     */
    public static void shakeLeft(View target) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, "rotation", 0, 10, -10, 6, -6, 3, -3, 0);
        oa.setDuration(DEFAULT_DURATION);
        oa.start();
    }

    /**
     * 单侧抖动（右）
     *
     * @param target
     */
    public static void shakeRight(View target) {
        float x = (target.getWidth() - target.getPaddingLeft() - target.getPaddingRight()) / 2 + target.getPaddingLeft();
        float y = target.getHeight() - target.getPaddingBottom();

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "rotation", 12, -12, 3, -3, 0);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "pivotY", y, y, y, y, y);
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(target, "pivotX", x, x, x, x, x);
        set.playTogether(oa1, oa2, oa3);
        set.setDuration(DEFAULT_DURATION);
        set.start();
    }

    /**
     * 心跳
     *
     * @param target
     */
    public static void pulseBounce(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleY", 1, 1.05f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleX", 1, 1.05f, 1);
        set.playTogether(oa1, oa2);
        set.setDuration(1000);
        set.setInterpolator(new BounceInterpolator());
        set.start();
    }

    /**
     * 心跳
     *
     * @param target
     */
    public static void pulse(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleY", 1, 1.05f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleX", 1, 1.05f, 1);
        set.playTogether(oa1, oa2);
        set.setDuration(DEFAULT_DURATION);
        set.start();
    }

    /**
     * 心跳
     *
     * @param target
     */
    public static void bounceInRight(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "translationX", -target.getWidth(), 30, -10, 0);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1);
        set.playTogether(oa1, oa2);
        set.setDuration(DEFAULT_DURATION);
        set.start();
    }

    /**
     * 收缩下沉
     *
     * @param target
     */
    public static void shrink(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleY", 1, 0.95f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleX", 1, 0.95f, 1);
        set.playTogether(oa1, oa2);
        set.setDuration(DEFAULT_DURATION);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }

    public static void shake2(View target) {
        float width = target.getWidth();
        float one = (float) (width / 100.0);

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "translationX", 0 * one, -25 * one, 20 * one, -15 * one, 10 * one, -5 * one, 0 * one, 0);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "rotation", 0, -5, 3, -3, 2, -1, 0);
        set.playTogether(oa1, oa2);
        set.setDuration(DEFAULT_DURATION);
        set.start();
    }


    /*====================================================================================*/

    public static Animator animViewFadeIn(View paramView) {
        return animViewFadeIn(paramView, 200L, null);
    }

    public static Animator animRotation(View paramView, int duration) {
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "rotation", 0f, 360f);
        localObjectAnimator.setDuration(duration);
        localObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        localObjectAnimator.setRepeatMode(ValueAnimator.INFINITE);
        localObjectAnimator.start();
        return localObjectAnimator;
    }

    public static Animator animViewFadeIn(View paramView, long paramLong, Animator.AnimatorListener paramAnimatorListener) {
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "alpha", new float[]{0.0F, 1.0F});
        localObjectAnimator.setDuration(paramLong);
        if (paramAnimatorListener != null)
            localObjectAnimator.addListener(paramAnimatorListener);
        localObjectAnimator.start();
        return localObjectAnimator;
    }

    public static Animator animViewFadeOut(View paramView) {
        return animViewFadeOut(paramView, 200L, null);
    }

    public static Animator animViewFadeOut(View paramView, long paramLong, Animator.AnimatorListener paramAnimatorListener) {
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "alpha", new float[]{1.0F, 0.0F});
        localObjectAnimator.setDuration(paramLong);
        if (paramAnimatorListener != null)
            localObjectAnimator.addListener(paramAnimatorListener);
        localObjectAnimator.start();
        return localObjectAnimator;
    }

    /**
     * 将View的背景颜色更改，使背景颜色转换更和谐的过渡动画
     *
     * @param view       要改变背景颜色的View
     * @param startColor 上个颜色值
     * @param endColor   当前颜色值
     * @param duration   动画持续时间
     */
    public static void transformColor(View view, int startColor, int endColor, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofInt(view, "backgroundColor", new int[]{startColor, endColor});
        animator.setDuration(duration);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }

}
