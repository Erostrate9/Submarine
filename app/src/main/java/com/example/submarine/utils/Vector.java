package com.example.submarine.utils;

import static java.lang.Math.acos;
import static java.lang.Math.sqrt;

public class Vector {
    public float x,y;
    public Vector(float a,float b)
    {
        this.x = a;this.y = b;
    }
    public Vector(float degree)
    {
        float rad = (float)Math.toRadians(degree);
        this.x = (float)Math.cos(rad);
        this.y = (float)Math.sin(rad);
    }
    public Vector(float x0,float y0,float x1,float y1)
    {
        this.x = x1-x0;
        this.y = y1-y0;
    }
    public Vector(Vector a,Vector b)
    {
        this.x = b.x-a.x;
        this.y = b.y-a.y;
    }
    public Vector()
    {
        this.x = this .y = 0f;
    }
    public float len()
    {
        return (float)sqrt(x*x+y*y);
    }
    public static float Dotmul(Vector a,Vector b)
    {
        return a.x*b.x + a.y*b.y;
    }
    public static float Crossmul(Vector a,Vector b)
    {
        return a.x*b.y - b.x*a.y;
    }
    public void mulc(float c)
    {
        x*=c;y*=c;
    }
    public void gok(int k)
    {
        float sq = this.len();
        x = x /sq*(float)sqrt(k);
        y = y /sq*(float)sqrt(k);
    }
    public static float angle(Vector a,Vector b)
    {
        double theta = acos(Vector.Dotmul(a,b)/a.len()/b.len());
        if(Vector.Crossmul(b,a) < 0) theta*=-1;
        return (float)Math.toDegrees(theta);
    }
}
