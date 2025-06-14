package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends Object {
    int type1;
    int type2;

    public Fragment(float x, float y) {
        super(x, y);
        type1 = MathUtils.random(0, 3);
        type2 = MathUtils.random(0, 3);
        width = height = MathUtils.random(20f, 120);
        vX = MathUtils.random(-4f, 4);
        vY = MathUtils.random(-4f, 4);
    }

    public boolean OutOfscreen() {
        return y >= SCR_HEIGHT - height / 2 || y < -height / 2 || x >= SCR_WIDTH - width / 2 || x < width / 2;
    }

    public void move() {
        super.move();
    }


}
