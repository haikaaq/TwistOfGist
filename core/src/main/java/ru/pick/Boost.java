package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Boost extends Object{

    public int type;

    public Boost() {
        width=height= MathUtils.random(100f,200);
        type=MathUtils.random(0,1);
        x=MathUtils.random(width/2,SCR_WIDTH-width/2);
        y=MathUtils.random(SCR_HEIGHT+height,2*SCR_HEIGHT);



        vY= MathUtils.random(-5f,-1f);
    }
    public boolean OutOfscreen(){
        return y<-height/2||x>=SCR_WIDTH-width/2||x<width/2;

    }
    public void move(){
        super.move();}
    public void stop(){
        super.stop();}


}



