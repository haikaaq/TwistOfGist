package ru.pick;

import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.TimeUtils;

public class Object {
    public float x, y;
    public float width, height;

    public float vX;
    public float vY;

    public int type;
    public int phase;
    public int nPhases;
    public int imgWidth;
    public int imgHeight;
    public long timePhaseInterval = 50;
    private long timeLastPhase;
    public long timeLastWouded;
    public long timeWoud;
    public boolean isWouded;
    public boolean isoverlap;

    public Object(float x, float y) {
        this.x = x;
        this.y = y;

    }

    public Object() {


    }

    public int getAtlasX() {
        return 0;
    }

    public int getAtlasY() {
        return type * imgHeight;

    }

    public float scrX() {
        return (x - width / 2);
    }

    public float scrY() {
        return (y - height / 2);
    }

    public void move() {
        x += vX;
        y += vY;


    }

    public void stop() {
        x += 0;
        y += 0;


    }

    public boolean overlap(Object o) {

        return ((Math.abs(x - o.x) < (width / 2 + o.width / 2)) && (Math.abs(y - o.y) < (height / 4 + o.height / 4)) && width != 0 && height != 0 && o.width != 0 && o.height != 0);


    }

    boolean overlap(Object obj2, Pixmap p1, Pixmap p2) {

        if (Math.abs(x - obj2.x) > (width + obj2.width) / 2 ||
            Math.abs(y - obj2.y) > (height + obj2.height) / 2) {
            return false;
        } else {
            // 2. Определяем область пересечения в мировых координатах

            int overlapLeft = (int) Math.max(scrX(), obj2.scrX());
            int overlapRight = (int) Math.min((x + width / 2), (obj2.x + (obj2.width / 2)));
            int overlapBottom = (int) Math.max(scrY(), obj2.scrY());
            int overlapTop = (int) Math.min((y + (height / 2)), (obj2.y + (obj2.height / 2)));


            // 3. Проверяем пиксели с шагом 5 (оптимизация)
            for (int worldX = overlapLeft; worldX < overlapRight; worldX += 10) {
                for (int worldY = overlapBottom; worldY < overlapTop; worldY += 10) {


                    // Координаты в атласе
                    int u1 = (int) (Math.abs((worldX - (int) (scrX())) / width) * imgWidth);
                    int v1 = (int) (Math.abs((worldY - (int) (scrY())) / height) * imgHeight);
                    int atlasX1 = ((u1));
                    //int atlasY1 =p1.getHeight()-( ((4-type)*imgHeight )+(v1));
                    int atlasY1 = type * imgHeight + imgHeight - (v1);

                    // Координаты в атласе для obj2
                    int u2 = (int) (Math.abs((worldX - (int) (obj2.scrX())) / obj2.width) * obj2.imgWidth);
                    int v2 = (int) (Math.abs((worldY - (int) (obj2.scrY())) / obj2.height) * obj2.imgHeight);
                    int atlasX2 = ((u2));
                    // int atlasY2 = p2.getHeight()-(((4-obj2.type)*obj2.imgHeight )+  (v2));
                    int atlasY2 = obj2.type * obj2.imgHeight + imgHeight - (v2);


                    if (atlasX1 < 0 || atlasX1 >= p1.getWidth() ||
                        atlasY1 < 0 || atlasY1 >= p1.getHeight() ||
                        atlasX2 < 0 || atlasX2 >= p2.getWidth() ||
                        atlasY2 < 0 || atlasY2 >= p2.getHeight()) {
                        continue;  // Пропускаем невалидные пиксели
                    }

                    int pixel1 = p1.getPixel(atlasX1, atlasY1);
                    int alpha1 = (pixel1 >>> 24);
                    int pixel2 = p2.getPixel(atlasX2, atlasY2);
                    int alpha2 = (pixel2 >>> 24);

                    if ((alpha2) > 0 && (alpha1) > 0) {
                        return true;
                    }


                }

            }

        }
        return false;
    }

    public void changePhase() {
        if (TimeUtils.millis() > timeLastPhase + timePhaseInterval) {
            timeLastPhase = TimeUtils.millis();
            phase++;
        }
        if (phase == nPhases) phase = 0;

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
