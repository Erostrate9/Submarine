package com.example.submarine.fg;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.submarine.utils.ScreenUtil;

public class FgLayout extends FrameLayout {

    SUBView subView;
    boolean isStop=true;
    Context context;
    AttributeSet attrs;
    AnimatorSet animatorSet;

    public void start(){
        removeAllViews();
        subView = new SUBView(context,attrs);
        addView(subView,200,200);
//        subView.setY(800);
        subView.setY(ScreenUtil.getScreenHeight(context)/2);
        subView.setX(ScreenUtil.getScreenWidth(context)/4);
        isStop=false;
    }
    public void stop(){
        isStop=true;
        if (animatorSet!=null){
            animatorSet.cancel();

        }

    }
    public FgLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
//        this.start();
    }
    public void moveTo(float x,float y){
        if (isStop){
            return;
        }
        subView.setX(200);
        float dy=y-subView.getY();
        float dx=100;
        animatorSet = new AnimatorSet();
        //ValueAnimator的子类
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(subView,"rotation",subView.getRotation(), (float) Math.toDegrees(Math.atan(dy / dx)));
        animatorA.setDuration(100L);

//        //如果只有y变化,沿y轴移动
//        ObjectAnimator animatorB = ObjectAnimator.ofFloat(subView,"y",subView.getY(),y);
//        animatorA.start();

        //如果x,y都变化,沿x,y移动
        Path path = new Path();
        path.moveTo(subView.getX(),subView.getY());
        path.lineTo(x,y);
        ObjectAnimator animatorB = ObjectAnimator.ofFloat(subView,"x","y",path);
        animatorB.setDuration(100L);

        animatorSet.playTogether(animatorA, animatorB);
        animatorSet.start();


    }
}
