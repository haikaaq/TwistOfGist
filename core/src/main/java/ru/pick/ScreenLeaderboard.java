package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

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
    private InputKeyboard keyboard;
    public static boolean iskeyboard = true;
    private boolean idIsLoad;
    Texture imgBackAtlas;
    Texture imgReset;
    TextureRegion[] imgBack = new TextureRegion[2];

    SpaceButton btnBack;
    SpaceButton btnLead;
    SpaceButton btnName;
    SpaceButton btnPlace;
    SpaceButton btnReset;

    private List<Player> topPlayers = new ArrayList<>();


    public ScreenLeaderboard(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font70;
        font32 = main.font32;


        imgReset = new Texture("reset.png");
        imgBackAtlas = new Texture("buttonsLeftRight.png");
        imgBG = new Texture("bgleadbd.png");
        imgPBT = new Texture("leaderboardBT.png");
        btnBack = new SpaceButton(10, 1500, 90, 90, 0);
        btnLead = new SpaceButton(font, LanguageManager.get("leaderboard"), SCR_HEIGHT - 20);
        btnName = new SpaceButton(font, LanguageManager.get("enteryourname"), SCR_HEIGHT / 2 + 100);
        btnPlace = new SpaceButton(font, LanguageManager.get("Youarein") + " " + main.player.rank + " " + LanguageManager.get("place"), SCR_HEIGHT / 4);
        btnReset = new SpaceButton(SCR_WIDTH - 100, 1500, 90, 90, 0);
        keyboard = new InputKeyboard(font, SCR_WIDTH, SCR_HEIGHT * 3 / 4, 11);


        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }


    }


    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {

        if (!iskeyboard) {
            main.player.savePlayerToFirebase(main.player);
            leaderboard();
            getplase();
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
                getplase();
            }
            if (keyboard.touch(touch.x, touch.y)) {
                main.player.name = keyboard.getText();
                main.player.money = main.allmoney;
                main.player.level = main.level;
                loadId();

                iskeyboard = false;
            }

        }

        if (iskeyboard) keyboard.start();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        keyboard.draw(batch);

        btnLead.font.draw(batch, btnLead.text, btnLead.x, btnLead.y);
        batch.draw(imgBack[btnBack.type], btnBack.imgX, btnBack.imgY, btnBack.imgWidth, btnBack.imgHeight);
        batch.draw(imgReset, btnReset.imgX, btnReset.imgY, btnReset.imgWidth, btnReset.imgHeight);

        if (!iskeyboard) {
            font.draw(batch, LanguageManager.get("name"), 100, SCR_HEIGHT - 160);
            if (LanguageManager.currentBundle == LanguageManager.ruBundle) {
                font.draw(batch, LanguageManager.get("levelLB"), 280, SCR_HEIGHT - 160);
            } else {
                font.draw(batch, LanguageManager.get("levelLB"), 360, SCR_HEIGHT - 160);
            }
            font.draw(batch, LanguageManager.get("money"), 620, SCR_HEIGHT - 160);


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
        batch.end();
    }


    public void getplase() {
        FirebaseManager firebase = FirebaseService.create();

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
    }

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
                    }
                    saveId();
                } else {
                    btnPlace.changeText(LanguageManager.get("Youareoffline"));
                }
            }

            @Override
            public void onError(String message) {
                btnPlace.changeText(LanguageManager.get("Youareoffline"));

            }
        });


        btnLead.changeText(LanguageManager.get("leaderboard"));
        btnName.changeText(LanguageManager.get("enteryourname"));


    }

    private void loadId() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

        main.player.firebaseId = prefs.getString("id");
        idIsLoad = true;

    }

    private void saveId() {
        if (idIsLoad) {

            Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
            prefs.putString("id", main.player.firebaseId);

            prefs.flush();
        }
    }


   /* public void changeButtons (){
        FirebaseManager firebase = FirebaseService.create();
        if(firebase.isOnline()) {
            if(main.player.rank<1||main.player.rank>10){
                btnPlace.changeText(LanguageManager.get("notintop"));}
            else{
                btnPlace.changeText(LanguageManager.get("Youarein")+" "+main.player.rank+" "+LanguageManager.get("place"));
            }
        }
        else{
            btnPlace.changeText(LanguageManager.get("Youareoffline"));
        }


        btnLead.changeText(LanguageManager.get("leaderboard"));
        btnName.changeText(LanguageManager.get("enteryourname"));

    }*/

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
