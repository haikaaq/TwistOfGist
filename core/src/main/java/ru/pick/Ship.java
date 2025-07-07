package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Ship extends Object {


    private static final float MAX_ROTATION_SPEED = 5f;  // Макс. скорость поворота (градусов/кадр)
    private static final float MAX_ROTATION_ANGLE = 70f; // Макс. угол отклонения (градусов)
    private long timeLastPhase;
    private long timePhaseInterval = 150;
    public float rotation = 0;
    public float rotationSpeed;
    private float vxAbs;

    //public float checkVx;
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

    public void changeShipPhase() {
        /*if (TimeUtils.millis() > timeLastPhase + timePhaseInterval) {
            timeLastPhase = TimeUtils.millis();
            phase++;
        }
        if (phase == nPhases) phase = 0;*/

        if (vX == 0) phase = 0;
        if (vxAbs > 0 && vxAbs <= 1.7f) phase = 1;
        if (vxAbs > 1.7f && vxAbs <= 2.9f) phase = 2;
        if (vxAbs > 2.9f && vxAbs <= 3.8f) phase = 3;
        if (vxAbs > 3.8f && vxAbs <= 4.9f) phase = 4;
        if (vxAbs > 4.9f && vxAbs <= 6f) phase = 5;
        if (vxAbs > 6.0f && vxAbs <= 6.8f) phase = 6;
        if (vxAbs > 6.8f) phase = 7;


    }

    public void touch(Vector3 touch) {

        vX = (touch.x - scrX() - 125) / 27;
        vY = (touch.y - scrY() - 125) / 70;

        vxAbs = Math.abs(vX);
        outOfScreen();

    }

    @Override
    public void move() {
        super.move();
        //if (vX>MaxVx)vX=MaxVx;
        vxAbs = Math.abs(vX);
        rotation -= rotationSpeed;
        rotation = MathUtils.clamp(rotation, -60f, 60f);


        updateRotatoin();
        changeShipPhase();
        outOfScreen();

    }

    public void updateRotatoin() {
        rotation = (vX * 2);
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












