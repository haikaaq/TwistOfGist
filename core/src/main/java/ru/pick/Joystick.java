package ru.pick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Joystick {
    private final Texture baseImg;
    private final Texture stickImg;
    private final Vector2 basePos;
    private final Vector2 stickPos;
    private final float baseRadius;
    private final float stickRadius;
    private boolean isActive;
    private int pointer = -1;

    public Joystick(Texture baseImg, Texture stickImg,
                    float centerX, float centerY,
                    float baseSize, float stickSize) {
        this.baseImg = baseImg;
        this.stickImg = stickImg;
        this.basePos = new Vector2(centerX, centerY);
        this.stickPos = new Vector2(centerX, centerY);
        this.baseRadius = baseSize / 2;
        this.stickRadius = stickSize / 2;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(baseImg, basePos.x - baseRadius, basePos.y - baseRadius,
            baseRadius * 2, baseRadius * 2);
        batch.draw(stickImg, stickPos.x - stickRadius, stickPos.y - stickRadius,
            stickRadius * 2, stickRadius * 2);
    }

    public Vector2 getDirection() {
        if (!isActive) return Vector2.Zero;

        Vector2 dir = new Vector2(
            (stickPos.x - basePos.x) / baseRadius,
            (stickPos.y - basePos.y) / baseRadius
        );

        // Ограничиваем длину вектора если палец далеко
        if (dir.len() > 1f) dir.nor();

        return dir;
    }

    public boolean touchDown(float touchX, float touchY, int pointer) {
        if (isActive) return false;

        // Проверяем попадание в зону джойстика
        if (Vector2.dst(touchX, touchY, basePos.x, basePos.y) <= baseRadius * 1.5f) {
            isActive = true;
            this.pointer = pointer;
            stickPos.set(touchX, touchY);
            return true;
        }
        return false;
    }

    public boolean touchDragged(float touchX, float touchY, int pointer) {
        if (!isActive || this.pointer != pointer) return false;

        Vector2 dir = new Vector2(touchX - basePos.x, touchY - basePos.y);
        float distance = dir.len();

        // Ограничиваем движение стика радиусом базы
        if (distance > baseRadius) {
            dir.setLength(baseRadius);
        }

        stickPos.set(basePos.x + dir.x, basePos.y + dir.y);
        return true;
    }

    public boolean touchUp(int pointer) {
        if (this.pointer == pointer) {
            isActive = false;
            stickPos.set(basePos); // Возвращаем стик в центр
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return isActive;
    }
}
