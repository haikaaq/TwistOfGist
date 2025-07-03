package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;


public class Rocket extends Object {
    private final Vector2 position = new Vector2();
    private final Vector2 velocity = new Vector2();
    private float rotation;


    private float speed = 370f;


    // Для плавного изменения направления
    private float targetAngle;
    private float currentAngle;
    private float changeTimer = 0f;
    private final Random random = new Random();

    public Rocket(float startX, float startY) {
        position.set(startX, startY);
        // Начальное направление - случайное, но гарантированно в пределах экрана
        currentAngle = MathUtils.random(30f, 330f) * MathUtils.degreesToRadians;
        targetAngle = currentAngle;
        updateVelocity();
        imgWidth = 500;
        imgHeight = 500;
        type = 0;
        width = 220;
        height = 220;
    }

    public void update(float delta) {
        // Плавное изменение направления
        changeTimer += delta;
        if (changeTimer > 1f) { // Меняем направление каждые 1 секунды
            changeTimer = 0f;
            generateNewTargetAngle();
        }

        currentAngle = MathUtils.lerpAngle(currentAngle, targetAngle, delta * 2f);
        updateVelocity();

        // Предварительное вычисление новой позиции
        float newX = position.x + velocity.x * delta;
        float newY = position.y + velocity.y * delta;

        // Коррекция у границ
        if (newX < 50f || newX > SCR_WIDTH || newY < 0f || newY > SCR_HEIGHT) {
            generateSafeTargetAngle();
        }

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        x = position.x;
        y = position.y;
        rotation = velocity.angleDeg();
    }

    private void generateNewTargetAngle() {
        // Случайное изменение направления (в пределах 90° от текущего)
        float angleChange = MathUtils.random(-0.6f, 0.6f) * MathUtils.PI;
        targetAngle = currentAngle + angleChange;
    }

    private void generateSafeTargetAngle() {
        // Вычисляем направление к центру экрана
        Vector2 toCenter = new Vector2(SCR_WIDTH / 2f - position.x, SCR_WIDTH / 2f - position.y).nor();
        float centerAngle = toCenter.angleRad();

        // Случайное отклонение ±60° от направления к центру
        targetAngle = centerAngle + MathUtils.random(-1f, 1f) * MathUtils.PI / 3f;
    }

    private void updateVelocity() {
        velocity.set(speed, 0).rotateRad(currentAngle);

    }

    public boolean overlapQ(Object o) {

        return ((Math.abs(x - o.x) < (width / 2 + o.width / 2)) && (Math.abs(y - o.y) < (height / 3 + o.height / 3)) && width != 0 && height != 0 && o.width != 0 && o.height != 0);


    }

    // Геттеры
    public float getX() {
        return position.x - width / 2;
    }

    public float getY() {
        return position.y - height / 2;
    }

    public float getRotation() {
        return rotation;
    }
}
