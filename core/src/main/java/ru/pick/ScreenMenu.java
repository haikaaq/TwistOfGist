package ru.pick;

import static ru.pick.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;


public class ScreenMenu implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font70;
    private AssetManager manager;
    private long timeLastChangeBg, timeChangeBg = 700;
    private int phaseBg;
    private float menuEnemyAlpha = 1f;



    private Levels.Level curentlevel;
    SpaceButton btnLevel;
    SpaceButton btnPlay;
    SpaceButton btnSetting;
    SpaceButton btnLeaderboard;
    SpaceButton btnAbout;
    SpaceButton btnExit;
    SpaceButton btnAllmoney;
    SpaceButton btnShop;

    Music fonMusic;
    Music menuMusic;
    Texture imgLogo;
    Texture imgMN;
    Texture imgBG2;

    Texture imgEnemyesBoses;
    Texture imgLongButtonAtlas;
    Texture imgButtonsAtlas;
    TextureRegion[] imgEnemyBoses = new TextureRegion[6];
    TextureRegion[] imgBG = new TextureRegion[4];
    TextureRegion[] imgLongButton = new TextureRegion[3];
    TextureRegion[][] imgButtons = new TextureRegion[3][3];
    Texture imgBGAtlas;

    Boss menuEnemy;


    public ScreenMenu(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font70 = main.font70;
        this.manager = main.manager;
        fonMusic = main.fonMusic;
        menuMusic = main.menuMusic;

        LanguageManager.loadBundles();
        imgEnemyesBoses = new Texture("atlasboss.png");
        imgLongButtonAtlas = new Texture("longButton.png");
        imgButtonsAtlas = manager.get("atlasButtons.png", Texture.class);
        imgBGAtlas = manager.get("bgmenu.png", Texture.class);
        imgBG2 = manager.get("bgmenu.png", Texture.class);
        imgMN = manager.get("moneta.png", Texture.class);
        imgLogo = manager.get("logo.png", Texture.class);

        for (int i = 0; i < imgBG.length; i++) {
            imgBG[i] = new TextureRegion(imgBGAtlas, (i) * 900, 0, 900, 1600);
        }
        for (int e = 0; e < imgEnemyBoses.length; e++) {

            imgEnemyBoses[e] = new TextureRegion(imgEnemyesBoses, (e < 6 ? e : 10 - e) * 1025, 0, 1025, 1025);
        }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e) * 193, 497, 193);
        }

        for (int j = 0; j < imgButtons.length; j++) {
            for (int i = 0; i < imgButtons[j].length; i++) {
                imgButtons[j][i] = new TextureRegion(imgButtonsAtlas, i * 209, (j) * 180, 209, 180);
            }
        }
        btnPlay = new SpaceButton(font70, LanguageManager.get("play"), imgLongButtonAtlas, SCR_HEIGHT / 3.6f, 3.8f);
        btnLeaderboard = new SpaceButton(font70, LanguageManager.get("leaderboard"), 250);
        btnLevel = new SpaceButton(font70, LanguageManager.get("level") + " " + main.level, 1540);

        btnAbout = new SpaceButton(0, 1140, 200, 170, 0);
        btnSetting = new SpaceButton(SCR_WIDTH - 200, 1140, 200, 170, 1);
        btnShop = new SpaceButton(0, 950, 200, 170, 2);

        btnExit = new SpaceButton(font70, "x", 20, SCR_HEIGHT - 30);
        btnAllmoney = new SpaceButton(font70, "" + main.allmoney, SCR_WIDTH - 120, 1550);


        menuEnemy = new Boss();
        menuEnemy.MenuEnemy = true;


    }


    @Override
    public void show() {
        loadState();
        if (main.isFonMusic) {
            menuMusic.setLooping(true);
            menuMusic.play();
        }

    }

    @Override
    public void render(float delta) {
        curentlevel = Levels.LEVELS[main.level];
        Vector3 Mousepose = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        camera.unproject(Mousepose);
        if (LanguageManager.currentBundle == LanguageManager.ruBundle) {
            btnPlay.imgWidhtCoefficient = 2.7f;
        } else btnPlay.imgWidhtCoefficient = 4.2f;
        if (gameState == GAME) {
            if (LanguageManager.currentBundle == LanguageManager.ruBundle) {
                btnPlay.imgWidhtCoefficient = 1.5f;
            } else btnPlay.imgWidhtCoefficient = 2f;
        }
        btnPlay.buttonsState(Mousepose.x, Mousepose.y);
        btnSetting.buttonsState(Mousepose.x, Mousepose.y);
        btnAbout.buttonsState(Mousepose.x, Mousepose.y);
        btnShop.buttonsState(Mousepose.x, Mousepose.y);
        btnLeaderboard.buttonsState(Mousepose.x, Mousepose.y);
        btnExit.buttonsState(Mousepose.x, Mousepose.y);

        if (btnAbout.setScreenButton) main.setScreen(main.screenAbout);
        if (btnSetting.setScreenButton) main.setScreen(main.screenSettings);
        if (btnShop.setScreenButton) main.setScreen(main.screenShop);
        if (btnPlay.setScreenButton) {
            main.setScreen(main.screenNumLevel);
            if (main.isFonMusic) {
                menuMusic.pause();
                fonMusic.setLooping(true);
                fonMusic.play();
            }
            fonMusic.setVolume(1.3f);

            return;
        }


        if (btnLeaderboard.setScreenButton) {
            main.setScreen(main.screenLeaderboard);
            //iskeyboard = true;
        }

        if (btnExit.setScreenButton) {
            Gdx.app.exit();
        }


        menuEnemy.move();
        btnSetting.changePhases();
        btnAbout.changePhases();
        btnShop.changePhases();
        btnPlay.changePhases();
        if (gameState == GAME) {
            btnPlay.changeText(LanguageManager.get("continue"));
        } else btnPlay.changeText(LanguageManager.get("play"));

        btnLeaderboard.changeText(LanguageManager.get("leaderboard"));
        btnLevel.changeText(LanguageManager.get("level") + " " + main.level);


        btnAllmoney.changeText(main.allmoney);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //batch.draw(imgBG[phaseBg], 0, 0, SCR_WIDTH, SCR_HEIGHT);
        batch.draw(imgBG2, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        changePhase();

        btnPlay.isMove = true;


        batch.setColor(1, 1, 1, menuEnemyAlpha);
        batch.draw(imgEnemyBoses[menuEnemy.phase], menuEnemy.scrX(), menuEnemy.scrY(), menuEnemy.width / 2, menuEnemy.height / 2, menuEnemy.width, menuEnemy.height, 1, 1, menuEnemy.rotation);
        batch.setColor(1, 1, 1, 1);


        //batch.draw(imgLogo, SCR_WIDTH / 2 - 240, 950 , 480, 390);
        batch.draw(imgLongButton[btnPlay.phase], btnPlay.imgX, btnPlay.imgY, btnPlay.imgWidth, btnPlay.imgHeight);
        batch.draw(imgButtons[btnSetting.type][btnSetting.phase], btnSetting.imgX, btnSetting.imgY, btnSetting.imgWidth, btnSetting.imgHeight);
        batch.draw(imgButtons[btnAbout.type][btnAbout.phase], btnAbout.imgX, btnAbout.imgY, btnAbout.imgWidth, btnAbout.imgHeight);
        batch.draw(imgButtons[btnShop.type][btnShop.phase], btnShop.imgX, btnShop.imgY, btnShop.imgWidth, btnShop.imgHeight);

        btnPlay.font.draw(batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnLeaderboard.font.draw(batch, btnLeaderboard.text, btnLeaderboard.x, btnLeaderboard.y);
        btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        btnLevel.font.draw(batch, btnLevel.text, btnLevel.x, btnLevel.y);
        btnAllmoney.font.draw(batch, main.allmoney < 1000 ? btnAllmoney.text : main.allmoney / 1000 + "k", btnAllmoney.x, btnAllmoney.y);

        batch.draw(imgMN, btnAllmoney.x - 70, btnAllmoney.y - 58, 50, 50);


        batch.end();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePhase() {
        if (TimeUtils.millis() >= timeChangeBg + timeLastChangeBg) {
            phaseBg += 1;
            if (phaseBg == 4) phaseBg = 0;
            timeLastChangeBg = TimeUtils.millis();

        }
    }

    public void loadState() {

        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

// Получаем значение (с дефолтным значением на случай первого запуска)
        main.isFirstLeaderboard = prefs.getBoolean("isFLB", true);


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
