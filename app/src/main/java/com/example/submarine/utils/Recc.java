package com.example.submarine.utils;

public class Recc {
    public Vector[] a;
    public Recc(Vector q,Vector w,Vector e,Vector r)
    {
        a[0] = new Vector(q.x,q.y);
        a[1] = new Vector(w.x,w.y);
        a[2] = new Vector(e.x,e.y);
        a[3] = new Vector(r.x,r.y);
    }
    public Recc()
    {
        a[0] = new Vector();
        a[1] = new Vector();
        a[2] = new Vector();
        a[3] = new Vector();
    }
    public static Boolean safee(Recc r1,Recc r2)
    {
        for(int i = 0;i <4;i++)
            if(inn(r1.a[i],r2)) return false;
        for(int i = 0;i <4;i++)
            if(inn(r2.a[i],r1)) return false;
        return true;
    }
    public static Boolean inn(Vector v,Recc r)
    {
        int bu[] = {0,0};
        for(int i = 0;i < 4;i++)
        {
            if(Vector.Crossmul(new Vector(r.a[i],v),new Vector(v,r.a[(i+1)%4])) > 0)
            bu[0]++;
            else bu[1]++;
        }
        if(bu[0]*bu[1] != 0) return true;
        return false;
    }
}
