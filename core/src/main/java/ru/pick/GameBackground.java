package ru.pick;

import static ru.pick.Main.*;

public class GameBackground extends Object {

    public GameBackground(float x, float y) {
        super(x, y);
        width= SCR_WIDTH;
        height = SCR_HEIGHT * 3;
        vX=0;
        vY=-1;
    }

    @Override
    public void move() {
        super.move();
        if (y < -SCR_HEIGHT * 3) y = SCR_HEIGHT * 3 - 70;
    }
}
