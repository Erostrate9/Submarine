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
import com.example.submarine.utils.Vector;

public class FgLayout extends FrameLayout {

    public SUBView subView;
    boolean isStop=true;
    Context context;
    AttributeSet attrs;
    AnimatorSet animatorSet;
    float screenHeight;
    float screenWidth;
    AnimatorSet set = new AnimatorSet();
    public void start(){
        screenHeight=ScreenUtil.getScreenHeight(context);
        screenWidth=ScreenUtil.getScreenWidth(context);

        removeAllViews();
        subView = new SUBView(context,attrs);
        addView(subView,200,200);
//        subView.setY(800);
        subView.setY(screenHeight/2);
        subView.setX(screenWidth/4);
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
//        x=screenHeight/4;
//        if (isStop){
//            return;
//        }
//        float dy=y-subView.getY();
////        float dx=x-subView.getX();
//        float degree=(float) Math.toDegrees(Math.atan(dy / 200));
//        if (Math.abs(dy) <2){
//            degree=0;
//        }
//        animatorSet = new AnimatorSet();
//        //ValueAnimator的子类
//        ObjectAnimator animatorA = ObjectAnimator.ofFloat(subView,"rotation",subView.getRotation(), degree);
//        animatorA.setDuration(100L);
//
////        //如果只有y变化,沿y轴移动
//        ObjectAnimator animatorB = ObjectAnimator.ofFloat(subView,"y",subView.getY(),y);
//
//        //如果x,y都变化,沿x,y移动
////        Path path = new Path();
////        path.moveTo(subView.getX(),subView.getY());
////        path.lineTo(x,y);
////        ObjectAnimator animatorB = ObjectAnimator.ofFloat(subView,"x","y",path);
//        animatorB.setDuration(100L);
//
//        animatorSet.playTogether(animatorA, animatorB);
//        animatorSet.start();


        //xy
        x=300;
        if (isStop){
            return;
        }
        Vector a= new Vector(subView.getRotation());
        Vector b = new Vector(subView.getX(),subView.getY(),x,y);
        float theta=0;
        if (y-subView.getY()<0){
            theta=-15;
        }else if (y-subView.getY()>0){
            theta=15;
        }else{
            theta=0;
        }
//        theta =  Vector.angle(a,b);
        ObjectAnimator an = ObjectAnimator.ofFloat(subView,"rotation",subView.getRotation(),subView.getRotation()+theta);
        if (theta==0){
            an = ObjectAnimator.ofFloat(subView,"rotation",subView.getRotation(),0);
        }


        an.setDuration(80L);
        //an.start();
        Vector vv =new Vector(subView.getX(),subView.getY(),x,y);
        vv.gok(300);

        //xy移动
//        Path p = new Path();
//        p.moveTo(subView.getX(),subView.getY());
//        p.lineTo(subView.getX()+vv.x,subView.getY()+vv.y);
//        ObjectAnimator an2 = ObjectAnimator.ofFloat(subView,"x","y",p);

        //        //如果只有y变化,沿y轴移动
        ObjectAnimator an2 = ObjectAnimator.ofFloat(subView,"y",subView.getY(),y);
        an2.setDuration(50L);
        //如果x,y都变化,沿x,y移动
//        Path path = new Path();
//        path.moveTo(subView.getX(),subView.getY());
//        path.lineTo(x,y);
//        ObjectAnimator animatorB = ObjectAnimator.ofFloat(subView,"x","y",path);
//        animatorB.setDuration(100L);



        set = new AnimatorSet();
        set.playTogether(an,an2);
        set.start();

    }
}
