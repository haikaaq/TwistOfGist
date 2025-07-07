package ru.pick;


import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Objects;


public class LoadingScreen implements Screen {

    private OrthographicCamera camera;
    private Main main;
    public SpriteBatch batch;
    public BitmapFont font70;

    private ProgressBar progressBar;
    private Label progressLabel;
    private AssetManager manager;


    Texture imgLogo;


    public LoadingScreen(Main main) {

        batch = main.batch;
        camera = main.camera;
        this.main = main;

        this.manager = main.manager;

        font70 = main.font70;


        loadAssets();


        imgLogo = new Texture("logo.png");
    }

    private void loadAssets() {
        ObjLoader objLoader = new ObjLoader();

// 2. Регистрируем загрузчик для .obj
        manager.setLoader(Model.class, ".obj", new ObjLoader(manager.getFileHandleResolver()));


        manager.load("models/type0.obj", Model.class);


        manager.load("32.png", Texture.class);

        manager.load("701.png", Texture.class);

        manager.load("atlas.png", Texture.class);
        manager.load("atlasboss.png", Texture.class);
        manager.load("atlasButtons.png", Texture.class);
        manager.load("bgabout.png", Texture.class);
        manager.load("bgabout0.png", Texture.class);
        manager.load("bgabout1.png", Texture.class);
        manager.load("bggame.png", Texture.class);
        manager.load("bgleadbd.png", Texture.class);
        manager.load("bgmenu.png", Texture.class);
        manager.load("bgmenu2.png", Texture.class);
        manager.load("bgset.png", Texture.class);
        manager.load("bgshop.png", Texture.class);
        manager.load("black.png", Texture.class);

        manager.load("buttonsLeftRight.png", Texture.class);
        manager.load("earthatlas.png", Texture.class);
        manager.load("enemyes.png", Texture.class);
        manager.load("enemyesDead.png", Texture.class);

        manager.load("flyingSaucer.png", Texture.class);
        manager.load("fragments.png", Texture.class);
        manager.load("grayBG.png", Texture.class);
        manager.load("green.png", Texture.class);
        manager.load("js.png", Texture.class);
        manager.load("jsBase.png", Texture.class);
        manager.load("jsStick.png", Texture.class);
        manager.load("keys.png", Texture.class);
        manager.load("leaderboardBT.png", Texture.class);
        manager.load("levels.png", Texture.class);
        manager.load("logo.png", Texture.class);
        manager.load("longButton.png", Texture.class);
        manager.load("minus.png", Texture.class);
        manager.load("moneta.png", Texture.class);

        manager.load("nothing.png", Texture.class);
        manager.load("nums.png", Texture.class);
        manager.load("off.png", Texture.class);
        manager.load("on.png", Texture.class);
        manager.load("plus.png", Texture.class);
        manager.load("push.png", Texture.class);
        manager.load("red.png", Texture.class);
        manager.load("reset.png", Texture.class);
        manager.load("rocket.png", Texture.class);
        manager.load("rocketBoost.png", Texture.class);
        manager.load("shieldBoost.png", Texture.class);
        manager.load("shieldShip.png", Texture.class);
        manager.load("shieldWarringShip.png", Texture.class);
        manager.load("shots.png", Texture.class);
        manager.load("stone.png", Texture.class);
        manager.load("warring.png", Texture.class);
        manager.load("woundedemenies.png", Texture.class);
    }



    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.10f, 1);

        // Обновляем загрузку
        manager.update();
        float progress = manager.getProgress() * 100;


        // Когда все загружено, переключаем экран
        if (manager.update() && manager.getProgress() >= 1) {
            main.screenMenu = new ScreenMenu(main);
            main.screenGame = new ScreenGame(main);
            main.screenSettings = new ScreenSettings(main);
            main.screenLeaderboard = new ScreenLeaderboard(main);
            main.screenAbout = new ScreenAbout(main);
            main.screenShop = new ScreenShop(main);
            main.screenNumLevel = new ScreenNumLevel(main);


            main.setScreen(main.screenMenu);
            if (FirstRunManager.isFirstRun()) {
                main.isFirstRunning = true; // Показываем обучение
                FirstRunManager.setFirstRunCompleted();
            } else {
                main.isFirstRunning = false;

            }
        }


        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        font70.draw(batch, (int) progress + "%", 395, 750);
        batch.draw(imgLogo, 330, 750, 250, 165);
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
        batch.dispose();
        font70.dispose();

    }
}
