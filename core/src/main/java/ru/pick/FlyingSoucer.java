package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class FlyingSoucer extends Object {



    public int phase, nPhases =12;
    private long timeLastPhase;
    private long timePhaseInterval=150;



    public FlyingSoucer(float x, float y){
        super(x,y);
        width=700;
        height=600;
        vX=10;


    }




    public void changePhase(){
        if(TimeUtils.millis()> timeLastPhase+timePhaseInterval){
            timeLastPhase= TimeUtils.millis();
            phase++;}
        if(phase == nPhases) phase=0;

    }


    @Override
    public void move() {


    }
    public void moveflyingSauser() {
        vY+= MathUtils.random(-2f,2f);

        super.move();
        OutOfScreen();
        /*if(x<-width/2){ x=SCR_WIDTH+width/2;}
        if(x>SCR_WIDTH+width/2){ x=-width/2;}
        if(y<-height/2){ y=SCR_HEIGHT+height/2;}
        if(y>SCR_HEIGHT+height/2){ y=-height/2;}*/




    }

    public void OutOfScreen(){
        if (x>=SCR_WIDTH) { vX=-vX;}
        if (y+height/2>= SCR_HEIGHT) { vY=-vY;}
        if(y-height/2<0){ vY=-vY;}
        if(x<=0){ vX=-vX;}
    }

    public void stop(){
        vX=0;
        vY=0;
    }




}
















