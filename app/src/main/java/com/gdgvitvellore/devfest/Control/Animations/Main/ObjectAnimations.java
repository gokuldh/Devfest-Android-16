package com.gdgvitvellore.devfest.Control.Animations.Main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

/**
 * Created by Prince Bansal Local on 10/10/2016.
 */

public class ObjectAnimations {



    public static enum Position{UP,DOWN};

    public static Animator drawerArrowAnimator(View v, Position p){
        float start=0,end=0;
        if(p==Position.UP){
            start=0;
            end=180;
        }else if(p==Position.DOWN){
            start=180;
            end=0;
        }
        ObjectAnimator animator=ObjectAnimator.ofFloat(v,"rotation",start,end);
        animator.setDuration(400);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }
    public static Animator fragmentHolderAnimator(LinearLayout fragmentHolder, int fromY, int toY, int delay) {
        ObjectAnimator holderAnimator=ObjectAnimator.ofFloat(fragmentHolder,"translationY",fromY,toY);
                /*fragmentHolder.animate()
                        .translationYBy(drawerHeight)*/
        holderAnimator.setStartDelay(delay);
        holderAnimator.setInterpolator(new OvershootInterpolator());
        holderAnimator.setDuration(500);
        return holderAnimator;
    }

}
