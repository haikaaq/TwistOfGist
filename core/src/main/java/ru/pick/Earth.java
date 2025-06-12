package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

public class Earth extends Object{
    public Earth(float x, float y) {
        super(x, y);

        vX=0;
        vY=0;
        x=SCR_WIDTH/2;
        y=1120;
        nPhases=1;
        width=height=850;

    }

    @Override
    public void move() {
        super.changePhase();

    }
}


