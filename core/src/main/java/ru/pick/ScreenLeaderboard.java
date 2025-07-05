package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

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

import java.util.ArrayList;
import java.util.List;


public class ScreenLeaderboard implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    private BitmapFont font32;
    Texture imgBG;
    Texture imgPBT;
    Texture imgMN;
    private InputKeyboard keyboard;
    private AssetManager manager;
    private boolean isleaderboard = false;
    private boolean idIsLoad;
    Texture imgBackAtlas;
    Texture imgWarring;
    Texture imgReset;
    TextureRegion[] imgBack = new TextureRegion[2];
    private long timeWarring = 1500, timeLastWarring;
    private long timeUpdatingFirebase = 1500, timeLastFirebaseUpdate;
    private boolean iswarring = false;
    private boolean iswarringName = false;

    SpaceButton btnBack;
    SpaceButton btnLead;
    SpaceButton btnName;
    SpaceButton btnPlace;
    SpaceButton btnReset;

    private List<Player> topPlayers = new ArrayList<>();


    public ScreenLeaderboard(Main main) {
        this.main = main;
        batch = main.batch;
        this.manager = main.manager;
        camera = main.camera;
        touch = main.touch;
        font = main.font70;
        font32 = main.font32;


        imgReset = manager.get("reset.png", Texture.class);
        imgBackAtlas = manager.get("buttonsLeftRight.png", Texture.class);
        imgBG = manager.get("bgleadbd.png", Texture.class);
        imgPBT = manager.get("leaderboardBT.png", Texture.class);
        imgWarring = manager.get("push.png", Texture.class);
        imgMN = manager.get("moneta.png", Texture.class);

        btnBack = new SpaceButton(10, 1500, 90, 90, 0);
        btnLead = new SpaceButton(font, LanguageManager.get("leaderboard"), SCR_HEIGHT - 20);
        btnName = new SpaceButton(font, LanguageManager.get("enteryourname"), SCR_HEIGHT / 2 + 100);
        btnPlace = new SpaceButton(font, LanguageManager.get("Youarein") + " " + main.player.rank + " " + LanguageManager.get("place"), SCR_HEIGHT / 4);
        btnReset = new SpaceButton(SCR_WIDTH - 100, 1500, 90, 90, 0);
        keyboard = new InputKeyboard(font, SCR_WIDTH, SCR_HEIGHT * 3 / 4, 8);

        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }


    }


    @Override
    public void show() {
        loadState();
        isleaderboard = false;
        if (main.isFirstLeaderboard) {
            //  loadId();
            keyboard.start();

        } else {
            main.player.savePlayerToFirebase(main.player);
            // leaderboard();

        }

    }

    @Override
    public void render(float delta) {
        warring();


        if (!keyboard.isKeyboardShow) {
            if (TimeUtils.millis() > timeUpdatingFirebase + timeLastFirebaseUpdate) {

                leaderboard();


                timeLastFirebaseUpdate = TimeUtils.millis();


            }


        }


        if (Gdx.input.justTouched()) {


            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (btnBack.hit(touch.x, touch.y)) {
                if (btnBack.hit(touch.x, touch.y)) {
                    main.setScreen(main.screenMenu);
                }

            }
            if (btnReset.hit(touch.x, touch.y)) {
                leaderboard();
                //getplase();
                iswarring = true;
                timeLastWarring = TimeUtils.millis();
            }
            if (keyboard.touch(touch.x, touch.y)) {

                main.player.name = keyboard.getText();
                main.player.money = main.allmoney;
                main.player.level = main.level;
                main.player.savePlayerToFirebase(main.player);
                main.isFirstLeaderboard = false;
                iswarring = true;
                iswarringName = true;
                timeLastWarring = TimeUtils.millis();

                saveState();

            }

        }


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        keyboard.draw(batch);

        btnLead.font.draw(batch, btnLead.text, btnLead.x, btnLead.y);
        batch.draw(imgBack[btnBack.type], btnBack.imgX, btnBack.imgY, btnBack.imgWidth, btnBack.imgHeight);
        batch.draw(imgReset, btnReset.imgX, btnReset.imgY, btnReset.imgWidth, btnReset.imgHeight);

        if (!keyboard.isKeyboardShow) {
            font.draw(batch, LanguageManager.get("name"), 115, SCR_HEIGHT - 160);
            if (LanguageManager.currentBundle == LanguageManager.ruBundle) {
                font.draw(batch, LanguageManager.get("levelLB"), 313, SCR_HEIGHT - 160);
            } else {
                font.draw(batch, LanguageManager.get("levelLB"), 390, SCR_HEIGHT - 160);
            }
            batch.draw(imgMN, 683, SCR_HEIGHT - 213, 70, 70);

            //font.draw(batch, LanguageManager.get("money"), 620, SCR_HEIGHT - 160);


            btnPlace.font.draw(batch, btnPlace.text, btnPlace.x, btnPlace.y);
            for (int i = 0; i < topPlayers.size(); i++) {

                Player p = topPlayers.get(i);
                batch.draw(imgPBT, 60, SCR_HEIGHT - 260 - 68 * (i + 1), SCR_WIDTH - 120, 70);
                font.draw(batch, (i + 1) + ". " + p.name, 100, SCR_HEIGHT - 260 - 68 * i);
                font.draw(batch, "" + p.level, 500, SCR_HEIGHT - 260 - 68 * i);
                if (p.money < 100000) {
                    font.draw(batch, (p.money + ""), 700, SCR_HEIGHT - 260 - 68 * i);
                } else font.draw(batch, (p.money / 1000 + "k"), 700, SCR_HEIGHT - 260 - 68 * i);


            }
        } else btnName.font.draw(batch, btnName.text, btnName.x, btnName.y);
        if (iswarring) {
            batch.draw(imgWarring, 150, 1410, 600, 170);
            if (iswarringName) {
                font32.draw(batch, LanguageManager.get("name_change_hint"), 309, 1536, 400, Align.center, true);

            } else {
                font32.draw(batch, LanguageManager.get("leaderboard_updated"), 309, 1536, 400, Align.center, true);
            }


        }
        batch.end();
    }

    public void warring() {
        if (iswarring) {
            if (TimeUtils.millis() > timeLastWarring + timeWarring) {
                iswarring = false;
                iswarringName = false;
            }
        }
    }

    public void saveState() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

        prefs.putBoolean("isFLB", main.isFirstLeaderboard);
        prefs.putString("nm", main.player.name);

        prefs.flush();
    }

    public void loadState() {

        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

// Получаем значение (с дефолтным значением на случай первого запуска)
        main.isFirstLeaderboard = prefs.getBoolean("isFLB", true);
        main.player.name = prefs.getString("nm");


    }


    //public void getplase() {
    //FirebaseManager firebase = FirebaseService.create();

    public void leaderboard() {
        FirebaseManager firebase = FirebaseService.create();


        firebase.getTop10ByLevel(new FirebaseManager.SortedLeaderboardCallback() {


            @Override
            public void onSuccess(List<Player> players) {
                topPlayers = players;
                if (firebase.isOnline()) {
                    if (main.player.rank < 1 || main.player.rank > 10) {
                        btnPlace.changeText(LanguageManager.get("notintop"));
                    } else {
                        btnPlace.changeText(LanguageManager.get("Youarein") + " " + main.player.rank + " " + LanguageManager.get("place"));
                        //saveId();
                    }

                } else {
                    btnPlace.changeText(LanguageManager.get("Youareoffline"));
                }
            }

            @Override
            public void onError(String message) {
                btnPlace.changeText(LanguageManager.get("Youareoffline"));

            }
        });

        firebase.getPlayerRank(main.player, new FirebaseManager.PlayerRankCallback() {

            @Override
            public void onSuccess(int rank) {


                main.player.rank = rank;

            }

            @Override
            public void onError(String message) {
                Gdx.app.log("RANK", "Ошибка: " + message);
            }
        });
        btnLead.changeText(LanguageManager.get("leaderboard"));
        btnName.changeText(LanguageManager.get("enteryourname"));

        isleaderboard = true;

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
        keyboard.dispose();

    }


}
