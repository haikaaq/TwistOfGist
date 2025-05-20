package ru.pick;

public class Object {
    public float x , y;
    public float width,height;

    public float vX;
    public float vY;
    public boolean EnemyIsBoss;


    public Object(float x, float y) {
        this.x = x;
        this.y = y;

    }
    public Object() {


   }


    public float scrX(){
        return (x-width/2);
    }

    public float scrY(){
        return (y-height/2);
    }
    public void move(){
        x+=vX;
        y+=vY;


    }
    public void stop(){
        x+=0;
        y+=0;


    }
    public boolean overlab(Object o){

        return((Math.abs(x-o.x)<(width/2+o.width/2))&&( Math.abs(y-o.y)<(height/4+o.height/4))&&width!=0&&height!=0&&o.width!=0&&o.height!=0);



    }
    public boolean overlab(Object o, boolean a) {
        return ((Math.abs(x - o.x) < (a ? (width/4+o.width/4):(width/2+o.width/2)) && (Math.abs(y - o.y) < (height / 4 + o.height / 4)) && width != 0 && height != 0 && o.width != 0 && o.height != 0));
    }




}
