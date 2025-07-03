package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;
import static ru.pick.ScreenGame.shotCount;

import com.badlogic.gdx.math.MathUtils;

public class Enemy extends Object {


    public int health;


    public float rotation = 0;
    public float rotationSpeed;


    public Enemy() {
        type = MathUtils.random(0, 4);

        nPhases = 12;

        x = MathUtils.random(width / 2, SCR_WIDTH - width / 2);
        y = MathUtils.random(SCR_HEIGHT + height, SCR_HEIGHT + height + 300);
        isWouded = false;
        timeWoud = 154;
        imgWidth = imgHeight = 800;
        rotationSpeed = 0;
        width = height = MathUtils.random(180f, 250);
        vX = MathUtils.random(-0.5000f, 0.50000f);
        health = MathUtils.random(3 * (shotCount + 1), 11 * (shotCount + 1));
        vY = MathUtils.random(-2.99f, -0.78f);
    }


    @Override
    public void move() {

        super.move();

        changePhase();
        outOfScreen();

    }

    boolean BelowTheScreen() {
        return y <= height / 2;
    }

    boolean hit(float tx, float ty) {

        return scrX() < tx && tx < scrX() + width && ty > scrY() && ty < scrY() + height;
    }


}
