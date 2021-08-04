package com.example.submarine.background;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.submarine.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BgLayout extends FrameLayout {
    Context context;
    AttributeSet attrs;
    Timer timer;
    List<ValueAnimator>  animatorList = new ArrayList<>();

    public BgLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
//        start();
//        createBar();
    }

    public void start(){
        //清除所有的障碍物
        removeAllViews();
        //定时任务
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //添加到消息队列
                post(new Runnable() {
                    @Override
                    public void run() {
                        createBar();
                    }
                });
            }
        },1000L,3500L);
    }

    public void stop(){
        if(timer!=null){
            timer.cancel();
        }
        for (ValueAnimator animator: animatorList){
            if (animator != null){
                animator.cancel();
            }
        }
    }

    public void createBar(){
        BarView barView = new BarView(context,attrs);
        addView(barView);
        float max=barView.barHeight/2-barView.margin-100;
        float min=-barView.barHeight/2+barView.margin+50;
        barView.y_offset=(float)Math.random()*(max-min)+min;
        barView.y_top=-barView.barHeight/2 -barView.margin+barView.y_offset;
        barView.y_bottom=barView.barHeight/2 +barView.margin+barView.y_offset;
        //Animation
        ValueAnimator animator = ValueAnimator.ofFloat(ScreenUtil.getScreenWidth(context),-barView.barWidth);
        animator.setInterpolator(new LinearInterpolator());//Uniform motion
        animator.setDuration(5000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                barView.x = (Float) animation.getAnimatedValue();
                barView.postInvalidate();//Forced refresh to avoid trailing images

                if (barView.x <= -barView.barWidth) {
                    removeView(barView);
                    animatorList.remove(animator);
                }
            }
        });
        animator.start();
        animatorList.add(animator);
    }
}
