package ru.pick;

import static ru.pick.Main.*;
import static ru.pick.ScreenGame.ShotCount;


public class Shot extends Object {
    public int type;


    public boolean isoverlab= false;



    public Shot(float x, float y) {
       super(x,y);



        width = 40;
        height = 50;
        vY = 16;
        vX= (type == 0) ? ShotCount : 0;




    }

    public boolean OutOfscreen() {
        return y >= SCR_HEIGHT - height / 2 || x >= SCR_WIDTH - width / 2 || x < width / 2;
    }

    public void move(float vX) {
        y += vY;

        x += vX;

    }

    public void move() {
        super.move();


    }


}

