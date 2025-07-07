package ru.pick;

import static ru.pick.Main.SCREEN;
import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
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

public class ScreenShop implements Screen {

    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    private BitmapFont font32;
    private AssetManager manager;

    private static int SHOP = 0, SKIN = 1, SHOTS = 2, BOOST = 3;
    private long timeLastSpawnShots, timeShotsInterval = 300;
    private int shotEven = 0;
    public int shipSkin = 0;
    public int shotsShots;
    public int shotsBoostCount;
    private int shipDistance;
    private int maxSkin = 4;
    private int maxBoost = 7;
    private int maxShot = 3;
    private int buyBoostLevel = 0;
    private int buyShotLevel = 0;
    private int buySkinLevel = 0;
    private int realShotEven;
    private int realShotsSkin;
    private int realShipSkin;
    private int realBoostCount;

    private int screenState = SHOP;

    private boolean isNewShipSkin;
    private boolean isNewShotSkin;
    private long timeRedSpawn, timeRed = 700;
    private long timeGreenSpawn, timeGreen = 700;
    private long timeWarringAndPush = 1100, timeLastWarring, timeLastPush;
    private boolean iswarring = false;
    private boolean ispush = false;
    private boolean ispushAgain = false;

    SpaceButton btnAllmoney;

    Texture imgShipsatlas;
    Texture imgShotsatlas;
    Texture imgPlus;
    Texture imgBG;

    Texture imgGreen;
    Texture imgRed;
    Texture imgLongButtonAtlas;
    Texture imgBackAtlas;
    Texture imgWarring;
    Texture imgPush;

    SpaceButton btnBack;
    SpaceButton btnSkins;
    SpaceButton btnShots;
    SpaceButton btnBoosts;
    SpaceButton btnRight;
    SpaceButton btnLeft;
    SpaceButton btnBuy;


    TextureRegion[] imgBack = new TextureRegion[2];
    TextureRegion[] imgLongButton = new TextureRegion[3];
    TextureRegion[][] imgShipatlas = new TextureRegion[5][12];
    TextureRegion[] imgShotatlas = new TextureRegion[4];

    Ship ship;
    Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");
    private List<Shot> shots = new ArrayList<>();
    private List<Boolean> isSkinBuy = new ArrayList<>();
    private List<Boolean> isShotsBuy = new ArrayList<>();
    private List<Boolean> isBoostBuy = new ArrayList<>();

    public ScreenShop(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font70;
        font32 = main.font32;
        this.manager = main.manager;

        imgBackAtlas = manager.get("buttonsLeftRight.png", Texture.class);

        imgGreen = manager.get("green.png", Texture.class);
        imgRed = manager.get("red.png", Texture.class);
        imgBG = manager.get("bgshop.png", Texture.class);
        imgPush = manager.get("push.png", Texture.class);
        imgShipsatlas = new Texture("atlas.png");
        imgShotsatlas = manager.get("shots.png", Texture.class);
        imgPlus = manager.get("plus.png", Texture.class);
        imgLongButtonAtlas = manager.get("longButton.png", Texture.class);
        imgWarring = manager.get("warring.png", Texture.class);

        btnAllmoney = new SpaceButton(font, "" + (main.allmoney >= 1000 ? main.allmoney : main.allmoney / 1000 + 'k'), SCR_WIDTH - 120, 1550);
        btnBack = new SpaceButton(10, 1500, 90, 90, 0);


        btnBoosts = new SpaceButton(font, LanguageManager.get("boosts"), imgLongButtonAtlas, 325, 3.8f);

        btnSkins = new SpaceButton(font, LanguageManager.get("skins"), imgLongButtonAtlas, 500, 4.8f);

        btnShots = new SpaceButton(font, LanguageManager.get("shots"), imgLongButtonAtlas, 150, 4.8f);


        btnBuy = new SpaceButton(font, LanguageManager.get("buyfor") + " " + price() + " " + LanguageManager.get("coins"), 255);
        btnLeft = new SpaceButton(10, SCR_HEIGHT / 2, 100, 100, 0);
        ;
        btnRight = new SpaceButton(SCR_WIDTH - 100, SCR_HEIGHT / 2, 100, 100, 1);





        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }

        for (int j = 0; j < imgShipatlas.length; j++) {
            for (int i = 0; i < imgShipatlas[j].length; i++) {
                imgShipatlas[j][i] = new TextureRegion(imgShipsatlas, (i) * 400, j * 500, 400, 500);
            }
        }

        for (int i = 0; i < imgShotatlas.length; i++) {
            imgShotatlas[i] = new TextureRegion(imgShotsatlas, (i) * 100, 0, 100, 350);
        }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e) * 193, 497, 193);
        }
        for (int e = 0; e <= maxSkin; e++) {
            if (e == 0) isSkinBuy.add(Boolean.TRUE);
            else isSkinBuy.add(Boolean.FALSE);
        }

        for (int e = 0; e <= maxShot; e++) {
            if (e == 0) isShotsBuy.add(Boolean.TRUE);
            else isShotsBuy.add(Boolean.FALSE);
        }

        for (int e = 0; e <= maxBoost; e++) {
            if (e == 0) isBoostBuy.add(Boolean.TRUE);
            else isBoostBuy.add(Boolean.FALSE);
        }

        ship = new Ship(SCR_WIDTH / 2, SCR_HEIGHT / 2);
        ship.width = ship.height = 460;
        ship.vY = 0;


    }

    @Override
    public void show() {
        LoadShop();
    }

    @Override
    public void render(float delta) {

        Vector3 Mousepose = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        camera.unproject(Mousepose);
        warring();

        btnShots.buttonsState(Mousepose.x, Mousepose.y);
        btnSkins.buttonsState(Mousepose.x, Mousepose.y);
        btnBoosts.buttonsState(Mousepose.x, Mousepose.y);
        btnBuy.buttonsState(Mousepose.x, Mousepose.y);
        btnLeft.buttonsState(Mousepose.x, Mousepose.y);
        btnRight.buttonsState(Mousepose.x, Mousepose.y);
        btnBack.buttonsState(Mousepose.x, Mousepose.y);

        //переход между экранами

        if (btnBack.setScreenButton) {
            if (screenState == SHOP) {
                main.setScreen(main.screenMenu);

            }
            else screenState = SHOP;
        }
        if (btnShots.setScreenButton && screenState == SHOP) {
            screenState = SHOTS;

        }
        if (btnBoosts.setScreenButton && screenState == SHOP) {
            screenState = BOOST;
        }
        if (btnSkins.setScreenButton && screenState == SHOP) {
            screenState = SKIN;


        }
        // если нажали на правую кнопку
        if (btnRight.setScreenButton && ship.x == SCR_WIDTH / 2) {

            if (screenState == BOOST && buyBoostLevel < maxBoost) {
                ChangePlusShots();
                buyBoostLevel += 1;
            }
            if (screenState == SKIN && buySkinLevel < maxSkin) {
                ChangeShip(btnRight);
                buySkinLevel += 1;
            }

            if (screenState == SHOTS && buyShotLevel < maxShot) {
                ChangeShot(btnRight);
                buyShotLevel += 1;
            }
        }
        //если нажали на левую кнопку
        if (btnLeft.setScreenButton && ship.x == SCR_WIDTH / 2) {

            if (screenState == BOOST && buyBoostLevel > 0) {
                ChangeMinusShots();
                buyBoostLevel -= 1;
            }
            if (screenState == SKIN && buySkinLevel > 0) {
                ChangeShip(btnLeft);
                buySkinLevel -= 1;
            }
            if (screenState == SHOTS && buyShotLevel > 0) {
                ChangeShot(btnLeft);
                buyShotLevel -= 1;
            }
        }
        // если нажали кнопку купить

        if (btnBuy.setScreenButton) {



                if (screenState == BOOST) {
                    if (main.allmoney < price()) {
                        timeRedSpawn = TimeUtils.millis();
                        iswarring = true;
                        timeLastWarring = TimeUtils.millis();
                    } else {
                        if (price() > 0) {
                        main.allmoney -= price();
                            ispush = true;
                            timeLastPush = TimeUtils.millis();
                            isBoostBuy.set(buyBoostLevel, true);
                            timeGreenSpawn = TimeUtils.millis();

                        main.shotsBoostCount = shotsBoostCount;
                        main.shotEven = shotEven;
                    }
                    if (price() == 0) {
                        ispush = true;
                        ispushAgain = true;
                        timeLastPush = TimeUtils.millis();
                        main.shotsBoostCount = shotsBoostCount;
                        main.shotEven = shotEven;
                        timeGreenSpawn = TimeUtils.millis();
                    }
                    }

                }
            if (screenState == SKIN) {
                if (main.allmoney < price()) {
                        timeRedSpawn = TimeUtils.millis();
                        iswarring = true;
                        timeLastWarring = TimeUtils.millis();
                    } else {
                    if (price() > 0) {
                        main.allmoney -= price();
                        isSkinBuy.set(buySkinLevel, true);
                        timeGreenSpawn = TimeUtils.millis();
                        main.shipSkin = shipSkin;
                    }
                    if (price() == 0) {
                        main.shipSkin = shipSkin;
                        timeGreenSpawn = TimeUtils.millis();
                    }
                }


            }
            if (screenState == SHOTS) {
                if (main.allmoney < price()) {
                        timeRedSpawn = TimeUtils.millis();
                        iswarring = true;
                        timeLastWarring = TimeUtils.millis();
                    } else {
                    if (price() > 0) {
                        main.allmoney -= price();
                        isShotsBuy.set(buyShotLevel, true);
                        timeGreenSpawn = TimeUtils.millis();


                        main.shotsShots = shotsShots;
                    }
                    if (price() == 0) {
                        main.shotsShots = shotsShots;
                        timeGreenSpawn = TimeUtils.millis();
                    }
                }
                SaveShop();






            }


        }
        btnShots.changePhases();
        btnBoosts.changePhases();
        btnSkins.changePhases();
        btnLeft.changePhases();
        btnRight.changePhases();
        ///действия
        btnAllmoney.changeText(main.allmoney);
        spavnShot();
        updateLanguage();


        moveShots();

        moveship(0);
        btnBuy.changeText(LanguageManager.get("buyfor") + " " + price() + " " + LanguageManager.get("coins"));
        if (screenState == BOOST && price() == 0) {
            btnBuy.changeText(LanguageManager.get("getit"));
        }
        if (screenState == SKIN && price() == 0) {
            btnBuy.changeText(LanguageManager.get("getit"));
        }
        if (screenState == SHOTS & price() == 0) {
            btnBuy.changeText(LanguageManager.get("getit"));
        }


        if (screenState == SHOP) {
            shotsBoostCount = main.shotsBoostCount;
            shotEven = main.shotEven;
            shipSkin = main.shipSkin;
            shotsShots = main.shotsShots;
        }
        if (screenState == BOOST) {
            shotsBoostCount = realBoostCount;
            shotEven = realShotEven;
        }
        if (screenState == SHOTS) {
            shotsShots = realShotsSkin;

        }
        if (screenState == SKIN) {
            shipSkin = realShipSkin;

        }


        for (int i = shots.size() - 1; i >= 0; i--) {
            if (shots.get(i).OutOfscreen()) {
                shots.get(i).width = 0;
                shots.get(i).height = 0;
                if (TimeUtils.millis() >= timeLastSpawnShots + timeShotsInterval) {
                    shots.remove(i);
                    break;
                }
            }
        }


        ///отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);


        if (screenState == SCREEN) {
            batch.draw(imgLongButton[btnSkins.phase], btnSkins.imgX, btnSkins.imgY, btnSkins.imgWidth, btnSkins.imgHeight);
            batch.draw(imgLongButton[btnShots.phase], btnSkins.imgX, btnShots.imgY, btnSkins.imgWidth, btnShots.imgHeight);
            batch.draw(imgLongButton[btnBoosts.phase], btnSkins.imgX, btnBoosts.imgY, btnSkins.imgWidth, btnBoosts.imgHeight);

            btnSkins.font.draw(batch, btnSkins.text, btnSkins.x, btnSkins.y);
            btnShots.font.draw(batch, btnShots.text, btnShots.x, btnShots.y);
            btnBoosts.font.draw(batch, btnBoosts.text, btnBoosts.x, btnBoosts.y);

            for (Shot s : shots) {
                batch.draw(imgShotatlas[main.shotsShots], s.scrX(), s.scrY(), s.width, s.height);
            }

            batch.draw(imgShipatlas[main.shipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);

        }
        if (!(screenState == SCREEN)) {
            if (!((buyShotLevel == maxShot && screenState == SHOTS) || (buySkinLevel == maxSkin && screenState == SKIN) || (buyBoostLevel == maxBoost && screenState == BOOST))) {
                batch.draw(imgBack[btnRight.type], btnRight.imgX, btnRight.imgY, btnRight.imgWidth, btnRight.imgHeight);
            }
            if (!((buyShotLevel == 0 && screenState == SHOTS) || (buySkinLevel == 0 && screenState == SKIN) || (buyBoostLevel == 0 && screenState == BOOST))) {
                batch.draw(imgBack[btnLeft.type], btnLeft.imgX, btnLeft.imgY, btnLeft.imgWidth, btnLeft.imgHeight);
            }
            btnBuy.font.draw(batch, btnBuy.text, btnBuy.x, btnBuy.y);

            for (Shot s : shots) {
                batch.draw(imgShotatlas[shotsShots], s.scrX(), s.scrY(), s.width, s.height);
            }

            batch.draw(imgShipatlas[shipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
        }

        if (timeGreen()) batch.draw(imgGreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        if (timeRed()) batch.draw(imgRed, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, LanguageManager.get("shop"), 330, SCR_HEIGHT - 20);
        btnAllmoney.font.draw(batch, main.allmoney < 1000 ? btnAllmoney.text : main.allmoney / 1000 + "k", btnAllmoney.x, btnAllmoney.y);

        batch.draw(imgBack[btnBack.type], btnBack.imgX, btnBack.imgY, btnBack.imgWidth, btnBack.imgHeight);
        batch.draw(main.screenMenu.imgMN, btnAllmoney.x - 70, btnAllmoney.y - 58, 50, 50);
        if (iswarring) {
            batch.draw(imgWarring, 150, 1410, 600, 170);

            font32.draw(batch, LanguageManager.get("not_enough_money"), 316, 1536, 400, Align.center, true);


        }
        if (ispush) {
            batch.draw(imgPush, 150, 1410, 600, 170);
            if (ispushAgain) {
                font32.draw(batch, LanguageManager.get("purchase_saved"), 316, 1536, 400, Align.center, true);

            } else {
                font32.draw(batch, LanguageManager.get("choice_saved"), 316, 1536, 400, Align.center, true);
            }


        }
        batch.end();





    }


    private boolean timeGreen() {

        return TimeUtils.millis() - timeGreenSpawn <= timeGreen;
    }

    private boolean timeRed() {

        return TimeUtils.millis() - timeRedSpawn <= timeRed;
    }

    public void warring() {
        if (iswarring) {
            if (TimeUtils.millis() > timeLastWarring + timeWarringAndPush) {
                iswarring = false;
            }
        }
        if (ispush) {
            if (TimeUtils.millis() > timeLastPush + timeWarringAndPush) {
                iswarring = false;
                ispushAgain = false;

            }
        }
    }

    private int price() {

        if (screenState == BOOST && !isBoostBuy.get(buyBoostLevel)) {

            return main.basicBoostCoast * (buyBoostLevel);
        }

        if (screenState == SKIN && !isSkinBuy.get(buySkinLevel)) {

            return main.basicSkinCoast * (buySkinLevel);
        }

        if (screenState == SHOTS && !isShotsBuy.get(buyShotLevel)) {

            return main.basicShotCoast * (buyShotLevel);
        }
        return 0;



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

    private void spavnShot() {
        if (TimeUtils.millis() > timeLastSpawnShots + timeShotsInterval) {
            if (shotsBoostCount == 0) {
                shots.add(new Shot(ship.scrX() + ship.width / 2, ship.scrY() + 245));

            }

            for (int i = 0; i < (shotEven == 0 ? shotsBoostCount * 2 : (shotsBoostCount * 2 + 1)); i++) {
                shots.add(new Shot(ship.scrX() + ship.width / 2, ship.scrY() + 245));


            }

            timeLastSpawnShots = TimeUtils.millis();


        }

    }

    public void moveShots() {

        if (shotsBoostCount > 0) {
            int j = -shotsBoostCount;
            for (int r = Math.abs(shotEven == 0 ? shots.size() - shotsBoostCount * 2 : shots.size() - (shotsBoostCount * 2 + 1)); r < shots.size(); r++) {

                shots.get(r).vX = j;
                shots.get(r).move();
                j++;
                if (shotEven == 0) {
                    if (j == 0) j = 1;
                }

            }
            int e = 0;
            while (e < (shotEven == 0 ? shots.size() - shotsBoostCount * 2 : shots.size() - shotsBoostCount * 2 - 1)) {

                shots.get(e).move(shots.get(e).vX);
                e++;

            }
        } else {
            for (Shot s : shots) s.move();
        }
    }


    private void moveShip(SpaceButton b) {
        shipDistance = 0;
        if (b == btnRight) ship.vX = -40;
        else ship.vX = 40;
    }


    private void ChangePlusShots() {

        if (shotsShots <= imgShotatlas.length && screenState == BOOST) {


            if (shotsBoostCount < maxBoost + 1) {


                moveShip(btnRight);
                if (shotEven < shotsBoostCount) shotEven = shotsBoostCount;
                else {
                    shotEven = 0;
                    shotsBoostCount += 1;

                }
                realBoostCount = shotsBoostCount;
                realShotEven = shotEven;
            }

        }


    }

    private void ChangeMinusShots() {
        if (shotsShots <= imgShotatlas.length && screenState == BOOST) {

            if (shotsBoostCount >= 0) {
                if (buyBoostLevel >= 0) {

                    moveShip(btnLeft);
                }
                if (shotEven >= shotsBoostCount) shotEven = 0;
                else {
                    shotEven = shotsBoostCount;
                    shotsBoostCount -= 1;
                }
                realBoostCount = shotsBoostCount;
                realShotEven = shotEven;
            }
        }
    }

    public void moveship(int a) {
        if (shipDistance >= -(SCR_WIDTH + ship.width) && shipDistance <= (SCR_WIDTH + ship.width)) {
            ship.x += ship.vX;
            shipDistance += ship.vX;
            if (screenState == SKIN) {
                if (shipDistance < -SCR_WIDTH + ship.width / 2 && isNewShipSkin) {
                    shipSkin += 1;
                    realShipSkin = shipSkin;
                    isNewShipSkin = false;
                }
                if (shipDistance > SCR_WIDTH - ship.width / 2 && isNewShipSkin) {
                    shipSkin -= 1;
                    realShipSkin = shipSkin;
                    isNewShipSkin = false;
                }

            }
            if (screenState == SHOTS) {
                if (shipDistance < -SCR_WIDTH + ship.width / 2 && isNewShotSkin) {
                    shotsShots += 1;
                    realShotsSkin = shotsShots;
                    isNewShotSkin = false;
                }
                if (shipDistance > SCR_WIDTH - ship.width / 2 && isNewShotSkin) {
                    shotsShots -= 1;
                    realShotsSkin = shotsShots;
                    isNewShotSkin = false;
                }
            }

            if (ship.x < -ship.width / 2) {
                ship.x = SCR_WIDTH + ship.width / 2;
            }
        }
        if (ship.x > SCR_WIDTH + ship.width / 2) {
            ship.x = -ship.width / 2;
        }


    }

    private void ChangeShip(SpaceButton b) {
        if (b == btnRight) {
            if (shipSkin < maxBoost && screenState == SKIN) {
                isNewShipSkin = true;


                moveShip(btnRight);
            }

        }

        if (b == btnLeft) {
            if (shipSkin > 0 && screenState == SKIN) {

                if (buySkinLevel >= 0) {

                    moveShip(btnLeft);
                    if (isNewShipSkin) shipSkin -= 1;
                    isNewShipSkin = true;

                }

            }
        }
    }

    private void ChangeShot(SpaceButton b) {
        if (b == btnRight) {
            if (shotsShots < imgShotatlas.length - 1 && screenState == SHOTS) {
                isNewShotSkin = true;


                moveShip(btnRight);


            }
        }
        if (b == btnLeft) {
            if (shotsShots >= 0 && screenState == SHOTS) {

                if (buyShotLevel >= 0) {

                    isNewShotSkin = true;


                    moveShip(btnLeft);


                }
            }
        }
    }


    private void SaveShop() {


        prefs.putInteger("скин космолета", main.shipSkin);
        prefs.putInteger("скин выстрела", main.shotsShots);
        prefs.putInteger("четность", main.shotEven);
        prefs.putInteger("количество выстрелов", main.shotsBoostCount);
        prefs.putInteger("деньги игрока", main.allmoney);

        StringBuilder sb = new StringBuilder();
        for (Boolean b : isBoostBuy) {
            sb.append(b ? "1" : "0").append(",");
        }
        prefs.putString("boosts", sb.toString());
        StringBuilder sb1 = new StringBuilder();
        for (Boolean b : isSkinBuy) {
            sb1.append(b ? "1" : "0").append(",");
        }
        prefs.putString("skins", sb1.toString());

        StringBuilder sb2 = new StringBuilder();
        for (Boolean b : isShotsBuy) {
            sb2.append(b ? "1" : "0").append(",");
        }
        prefs.putString("shots", sb2.toString());

        prefs.flush();


    }

    private void LoadShop() {

        String saved = prefs.getString("boosts", "");
        if (!saved.isEmpty()) {
            String[] parts = saved.split(",");
            for (int i = 0; i < isBoostBuy.size(); i++) {
                isBoostBuy.set(i, parts[i].equals("1"));
            }
        }

        String saved1 = prefs.getString("skins", "");
        if (!saved1.isEmpty()) {
            String[] parts = saved1.split(",");
            for (int i = 0; i < isSkinBuy.size(); i++) {
                isSkinBuy.set(i, parts[i].equals("1"));
            }
        }

        String saved2 = prefs.getString("shots", "");
        if (!saved2.isEmpty()) {
            String[] parts = saved2.split(",");
            for (int i = 0; i < isShotsBuy.size(); i++) {
                isShotsBuy.set(i, parts[i].equals("1"));
            }
        }
        main.shotsShots = prefs.getInteger("скин выстрела", 0);
        main.shotEven = prefs.getInteger("четность", 0);
        main.shotsBoostCount = prefs.getInteger("количество выстрелов", 0);
        main.shipSkin = prefs.getInteger("скин космолета", 0);


    }

    private void updateLanguage() {

        btnBoosts.changeText(LanguageManager.get("boosts"));
        btnSkins.changeText(LanguageManager.get("skins"));
        btnShots.changeText(LanguageManager.get("shots"));

    }

}



