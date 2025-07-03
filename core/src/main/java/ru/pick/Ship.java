package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends Object {


    private static final float MAX_ROTATION_SPEED = 5f;  // Макс. скорость поворота (градусов/кадр)
    private static final float MAX_ROTATION_ANGLE = 70f; // Макс. угол отклонения (градусов)
    private long timeLastPhase;
    private long timePhaseInterval = 150;
    public float rotation = 0;
    public float rotationSpeed;


    public float CheckVx;
    private float acceleration = 0.2f;
    private float friction = 0.9f; // Трение (0.9 = 10% замедления)


    public Ship(float x, float y) {
        super(x, y);
        width = 350;
        height = 350;

        nPhases = 12;
        imgHeight = imgWidth = 800;


    }

    public void setVelocity(float vX, float vY) {
        this.vX = vX;
        this.vY = vY;
    }

    public void update(float delta) {


        // Постепенное замедление (если джойстик отпущен)
        vX *= friction;
        vY *= friction;

        // Если скорость очень мала — останавливаем корабль
        if (Math.abs(vX) < 0.01f) vX = 0;
        if (Math.abs(vY) < 0.01f) vY = 0;
    }

    public void changePhase() {
        if (TimeUtils.millis() > timeLastPhase + timePhaseInterval) {
            timeLastPhase = TimeUtils.millis();
            phase++;
        }
        if (phase == nPhases) phase = 0;

    }

    public void touch(Vector3 touch) {

        vX = (touch.x - scrX() - 125) / 27;
        vY = (touch.y - scrY() - 125) / 70;
        updateRotatoin();

        outOfScreen();

    }

    @Override
    public void move() {
        super.move();
        //if (vX>MaxVx)vX=MaxVx;

        rotation -= rotationSpeed;
        rotation = MathUtils.clamp(rotation, -80f, 80f);



        changePhase();
        outOfScreen();
        updateRotatoin();
    }

    public void updateRotatoin() {
        rotationSpeed = (vX - CheckVx) * 2;
        if (Math.abs(rotationSpeed) < 0.7f)
            rotationSpeed = MathUtils.clamp(rotationSpeed / 200, -0.7f, 0.7f);
    }


    public void stop() {
        vX = 0;
        vY = 0;
    }

    public void outOfScreen() {
        if (x >= SCR_WIDTH) {
            x = SCR_WIDTH;
        }
        if (y >= SCR_HEIGHT) {
            y = SCR_HEIGHT;
        }
        if (y < 0) {
            y = 0;
        }
        if (x < 0) {
            x = 0;
        }

    }


}












