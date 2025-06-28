package ru.pick;

import static ru.pick.Main.SCREEN;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenShop implements Screen {
    private static int SHOP = 0, SKIN = 1, SHOTS = 2, BOOST = 3;
    private long timeLastSpawnShots, timeShotsInterval = 300;
    private int shotEven = 0;
    public int shipSkin = 0;
    public int shotsShots;
    public int shotsBoostCount;
    private int shipDistance;
    private int screenShipNum = 0;
    private int screenBoostNum = 0;
    private int screenSHOTSNum = 0;
    private int buyBoostLevel = 0;
    private int buyShotLevel = 0;
    private int buyShipLevel = 0;
    private int realBoostCount;
    private int realShotEven;
    private int realShotsSkin;
    private int realShipSkin;
    private int minBoostLevel = 0;
    private int minSkinLevel = 0;
    private int minShotsLevel = 0;
    private int screenState = SHOP;
    private boolean isNewShipSkin;
    private boolean isNewShotSkin;
    private long timeRedSpawn, timeRed = 700;
    private long timeGreenSpawn, timeGreen = 700;
    private long timeWarring = 1100, timeLastWarring;
    private boolean iswarring = false;
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    private BitmapFont font32;


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

    private List<Shot> shots = new ArrayList<>();

    public ScreenShop(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font70;
        font32 = main.font32;
        imgBackAtlas = new Texture("buttonsLeftRight.png");

        imgGreen = new Texture("green.png");
        imgRed = new Texture("red.png");
        imgBG = new Texture("bgshop.png");

        imgLongButtonAtlas = new Texture("LongButton.png");
        btnAllmoney = new SpaceButton(font, "" + (main.allmoney >= 1000 ? main.allmoney : main.allmoney / 1000 + 'k'), SCR_WIDTH - 120, 1550);
        btnBack = new SpaceButton(10, 1500, 90, 90, 0);
        imgWarring = new Texture("warring.png");

        btnBoosts = new SpaceButton(font, LanguageManager.get("boosts"), imgLongButtonAtlas, 325, 3.8f);

        btnSkins = new SpaceButton(font, LanguageManager.get("skins"), imgLongButtonAtlas, 500, 4.8f);

        btnShots = new SpaceButton(font, LanguageManager.get("shots"), imgLongButtonAtlas, 150, 4.8f);


        btnBuy = new SpaceButton(font, LanguageManager.get("buyfor") + " " + price() + " " + LanguageManager.get("coins"), 255);
        btnLeft = new SpaceButton(10, SCR_HEIGHT / 2, 100, 100, 0);
        ;
        btnRight = new SpaceButton(SCR_WIDTH - 100, SCR_HEIGHT / 2, 100, 100, 1);


        imgShipsatlas = new Texture("atlas.png");
        imgShotsatlas = new Texture("shots.png");
        imgPlus = new Texture("plus.png");


        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }

        for (int j = 0; j < imgShipatlas.length; j++) {
            for (int i = 0; i < imgShipatlas[j].length; i++) {
                imgShipatlas[j][i] = new TextureRegion(imgShipsatlas, (i < 7 ? i : 12 - i) * 800, j * 800, 800, 800);
            }
        }

        for (int i = 0; i < imgShotatlas.length; i++) {
            imgShotatlas[i] = new TextureRegion(imgShotsatlas, (i) * 100, 0, 100, 350);
        }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e) * 193, 497, 193);
        }


        ship = new Ship(SCR_WIDTH / 2, SCR_HEIGHT / 2);
        ship.width = ship.height = 460;
        ship.vY = 0;
        LoadShop();

    }

    @Override
    public void show() {

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


        if (btnBack.setScreenButton) {
            if (screenState == SHOP) main.setScreen(main.screenMenu);
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
        if (btnRight.setScreenButton && ship.x == SCR_WIDTH / 2) {

            if (screenState == BOOST) {
                ChangePlusShots();

            }
            if (screenState == SKIN) {
                ChangeShip(btnRight);

            }

            if (screenState == SHOTS) {
                ChangeShot(btnRight);

            }
        }
        if (btnLeft.setScreenButton && ship.x == SCR_WIDTH / 2) {

            if (screenState == BOOST) {
                ChangeMinusShots();

            }
            if (screenState == SKIN) {
                ChangeShip(btnLeft);

            }
            if (screenState == SHOTS) {
                ChangeShot(btnLeft);


            }
        }
        //код временно выполнен в индийском стиле
        if (btnBuy.setScreenButton) {
            if (main.allmoney >= price()) {


                if (screenState == BOOST) {
                    if (screenBoostNum <= 1 && price() != 0) {
                        minBoostLevel -= 1;
                        timeGreenSpawn = TimeUtils.millis();
                        main.allmoney -= price();
                        buyBoostLevel = 0;
                        screenBoostNum = 0;
                        main.shotsBoostCount = shotsBoostCount;
                        main.shotEven = shotEven;
                    }
                    if (price() == 0) {
                        main.shotsBoostCount = shotsBoostCount;
                        main.shotEven = shotEven;
                        timeGreenSpawn = TimeUtils.millis();
                    }
                    if (screenBoostNum > 1) {
                        timeRedSpawn = TimeUtils.millis();
                        iswarring = true;
                        timeLastWarring = TimeUtils.millis();
                    }
                }
                if (screenState == SKIN) {
                    if (screenShipNum == 1 && price() != 0) {
                        minSkinLevel -= 1;
                        buyShipLevel = 0;
                        timeGreenSpawn = TimeUtils.millis();
                        main.allmoney -= price();
                        screenShipNum = 0;
                        main.shipSkin = shipSkin;
                    }
                    if (price() == 0) {
                        main.shipSkin = shipSkin;
                        timeGreenSpawn = TimeUtils.millis();
                    }
                    if (screenShipNum > 1) {
                        timeRedSpawn = TimeUtils.millis();
                        iswarring = true;
                        timeLastWarring = TimeUtils.millis();
                    }


                }
                if (screenState == SHOTS) {
                    if (screenSHOTSNum == 1 && price() != 0) {
                        buyShotLevel = 0;
                        minShotsLevel -= 1;
                        timeGreenSpawn = TimeUtils.millis();
                        main.allmoney -= price();
                        screenSHOTSNum = 0;
                        main.shotsShots = shotsShots;
                    }
                    if (price() == 0) {
                        main.shotsShots = shotsShots;
                        timeGreenSpawn = TimeUtils.millis();
                    }

                    if (screenSHOTSNum > 1) {
                        timeRedSpawn = TimeUtils.millis();
                        iswarring = true;
                        timeLastWarring = TimeUtils.millis();
                    }


                }


            } else {
                timeRedSpawn = TimeUtils.millis();
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

        MoveShots();

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
            batch.draw(imgBack[btnRight.type], btnRight.imgX, btnRight.imgY, btnRight.imgWidth, btnRight.imgHeight);
            batch.draw(imgBack[btnLeft.type], btnLeft.imgX, btnLeft.imgY, btnLeft.imgWidth, btnLeft.imgHeight);
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

            font32.draw(batch, LanguageManager.get("make_purchases_in_order"), 301, 1536, 400, Align.center, true);


        }
        batch.end();


        SaveShop();


    }

    private boolean timeGreen() {

        return TimeUtils.millis() - timeGreenSpawn <= timeGreen;
    }

    private boolean timeRed() {

        return TimeUtils.millis() - timeRedSpawn <= timeRed;
    }

    public void warring() {
        if (iswarring) {
            if (TimeUtils.millis() > timeLastWarring + timeWarring)
                iswarring = false;
        }
    }

    private int price() {

        if (screenState == BOOST && buyBoostLevel > 0)
            return main.basicBoostCoast * (buyBoostLevel + Math.abs(minBoostLevel));
        if (screenState == SKIN && buyShipLevel > 0)
            return main.basicSkinCoast * (buyShipLevel + Math.abs(minSkinLevel));
        if (screenState == SHOTS && buyShotLevel > 0)
            return main.basicShotCoast * (buyShotLevel + Math.abs(minShotsLevel));
        else return 0;


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

    public void MoveShots() {

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


    private void MoveShip(SpaceButton b) {
        shipDistance = 0;
        if (b == btnRight) ship.vX = -40;
        else ship.vX = 40;
    }


    private void ChangePlusShots() {

        if (shotsShots <= imgShotatlas.length && screenState == BOOST) {


            if (shotsBoostCount < 4) {
                buyBoostLevel += 1;
                screenBoostNum += 1;
                MoveShip(btnRight);
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
                if (buyBoostLevel > minBoostLevel) {
                    buyBoostLevel -= 1;
                    screenBoostNum -= 1;
                    MoveShip(btnLeft);
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
        ship.changePhase();


    }

    private void ChangeShip(SpaceButton b) {
        if (b == btnRight) {
            if (shipSkin < imgShipatlas.length - 1 && screenState == SKIN) {
                isNewShipSkin = true;
                buyShipLevel += 1;
                screenShipNum += 1;
                MoveShip(btnRight);
            }

        }

        if (b == btnLeft) {
            if (shipSkin > 0 && screenState == SKIN) {

                if (buyShipLevel >= minSkinLevel) {
                    buyShipLevel -= 1;
                    screenShipNum -= 1;
                    MoveShip(btnLeft);
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

                buyShotLevel += 1;
                screenSHOTSNum += 1;
                MoveShip(btnRight);


            }
        }
        if (b == btnLeft) {
            if (shotsShots >= 0 && screenState == SHOTS) {

                if (buyShotLevel > minShotsLevel) {

                    isNewShotSkin = true;
                    buyShotLevel -= 1;
                    screenSHOTSNum -= 1;

                    MoveShip(btnLeft);


                }
            }
        }
    }


    private void SaveShop() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

        prefs.putInteger("скин космолета", main.shipSkin);
        prefs.putInteger("скин выстрела", main.shotsShots);
        prefs.putInteger("четность", main.shotEven);
        prefs.putInteger("количество выстрелов", main.shotsBoostCount);
        //  prefs.putInteger();
        prefs.flush();
    }

    private void LoadShop() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

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



