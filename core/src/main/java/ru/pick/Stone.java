package ru.pick;

import com.badlogic.gdx.math.MathUtils;

public class Stone extends Object {
    public float rotation;

    public Stone(float x, float y) {
        super(x, y);


        type = 0;
        width = height = MathUtils.random(100f, 450f);
        rotation = MathUtils.random(0.1f, 300f);
        imgHeight = imgWidth = 500;


    }

    @Override
    public void move() {

        vY = -0.7f;
        super.move();
        super.changePhase();

    }

    public boolean OfScreen() {
        if (y < height / 2) return true;
        else return false;


    }


}
