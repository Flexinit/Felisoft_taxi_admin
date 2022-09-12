package com.example.drupp_driver.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class NoSwipeViewPager extends ViewPager {
    private boolean enabled;
    private Boolean mAnimStarted = false;

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public boolean canScrollHorizontally(int i) {
        return this.enabled && super.canScrollVertically(i);
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        if (!mAnimStarted && null != getAdapter()) {
//            int height = 0;
//            View child = ((FragmentStatePagerAdapter) getAdapter()).getItem(getCurrentItem()).getView();
//            if (child != null) {
//                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                height = child.getMeasuredHeight();
//                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT && height < getMinimumHeight()) {
//                    height = getMinimumHeight();
//                }
//            }
//
//            // Not the best place to put this animation, but it works pretty good.
//            int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//            if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight) {
//                final int targetHeight = height;
//                final int currentHeight = getLayoutParams().height;
//                final int heightChange = targetHeight - currentHeight;
//
//                Animation a = new Animation() {
//                    @Override
//                    protected void applyTransformation(float interpolatedTime, Transformation t) {
//                        if (interpolatedTime >= 1) {
//                            getLayoutParams().height = targetHeight;
//                        } else {
//                            int stepHeight = (int) (heightChange * interpolatedTime);
//                            getLayoutParams().height = currentHeight + stepHeight;
//                        }
//                        requestLayout();
//                    }
//
//                    @Override
//                    public boolean willChangeBounds() {
//                        return true;
//                    }
//                };
//
//                a.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                        mAnimStarted = true;
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        mAnimStarted = false;
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//                });
//
//                a.setDuration(1000);
//                startAnimation(a);
//                mAnimStarted = true;
//            } else {
//                heightMeasureSpec = newHeight;
//            }
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getAdapter() != null) {
            View child = ((FragmentStatePagerAdapter) getAdapter()).getItem(getCurrentItem()).getView();

            if (child != null) {
                child.measure(widthMeasureSpec, heightMeasureSpec);
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY));
            } else {
                View firstChild = getChildAt(0);
                firstChild.measure(widthMeasureSpec, heightMeasureSpec);
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(firstChild.getMeasuredHeight(), MeasureSpec.EXACTLY));
            }

        }


    }
}
