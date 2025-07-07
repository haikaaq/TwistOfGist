package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Boost extends Object {

    public int num;
    public boolean isTrueNum;

    public Boost() {
        width = height = MathUtils.random(150f, 200);
        type = MathUtils.random(0, 1);
        x = MathUtils.random(width / 2, SCR_WIDTH - width / 2);
        y = MathUtils.random(SCR_HEIGHT + height, 2 * SCR_HEIGHT);
        vY = MathUtils.random(-5f, -1f);
        imgWidth = imgHeight = 400;
    }

    public Boost(int num, boolean isTrueNum) {
        width = height = MathUtils.random(150f, 200);
        this.num = num;

        type = 0;
        this.isTrueNum = isTrueNum;
        imgWidth = imgHeight = 400;
        x = MathUtils.random(width / 2, SCR_WIDTH - width / 2);
        y = MathUtils.random(SCR_HEIGHT + height, SCR_HEIGHT + 1 / 2 * SCR_HEIGHT);
        vY = -4f;
    }

    public boolean overlap(Object o) {

        return ((Math.abs(x - o.x) < (width / 4 + o.width / 4)) && (Math.abs(y - o.y) < (height / 5 + o.height / 5)) && width != 0 && height != 0 && o.width != 0 && o.height != 0);


    }
    public boolean OutOfscreen() {
        return y < -height / 2 || x >= SCR_WIDTH - width / 2 || x < width / 2;
    }

    public void move() {
        super.move();
    }

    public void stop() {
        super.stop();
    }


}



