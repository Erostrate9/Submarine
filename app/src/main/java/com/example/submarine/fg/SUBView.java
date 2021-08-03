package com.example.submarine.fg;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.submarine.R;

import java.util.Timer;
import java.util.TimerTask;

public class SUBView extends AppCompatImageView {


    public SUBView(@NonNull  Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int con[]={R.drawable.boat_000,R.drawable.boat_002};
        final int[] k = {0};
        setImageResource(R.drawable.boat_000);
        Timer timer=new Timer();
        final boolean[] pic = {false};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        //两张图片切换
                        setImageResource(con[k[0]]);
                        k[0] =(k[0] +1)%2;
                    }
                });
            }
        },500L,500L);
    }
}
