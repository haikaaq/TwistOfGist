package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;
import static ru.pick.ScreenGame.shotCount;

import com.badlogic.gdx.math.MathUtils;

public class Boss extends Object {

    public int health;
    public long timePhaseInterval = 50;
    private long timeLastPhase;
    public boolean MenuEnemy = false;
    public boolean Dead = false;
    public float rotation = 0;
    public float rotationSpeed;
    private int spin;


    public Boss() {

        x = MathUtils.random(width / 2, SCR_WIDTH - width / 2);
        y = MathUtils.random(SCR_HEIGHT + height, SCR_HEIGHT + height + 300);
        type = 0;
        isWouded = false;
        timeWoud = 154;
        imgWidth = imgHeight = 450;
        spin = MathUtils.randomBoolean() ? -1 : 1;
        height = width = MathUtils.random(300f, 400);
        vY = MathUtils.random(-0.2f, -0.09f);
        health = MathUtils.random(15 * (shotCount + 1), 25 * (shotCount + 1));
        vX = MathUtils.random(-1.5f, 1.5f);
        nPhases = 6;
        if (Dead) {
            vY = MathUtils.random(2.9f, 9.5f);
        }


    }


    @Override
    public void move() {

        super.move();
        if (MenuEnemy) {
            y = SCR_HEIGHT / 3 + 80;
            width = height = SCR_WIDTH - 300;
            x = 12 * SCR_WIDTH / 15;
        }
        rotationSpeed = (isWouded) ? -2 * spin : 2 * spin;
        rotation += rotationSpeed;
        if (rotation >= 360) rotation = 0;
        if (!Dead && !MenuEnemy) {
            if (y < SCR_HEIGHT - 90) vY += MathUtils.random(-0.01f, 0.01f);

            if (y > SCR_HEIGHT - 90) vY = MathUtils.random(-1f, -0.5f);
            if (y < SCR_HEIGHT / 3) vY += MathUtils.random(0.9f, 1.8f);
        }
        if (Dead) rotationSpeed = 0;
        changePhase();
        if (!MenuEnemy) outOfScreen();
    }

    public boolean BelowTheScreen() {
        return y <= height / 4;
    }


    public void outOfScreen() {
        if (x >= SCR_WIDTH - width / 2) {
            x = SCR_WIDTH - width / 2;
            vX -= 2 * vX;
        }

        if (x < width / 2) {
            x = width / 2;
            vX -= 2 * vX;
        }

    }
}


