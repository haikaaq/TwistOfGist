package ru.pick;


import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class LoadingScreen implements Screen {


    private Main main;
    private SpriteBatch batch;
    private BitmapFont font70;

    private ProgressBar progressBar;
    private Label progressLabel;
    private AssetManager manager;


    Texture imgLogo;


    public LoadingScreen(Main main) {

        this.main = main;
        batch = main.batch;
        this.manager = main.manager;

        font70 = main.font70;


        loadAssets();


        createLoadingUI();


        imgLogo = new Texture("logo.png");
    }

    private void loadAssets() {
        // Получаем список всех файлов в корне assets
        FileHandle[] allFiles = Gdx.files.internal("assets").list();

        // Фильтруем только .png файлы
        for (FileHandle file : allFiles) {
            if (file.name().endsWith(".png")) {
                manager.load(file.name(), Texture.class);
                Gdx.app.log("Loading", "Loading texture: " + file.name());
            }
        }
    }

    private void createLoadingUI() {
        Stage stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Прогресс-бар
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(
        );
        barStyle.knobBefore = barStyle.knob;

        progressBar = new ProgressBar(0, 100, 1, false, barStyle);
        progressBar.setSize(300, 30);
        progressBar.setPosition(
            Gdx.graphics.getWidth() / 2 - progressBar.getWidth() / 2,
            Gdx.graphics.getHeight() / 2
        );


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
        font70.draw(batch, (int) progress + "%", 250, 450);
        batch.draw(imgLogo, 200, 450, 205, 155);
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
