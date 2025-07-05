package ru.pick;

import static ru.pick.Main.SCREEN;
import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;
import static ru.pick.Main.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;


public class ScreenSettings implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font70;
    private BitmapFont font32;
    private AssetManager manager;

    private Levels.Level curentlevel;
    private InputKeyboard keyboard;
    private Rocket rocket;
    Texture imgBG;
    Texture imgBG2;
    Texture imgON;
    Texture imgOFF;
    Texture imgLogo;
    Texture imgBackAtlas;
    Texture imgLongButtonAtlas;
    Texture imgWarring;
    //Texture imgRocket0;
    //TextureRegion[] imgRocket= new TextureRegion[1];
    TextureRegion[] imgBack = new TextureRegion[2];
    TextureRegion[] imgLongButton = new TextureRegion[3];
    private long timeWarring = 1100, timeLastWarring;
    private boolean iswarring = false;
    //код для смены вида управления скорее всего пригодится в следующих ровнях(Setting levels), поэтому не удален

    SpaceButton btnBack;
    SpaceButton btnLeft;
    SpaceButton btnRight;

    SpaceButton btnActionSounds;
    SpaceButton btnFonMusic;
    SpaceButton btnMusic;
    SpaceButton btnLanguage;
    SpaceButton btnRussian;
    SpaceButton btnEnglish;
    SpaceButton btnName;


    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font70 = main.font70;
        this.manager = main.manager;
        font32 = main.font32;
        loadLevel(main.level);
        controls = SCREEN;

        imgLongButtonAtlas = manager.get("LongButton.png", Texture.class);
        imgBG = manager.get("bgset.png", Texture.class);
        imgON = manager.get("on.png", Texture.class);
        imgOFF = manager.get("off.png", Texture.class);
        imgBG2 = manager.get("bgmenu2.png", Texture.class);
        imgLogo = manager.get("logo.png", Texture.class);
        imgWarring = manager.get("push.png", Texture.class);
        imgBackAtlas = manager.get("buttonsLeftRight.png", Texture.class);

        btnMusic = new SpaceButton(font70, LanguageManager.get("music"), 100, 1100);


        btnFonMusic = new SpaceButton(font70, LanguageManager.get("backgroundmusic"), 230, 1000);
        btnActionSounds = new SpaceButton(font70, LanguageManager.get("ActionsSounds"), 230, 900);
        btnLanguage = new SpaceButton(font70, LanguageManager.get("Language"), 100, 800);
        btnRussian = new SpaceButton(font70, LanguageManager.get("russian"), 230, 700);
        btnEnglish = new SpaceButton(font70, LanguageManager.get("english"), 230, 600);

        keyboard = new InputKeyboard(font70, SCR_WIDTH, SCR_HEIGHT * 3 / 4, 8);


        btnBack = new SpaceButton(10, 1500, 90, 90, 0);

        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }
        //for (int e = 0; e < imgRocket.length; e++) {

        //imgRocket[e] = new TextureRegion(imgRocket0, 0, 0, 500, 500);
        // }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e) * 193, 497, 193);
        }
        btnName = new SpaceButton(font70, LanguageManager.get("changename"), imgLongButtonAtlas, SCR_HEIGHT / 4.3f, 1.7f);

        btnActionSounds.width += 60;
        btnFonMusic.width += 60;
        btnEnglish.width += 60;
        btnRussian.width += 60;


    }


    @Override
    public void show() {
        loadLevel(main.level);
        rocket = new Rocket(100, 100);

    }

    @Override
    public void render(float delta) {
        rocket.update(delta);
        warring();
        Vector3 Mousepose = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        camera.unproject(Mousepose);
        btnName.buttonsState(Mousepose.x, Mousepose.y);
        if (btnName.setScreenButton && !main.isFirstLeaderboard) {

            keyboard.start();

        }
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (btnBack.hit(touch.x, touch.y)) {
                main.setScreen(main.screenMenu);
            }
            if (keyboard.touch(touch.x, touch.y)) {
                main.player.name = keyboard.getText();
                iswarring = true;
                timeLastWarring = TimeUtils.millis();
                main.isNewName = true;
                saveState();




            }

            if (btnFonMusic.hit(touch.x - 60, touch.y) && !keyboard.isKeyboardShow) {
                if (main.isFonMusic) main.isFonMusic = false;
                else main.isFonMusic = true;
            }

            if (btnActionSounds.hit(touch.x - 60, touch.y) && !keyboard.isKeyboardShow) {
                if (main.isActionSounds) main.isActionSounds = false;
                else main.isActionSounds = true;

            }
            if (btnEnglish.hit(touch.x - 60, touch.y) && !keyboard.isKeyboardShow) {
                LanguageManager.setLanguage("en");
            }
            if (btnRussian.hit(touch.x - 60, touch.y) && !keyboard.isKeyboardShow) {
                LanguageManager.setLanguage("ru");

            }


        }
        btnName.changePhases();
        btnName.changeText(LanguageManager.get("changename"));
        updateButtons();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);



        font70.draw(batch, LanguageManager.get("settings"), 0, 1550, SCR_WIDTH, Align.center, true);

        btnMusic.font.draw(batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnFonMusic.font.draw(batch, btnFonMusic.text, btnFonMusic.x, btnFonMusic.y);
        btnLanguage.font.draw(batch, btnLanguage.text, btnLanguage.x, btnLanguage.y);
        btnRussian.font.draw(batch, btnRussian.text, btnRussian.x, btnRussian.y);
        btnEnglish.font.draw(batch, btnEnglish.text, btnEnglish.x, btnEnglish.y);
        batch.draw(main.isFonMusic ? imgON : imgOFF, btnFonMusic.x + btnFonMusic.width, btnFonMusic.y - btnFonMusic.height - 10, 70, 70);
        btnActionSounds.font.draw(batch, btnActionSounds.text, btnActionSounds.x, btnActionSounds.y);
        if (!main.isFirstLeaderboard) {
        batch.draw(imgLongButton[btnName.phase], btnName.imgX, btnName.imgY, btnName.imgWidth, btnName.imgHeight);
            btnName.font.draw(batch, btnName.text, btnName.x, btnName.y);
        }
        batch.draw(main.isActionSounds ? imgON : imgOFF, btnActionSounds.x + btnActionSounds.width, btnActionSounds.y - btnActionSounds.height - 10, 70, 70);
        batch.draw(LanguageManager.currentBundle == LanguageManager.enBundle ? imgON : imgOFF, btnEnglish.x + btnEnglish.width, btnEnglish.y - btnEnglish.height - 10, 70, 70);
        batch.draw(LanguageManager.currentBundle == LanguageManager.ruBundle ? imgON : imgOFF, btnRussian.x + btnRussian.width, btnRussian.y - btnRussian.height - 10, 70, 70);

        // batch.draw(controls==ACCELEROMETER?imgON:imgOFF,btnAccelerometr.x+btnAccelerometr.widht, btnAccelerometr.y-btnAccelerometr.height -10,70,70 );
        /*if (controls==JOYSTIK||controls==JOYSTIK_LEFT||controls==JOYSTIK_RIGHT){
            btnLeft.font.draw(batch,btnLeft.text,btnLeft.x,btnLeft.y);
            batch.draw(controls==JOYSTIK_LEFT?imgON:imgOFF,btnLeft.x+btnLeft.widht, btnLeft.y-btnLeft.height -10,70,70 );
            btnRight.font.draw(batch,btnRight.text,btnRight.x,btnRight.y);
            batch.draw(controls==JOYSTIK_RIGHT?imgON:imgOFF,btnRight.x+btnRight.widht, btnRight.y-btnRight.height -10,70,70 );
            btnAccelerometr.y=600;
            btnScreen.y=700;
            btnMusic.y=400;
            btnActionSounds.y=200;
            btnFonMusic.y=300;
        }
        else {btnAccelerometr.y=800;
              btnScreen.y=900;
            btnMusic.y=600;
            btnActionSounds.y=400;
            btnFonMusic.y=500;
        }*/
        if (iswarring) {
            batch.draw(imgWarring, 150, 1410, 600, 170);

            font32.draw(batch, LanguageManager.get("namesaved"), 301, 1536, 400, Align.center, true);


        }
        batch.draw(imgBack[btnBack.type], btnBack.imgX, btnBack.imgY, btnBack.imgWidth, btnBack.imgHeight);
        keyboard.draw(batch);


        // batch.draw(imgRocket[0], rocket.getX(), rocket.getY(),
        //    rocket.width/2, rocket.height/2,
        //    rocket.width, rocket.height,
        //    1, 1,
        //    rocket.getRotation());
        batch.end();
       /* try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
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

    public void saveState() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");


        prefs.putString("nm", main.player.name);

        prefs.flush();
    }

    private void loadLevel(int level) {
        curentlevel = Levels.LEVELS[level];
    }

    private void updateButtons() {


        btnFonMusic.changeText(LanguageManager.get("backgroundmusic"));
        btnActionSounds.changeText(LanguageManager.get("ActionsSounds"));
        btnLanguage.changeText(LanguageManager.get("Language"));
        btnRussian.changeText(LanguageManager.get("russian"));
        btnEnglish.changeText(LanguageManager.get("english"));
        btnLanguage.changeText(LanguageManager.get("Language"));
        btnMusic.changeText(LanguageManager.get("music"));
    }

    public void warring() {
        if (iswarring) {
            if (TimeUtils.millis() > timeLastWarring + timeWarring)
                iswarring = false;
        }
    }

}
