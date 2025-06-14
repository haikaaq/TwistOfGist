package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenNumLevel implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;

    private long beginingTime, screenTime = 3200;
    private Levels.Level curentlevel;
    public int level;
    SpaceButton btnNumLevel;
    SpaceButton btnLevel;
    Texture imgBG;

    public ScreenNumLevel(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        font = main.font70;
        loadLevel(main.level);

        level = main.level;
        btnNumLevel = new SpaceButton(font, curentlevel.numLevel, 800);
        btnLevel = new SpaceButton(font, LanguageManager.get("level") + " " + main.level, 1350);
        imgBG = new Texture("GrayBG.png");
    }

    public void show() {
        beginingTime = TimeUtils.millis();
    }

    private void loadLevel(int level) {
        curentlevel = Levels.LEVELS[level];
    }

    public void render(float delta) {
        level = main.level;
        loadLevel(level);

        if (TimeUtils.millis() - beginingTime > screenTime) {
            main.setScreen(main.screenGame);
            return;
        }
        btnNumLevel.changeText(LanguageManager.get(curentlevel.numLevel));
        btnLevel.changeText(LanguageManager.get("level") + " " + level);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnNumLevel.font.draw(batch, btnNumLevel.text, 45, btnNumLevel.y, SCR_WIDTH - 90, Align.center, true);
        btnLevel.font.draw(batch, LanguageManager.get("level") + " " + main.level, 45, btnLevel.y, SCR_WIDTH - 90, Align.center, true);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
