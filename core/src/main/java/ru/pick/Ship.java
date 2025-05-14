package ru.pick;
import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends Object {



    public int phase, nPhases =12;
    private long timeLastPhase;
    private long timePhaseInterval=150;



    public Ship(float x, float y){
        super(x,y);
        width=250;
        height=250;


    }




    public void changePhase(){
        if(TimeUtils.millis()> timeLastPhase+timePhaseInterval){
            timeLastPhase= TimeUtils.millis();
            phase++;}
        if(phase == nPhases) phase=0;

    }

    public void touch( Vector3 touch){


        vX= (touch.x-scrX()-125)/50;
        vY= (touch.y-scrY()-125)/80;
        OutOfScreen();

    }
    @Override
    public void move() {
        super.move();

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














