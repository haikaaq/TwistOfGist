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
    private long timePhaseInterval=150;
    public float rotation=0;
    public float rotationSpeed;

    public  float CheckVx;





    public Ship(float x, float y){
        super(x,y);
        width=350;
        height=350;

        nPhases =12;
        imgHeight= imgWidth=800;




    }









    public void changePhase(){
        if(TimeUtils.millis()> timeLastPhase+timePhaseInterval){
            timeLastPhase= TimeUtils.millis();
            phase++;}
        if(phase == nPhases) phase=0;

    }

    public void touch( Vector3 touch){

        vX= (touch.x-scrX()-125)/27;
        vY= (touch.y-scrY()-125)/70;
        OutOfScreen();

    }
    @Override
    public void move() {
        super.move();
        //if (vX>MaxVx)vX=MaxVx;
        rotationSpeed=(vX-CheckVx)*2;

        rotation-=rotationSpeed;
        rotationSpeed = MathUtils.clamp(rotationSpeed, -MAX_ROTATION_SPEED, MAX_ROTATION_SPEED);

// Применяем поворот с ограничением угла
        float newRotation = rotation - rotationSpeed;
        float baseRotation = 0f; // Базовый угол (прямо по курсу)

// Ограничиваем отклонение от базового угла
        if (Math.abs(newRotation - baseRotation) > MAX_ROTATION_ANGLE) {
            newRotation = baseRotation + (MAX_ROTATION_ANGLE * Math.signum(newRotation - baseRotation));
        }
        rotation = newRotation;

        changePhase();
        OutOfScreen();
    }




    public void stop(){
        vX=0;
        vY=0;
    }
    public void OutOfScreen(){
        if (x>=SCR_WIDTH) { x= SCR_WIDTH;}
        if (y>= SCR_HEIGHT) { y= SCR_HEIGHT;}
        if(y<0){ y=0;}
        if(x<0){ x=0;}
    }



}














