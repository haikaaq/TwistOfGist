package ru.pick;

import static ru.pick.Main.GAME;
import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;
import static ru.pick.Main.gameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;


public class ScreenAbout implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    private BitmapFont font32;
    Texture imgBG0;
    Texture imgLevels;
    Texture imgWarring;
    TextureRegion[][] imgLevel = new TextureRegion[2][2];
    ;
    Texture imgBackAtlas;
    TextureRegion[] imgBack = new TextureRegion[2];
    boolean slide0 = true;
    boolean slide1 = false;
    private boolean iswarring = false;
    private long timeWarring = 1100, timeLastWarring;

    private Levels.Level curentlevel;
    public float speedSliding = 10;
    public float xBG0 = 0, xHistory = 60, minButtonX = 50, maxButtonX = SCR_WIDTH + 50;

    SpaceButton btnBack;
    SpaceButton btnRight;
    SpaceButton btnLeft;

    List<SpaceButton> levels = new ArrayList<>();


    public ScreenAbout(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font70;
        font32 = main.font32;
        imgBG0 = new Texture("bgabout.png");
        imgWarring = new Texture("warring.png");

        imgBackAtlas = new Texture("buttonsLeftRight.png");
        imgLevels = new Texture("levels.png");


        btnBack = new SpaceButton(10, 1500, 90, 90, 0);
        btnRight = new SpaceButton(SCR_WIDTH - 110, 80, 100, 100, 1);
        btnLeft = new SpaceButton(10, 80, 100, 100, 0);

        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }
        for (int j = 0; j < imgLevel.length; j++) {
            for (int i = 0; i < imgLevel[j].length; i++) {

                imgLevel[j][i] = new TextureRegion(imgLevels, (j) * 400, (i) * 400, 400, 400);
            }
        }
        spavnButtons();

    }


    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        warring();

        if (slide0) changeSlideMinus();
        if (slide1) changeSlidePlus();


        int j = 0;
        for (SpaceButton a : levels) {

            if (j <= main.level) a.type = 0;
            else a.type = 1;
            j++;
        }


        updateButtonsLanguage();
        Vector3 mousepose = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousepose);
        for (int i = 0; i < levels.size(); i++) {
            levels.get(i).changePhases(2);
            levels.get(i).buttonsState(mousepose.x, mousepose.y);
        }



        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (btnBack.hit(touch.x, touch.y)) {
                main.setScreen(main.screenMenu);
                slide0 = true;
                slide1 = false;
            }
            if (btnRight.hit(touch.x, touch.y)) {

                if (slide0) {


                    slide1 = true;
                    slide0 = false;

                }

            }
            if (btnLeft.hit(touch.x, touch.y)) {

                if (slide1) {

                    slide0 = true;
                    slide1 = false;

                }

            }
            for (int i = 0; i < levels.size(); i++) {
                if (levels.get(i).hit(touch.x, touch.y)) {
                    if (levels.get(i).type == 0) {
                        if (gameState != GAME) {
                            if (main.level != i) {
                                main.isAboutLevel = true;
                                main.aboutLevel = i;
                                main.setScreen(main.screenNumLevel);
                            } else {
                                main.setScreen(main.screenNumLevel);
                            }
                        } else {
                            iswarring = true;
                            timeLastWarring = TimeUtils.millis();
                        }
                    }
                }


            }

        }


        batch.setProjectionMatrix(camera.combined);
        batch.begin();


            batch.draw(imgBG0, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        font.draw(batch, LanguageManager.get("history"), xHistory, SCR_HEIGHT - 183, SCR_WIDTH - 120, Align.center, true);

        int i = 0;
        for (SpaceButton a : levels) {

            batch.draw(imgLevel[a.type][a.phase], a.imgX, a.imgY, a.imgWidth, a.imgHeight);
            if (a.type == 0)
                font.draw(batch, "" + i, (i < 10 ? a.imgX + a.imgWidth / 2 - 10 : a.imgX + a.imgWidth / 2 - 25), a.imgY + a.imgHeight / 2 + 30);

            i += 1;
        }


        if (slide0) {
            batch.draw(imgBack[btnRight.type], btnRight.imgX, btnRight.imgY, btnRight.imgWidth, btnRight.imgHeight);
        }
        if (slide1) {
            batch.draw(imgBack[btnLeft.type], btnLeft.imgX, btnLeft.imgY, btnLeft.imgWidth, btnLeft.imgHeight);
        }


        if (iswarring) {
            batch.draw(imgWarring, 150, 1410, 600, 170);

            font32.draw(batch, LanguageManager.get("completecurrentgame"), 291, 1536, 400, Align.center, true);


        }
        batch.draw(imgBack[btnBack.type], btnBack.imgX, btnBack.imgY, btnBack.imgWidth, btnBack.imgHeight);


        batch.end();
    }

    private void updateButtonsLanguage() {


    }

    public void changeSlidePlus() {


        if (xHistory > -SCR_WIDTH - 100) xHistory -= speedSliding;
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).imgX > minButtonX + i % 3 * 300) levels.get(i).imgX -= speedSliding;

        }


    }

    public void changeSlideMinus() {


        if (xHistory < 50) xHistory += speedSliding;
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).imgX < maxButtonX + i % 3 * 300) levels.get(i).imgX += speedSliding;

        }


    }

    public void spavnButtons() {
        for (int i = 0; i < 3; i++) {
            levels.add(new SpaceButton(maxButtonX + i * 300, 1200, 200, 200, 0));
        }
        for (int i = 0; i < 3; i++) {
            levels.add(new SpaceButton(maxButtonX + i * 300, 900, 200, 200, 0));
        }
        for (int i = 0; i < 3; i++) {
            levels.add(new SpaceButton(maxButtonX + i * 300, 600, 200, 200, 0));
        }

        for (int i = 0; i < 2; i++) {
            levels.add(new SpaceButton(maxButtonX + i * 300, 300, 200, 200, 0));
        }


    }

    public void warring() {
        if (iswarring) {
            if (TimeUtils.millis() > timeLastWarring + timeWarring)
                iswarring = false;
        }
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
