package ru.pick;

import static ru.pick.Main.*;

import static ru.pick.ScreenGame.*;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends Object {
    public int type;

    public int health;
    public int phase, nPhases =12;
    private long timeLastPhase;
    private long timePhaseInterval=50;
    public long timeLastWouded;
    public boolean deahth=false;
    public boolean EmenyDead=false;
    public  boolean isWouded = false;



    public Enemy(){
        type=MathUtils.random(0,3);
      //  EnemyIsBoss= false;
         if (EnemyIsBoss){
           height= width=MathUtils.random(300f,400);
            vY=MathUtils.random(-0.2f,-0.09f);
           health= MathUtils.random(15*(ShotCount+1),25*(ShotCount+1));
           vX= MathUtils.random(-1.5f,1.5f);
         }

        else{width=height=MathUtils.random(100f,250);
        vX= MathUtils.random(-0.5000f,0.50000f);
             health= MathUtils.random(3*(ShotCount+1),11*(ShotCount+1));
             vY= MathUtils.random(-2.99f,-0.78f);}

        x=MathUtils.random(width/2,SCR_WIDTH-width/2);
        y=MathUtils.random(SCR_HEIGHT+height,SCR_HEIGHT+height+300);




    }
    public void changePhase(){
        if(TimeUtils.millis()> timeLastPhase+timePhaseInterval){
            timeLastPhase= TimeUtils.millis();
            phase++;}
        if(phase == nPhases) phase=0;

    }

    @Override
    public void move() {
        super.move();
        changePhase();
        OutOfScreen();
    }

    public boolean BelowTheScreen(){
        return y<=height/2;
    }
    public boolean AboveTheScreen(){
        return y>=SCR_WIDTH-height/2;
    }

    public void OutOfScreen(){
        if (x>=SCR_WIDTH-width/2) { x= SCR_WIDTH-width/2; vX-=2*vX;}

        if(x<width/2){ x=width/2;vX-=2*vX;}

    }

}
