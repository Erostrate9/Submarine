package com.example.submarine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.submarine.fg.FgLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

    }

    public void btnMove(View view) {
//        float max=barView.barHeight/2-barView.margin-100;
//        float min=-barView.barHeight/2+barView.margin+50;
//        barView.y=(float)Math.random()*(max-min)+min;
        Toast.makeText(this, "开始游戏", Toast.LENGTH_SHORT).show();
        FgLayout fgLayout = findViewById(R.id.FgLayout);
        fgLayout.start();
    }
}