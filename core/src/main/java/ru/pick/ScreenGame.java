package ru.pick;

import static ru.pick.Main.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    private float JSwidth = SCR_WIDTH / 3, JSheight = SCR_WIDTH / 3;
    //координаты центра джостика
    private final Main main;
    private Levels.Level currentlevel;
    protected final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Vector3 touch;
    private final BitmapFont font70;
    private final BitmapFont font32;
    private InputNumKeyboard keyboard;
    private long timeLastSpawnEnemy;
    private long timeLastSpawnShots;
    private long timeLastSpawnBoost, timeBoostInterval = 9000;
    private long timeGreenSpawn, timeGreen = 700;
    private long timeRedSpawn, timeRed = 700;
    private long timeExplosions;
    private long timeStartGame;
    public int level;
    private float earthAlpha = 1.0f;

    private boolean isShots = true;

    private int emeniesDone = 0;
    private int emeniesCount = 0;
    private int sumCoastEnemyes = 0;


    public static int shotCount;
    private int shotEven;
    private int initialShotCount;
    private int initialEven;
    private int money;
    private String strmoney = money + "";
    private String strKB = "";
    public boolean isgame;
    public int shipSkin;
    public int allmoney;
    public int shotsShots;
    public int moneyFactor;
    public boolean isBoss = true;
    Texture imgJS;
    Texture imgMN;

    Texture imgBG2;
    Texture imgRED;
    Texture imgShipsatlas;
    Texture imgShotsatlas;
    Texture imgFragmentatlas;
    Texture imgEnemyes;
    Texture imgEnemyesBoses;
    Texture imgEnemyesDead;
    Texture imgEnemyesWouded;
    Texture imgEarthAtlas;
    Texture imgLongButtonAtlas;
    Texture imgMinus;
    Texture imgBG;
    Texture imgPlus;
    Texture imgGreen;
    Texture imgGrayBG;
    Texture imgBackAtlas;
    Texture imgLogo;
    Pixmap shipsPixmap;
    Pixmap enemyPixmap;
    Pixmap BossPixmap;
    Pixmap enemyWoudedPixmap;
    Pixmap enemyDeadPixmap;

    TextureRegion[][] imgShipatlas = new TextureRegion[5][12];
    TextureRegion[][] imgEnemy = new TextureRegion[5][12];
    TextureRegion[] imgEnemyBoses = new TextureRegion[6];
    TextureRegion[] imgEnemyDead = new TextureRegion[10];
    TextureRegion[][] imgFragments = new TextureRegion[4][4];
    TextureRegion[] imgShotatlas = new TextureRegion[5];
    TextureRegion[][] imgEnemyWouded = new TextureRegion[5][12];
    TextureRegion[] imgLongButton = new TextureRegion[3];
    TextureRegion[] imgEarth = new TextureRegion[4];
    TextureRegion[] imgBack = new TextureRegion[2];
    SpaceButton btnMoney;
    Sound sndExplosion;
    Sound sndBlaster;

    Music FonMusic;
    SpaceButton btnBack;
    SpaceButton btnGetMoney;
    Ship ship;
    Earth earth;
    GameBackground[] bg = new GameBackground[2];
    List<Enemy> enemies = new ArrayList<>();
    List<Boss> bosses = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    List<Boost> boosts = new ArrayList<>();


    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        moneyFactor = 1;
        LoadGame();
        loadLevel(main.level);
        touch = main.touch;
        font70 = main.font70;
        font32 = main.font32;
        level = main.level;
        FonMusic = main.FonMusic;
        allmoney = main.allmoney;


        Gdx.input.setInputProcessor(new Processor());
        imgMN = new Texture("moneta.png");
        imgJS = new Texture("js.png");
        imgBG = new Texture(currentlevel.backgroundPath);


        imgBG2 = new Texture("bgmenu2.png");
        imgShipsatlas = new Texture("atlas.png");
        imgFragmentatlas = new Texture("fragments.png");
        //imgEnemyes = new Texture(currentlevel.enemyPath);
        imgBackAtlas = new Texture("buttonsLeftRight.png");


        imgEnemyesWouded = new Texture("woundedemenies.png");
        imgEnemyesBoses = new Texture("atlasboss.png");
        imgEnemyesDead = new Texture("emenyesDead.png");
        imgLongButtonAtlas = new Texture("LongButton.png");
        imgEarthAtlas = new Texture("earthatlas.png");
        imgRED = new Texture("red.png");
        imgMinus = new Texture("minus.png");
        imgPlus = new Texture("plus.png");
        imgGreen = new Texture("green.png");
        imgGrayBG = new Texture("GrayBG.png");
        imgShotsatlas = new Texture("shots.png");
        imgLogo = new Texture("logo.png");
        keyboard = new InputNumKeyboard(font70, SCR_WIDTH, SCR_HEIGHT , 10);

        shipsPixmap = new Pixmap(Gdx.files.internal("atlas.png"));
        enemyPixmap = new Pixmap(Gdx.files.internal("enemyes.png"));

        if (!imgEnemyesBoses.getTextureData().isPrepared()) {
            imgEnemyesBoses.getTextureData().prepare();
        }
        BossPixmap = imgEnemyesBoses.getTextureData().consumePixmap();

        if (!imgEnemyesDead.getTextureData().isPrepared()) {
            imgEnemyesDead.getTextureData().prepare();
        }
        enemyDeadPixmap = imgEnemyesDead.getTextureData().consumePixmap();

        for (int j = 0; j < imgShipatlas.length; j++) {
            for (int i = 0; i < imgShipatlas[j].length; i++) {
                imgShipatlas[j][i] = new TextureRegion(imgShipsatlas, (i < 7 ? i : 12 - i) * 800, (j) * 800, 800, 800);
            }
        }


        for (int j = 0; j < imgEnemyWouded.length; j++) {
            for (int i = 0; i < imgEnemyWouded[j].length; i++) {
                imgEnemyWouded[j][i] = new TextureRegion(imgEnemyesWouded, (i < 7 ? i : 12 - i) * 800, (j) * 800, 800, 800);
            }
        }

        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }
        for (int e = 0; e < imgEnemyDead.length; e++) {

            imgEnemyDead[e] = new TextureRegion(imgEnemyesDead, (e) * 450, 0, 450, 450);
        }

        for (int j = 0; j < imgFragments.length; j++) {
            for (int i = 0; i < imgFragments[j].length; i++) {

                imgFragments[j][i] = new TextureRegion(imgFragmentatlas, (i) * 480, (j) * 480, 480, 480);
            }
        }
        for (int i = 0; i < imgShotatlas.length; i++) {
            imgShotatlas[i] = new TextureRegion(imgShotsatlas, (i) * 100, 0, 100, 350);
        }
        for (int e = 0; e < imgEnemyBoses.length; e++) {
            imgEnemyBoses[e] = new TextureRegion(imgEnemyesBoses, (e) * 450, 0, 450, 450);
        }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e) * 193, 497, 193);
        }
        for (int j = 0; j < imgEarth.length; j++) {

            imgEarth[j] = new TextureRegion(imgEarthAtlas, (j) * 740, 0, 740, 740);
        }


        btnBack = new SpaceButton(10, 1500, 90, 90, 0);
        btnGetMoney = new SpaceButton(font70, LanguageManager.get("getandexit"), imgLongButtonAtlas, 260, 1.58f);
        btnMoney = new SpaceButton(font70, strmoney, SCR_WIDTH * 4 / 5, btnBack.y);

        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));
        shipSkin = main.shipSkin;
        shotsShots = main.shotsShots;
        initialShotCount = main.shotsBoostCount;
        initialEven = main.shotEven;
        bg[0] = new GameBackground(0, 0);
        bg[1] = new GameBackground(0, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH / 2, SCR_HEIGHT / 5);


    }


    public void show() {

        gameState=GAME;
        shotCount = main.shotsBoostCount;
        shotEven = main.shotEven;
        timeStartGame = TimeUtils.millis();
        LoadGame();
        loadLevel(main.level);
        imgEnemyes = currentlevel.imgEnemyes;
        for (int j = 0; j < imgEnemy.length; j++) {
            for (int i = 0; i < imgEnemy[j].length; i++) {
                imgEnemy[j][i] = new TextureRegion(imgEnemyes, (i < 7 ? i : 12 - i) * 800, (j) * 800, 800, 800);
            }
        }
        btnGetMoney.changeText(LanguageManager.get("getandexit"));

    }

    @Override
    public void render(float delta) {
        ship.CheckVx = ship.vX;
        ship.type = main.shipSkin;


        SaveGame();
        //касания и управление
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (btnBack.hit(touch.x, touch.y)) {
                main.isPlayMove=false;
                main.setScreen(main.screenMenu);
                FonMusic.stop();
                main.shotsBoostCount = shotCount;
                StopGame();

            }
            if (currentlevel.isTapLevel) {
                for (Enemy e : enemies) {
                    if (e.hit(touch.x, touch.y)) {
                        e.health--;
                        e.isWouded = true;
                        e.timeLastWouded = TimeUtils.millis();
                    }
                }
            }

            if (btnGetMoney.hit(touch.x, touch.y)) {
                if (gameState == GAME_OWER) {
                    main.shotsBoostCount = initialShotCount;
                    main.shotEven = initialEven;
                    GameClear();
                    main.isPlayMove=true;
                }

            }
            if (keyboard.touch(touch.x, touch.y)) strKB = keyboard.getText();
        }
        btnGetMoney.changePhases();
        if (currentlevel.controls == ACCELEROMETER) {
            final float deadzone = 0.2f;
            float rawX = Gdx.input.getAccelerometerX();
            float rawY = Gdx.input.getAccelerometerY();
            if (Math.abs(rawX) <= deadzone) rawX = 0;
            if (Math.abs(rawY) <= deadzone) rawY = 0;
            if (OrientationHelper.getOrientation() == OrientationHelper.ScreenOrientation.LANDSCAPE) {
                ship.vY = -rawX * 4;
                ship.vX = rawY * 3;
            } else {
                ship.vX = -rawX * 4;
                ship.vY = -rawY * 4;
            }
        }
        if (currentlevel.isAccelerometrLevel) {
            final float deadzone = 0.2f;
            float rawX = Gdx.input.getAccelerometerX();
            float rawY = Gdx.input.getAccelerometerY();
            if (Math.abs(rawX) <= deadzone) rawX = 0;
            if (Math.abs(rawY) <= deadzone) rawY = 0;
            for (Shot s : shots) {
                if (OrientationHelper.getOrientation() == OrientationHelper.ScreenOrientation.LANDSCAPE) {

                    s.vY = -rawX * 3;

                } else {

                    s.vY = -rawY * 3;
                }
            }
        }
        if (level == 0) {
            if (earth.overlap(ship)) {

                earth.nPhases = 4;

            }

            earth.move();
        }
        if (currentlevel.isKeyboard) {
            keyboard();

        }

        //события


        if(gameState==GAME){
            gameStart();
        }


        ///bosses

        if (emeniesCount > currentlevel.enemiesMax) {
            for (int j = bosses.size() - 1; j >= 0; j--) {
                if ((TimeUtils.millis() - bosses.get(j).timeLastWouded) > 154) {
                    bosses.get(j).isWouded = false;
                }
                if (bosses.get(j).health == 0) {
                    bosses.get(j).Dead = true;
                    bosses.get(j).vY = -7.69f;
                    // btnMoney.changeText(strmoney);
                    emeniesDone += 1;
                    if (main.isActionSounds) timeExplosions = TimeUtils.millis();
                    bosses.get(j).health = -1;

                }


                if (bosses.get(j).BelowTheScreen()) {
                    money += moneyFactor * 2;

                    bosses.remove(j);

                    break;

                }
                if (bosses.get(j).overlap(ship, ((bosses.get(j).isWouded || bosses.get(j).Dead) ? enemyDeadPixmap : BossPixmap), shipsPixmap)) {
                    emeniesDone = 0;
                    timeRedSpawn = TimeUtils.millis();
                    gameState = GAME_OWER;
                    StopGame();
                    break;
                }
                for (int i = shots.size() - 1; i >= 0; i--) {


                    if (shots.get(i).overlap(bosses.get(j))) {
                        if (bosses.get(j).health > 0) {
                            bosses.get(j).health--;
                            bosses.get(j).isWouded = true;
                            bosses.get(j).timeLastWouded = TimeUtils.millis();

                        }
                        shots.get(i).isoverlab = true;
                        isgame = true;
                    }


                }
            }
        }


        /// enemies
        for (int j = enemies.size() - 1; j >= 0; j--) {
            if (currentlevel.isRexlexLevel && enemies.get(j).y < SCR_HEIGHT / 3)

                enemies.get(j).vY -= 0.15f;

            if ((TimeUtils.millis() - enemies.get(j).timeLastWouded) > 154) {
                enemies.get(j).isWouded = false;
            }
            if (enemies.get(j).health == 0) {
                for (int k = MathUtils.random(2, 9); k >= 0; k--) {
                    fragments.add(new Fragment(enemies.get(j).x, enemies.get(j).y));
                }
                emeniesDone += 1;
                money += moneyFactor;
                enemies.get(j).width = 0;
                sumCoastEnemyes += (4 - enemies.get(j).type) * main.basicSkinCoast;
                timeExplosions = TimeUtils.millis();
                enemies.remove(j);
                break;
            }


            if (enemies.get(j).BelowTheScreen()) {
                if (currentlevel.isRexlexLevel) {
                    emeniesDone += 1;
                    money += moneyFactor;
                } else {
                    {
                        timeRedSpawn = TimeUtils.millis();
                        money -= (4 + moneyFactor);
                    }
                }
                enemies.remove(j);
                break;


            }
            //если наш корабль столкнулся с врагом
            if (enemies.get(j).overlap(ship, enemyPixmap, shipsPixmap)) {
                emeniesDone = 0;
                timeRedSpawn = TimeUtils.millis();
                gameState = GAME_OWER;
                StopGame();
                break;
            }

            for (int i = shots.size() - 1; i >= 0; i--) {

                if (!currentlevel.isTapLevel) {
                    if (!currentlevel.isRexlexLevel) {
                        if (shots.get(i).overlap(enemies.get(j))) {
                            if (enemies.get(j).health > 0) {
                                enemies.get(j).health--;
                                enemies.get(j).isWouded = true;
                                enemies.get(j).timeLastWouded = TimeUtils.millis();

                            }
                            shots.get(i).isoverlab = true;
                            isgame = true;
                        }

                    } else {
                        shots.get(i).vY = -16;
                    }
                }
            }
        }

        if (TimeUtils.millis() == timeExplosions) {

            sndExplosion.play();
        }
        for (int i = boosts.size() - 1; i >= 0; i--) {
            if (ship.overlap(boosts.get(i))) {

                if (boosts.get(i).type == 1) {
                    timeGreenSpawn = TimeUtils.millis();

                    if (shotCount < 4) {
                        if (shotEven < shotCount) {
                            shotEven = shotCount;
                        }
                        else {
                            shotEven = 0;
                            shotCount += 1;

                        }

                }



                } else {
                    timeRedSpawn = TimeUtils.millis();

                    shotCount = 0;
                    shotEven = 0;
                }

                boosts.remove(i);
            }
        }


        /// delete
        for (int i = shots.size() - 1; i >= 0; i--) {
            if (currentlevel.isRexlexLevel) {
                if (ship.overlap(shots.get(i))) {
                    shots.get(i).width = 0;
                    shots.get(i).height = 0;
                    gameState = GAME_OWER;


                }
                if (shots.get(i).OutOfscreen()) {
                    shots.get(i).width = 0;
                    shots.get(i).height = 0;

                    if (TimeUtils.millis() >= timeLastSpawnShots + currentlevel.timeShotsInterval) {
                        shots.remove(i);
                        break;
                    }


                }

            } else {
                if (shots.get(i).isoverlab || shots.get(i).OutOfscreen()) {
                    shots.get(i).width = 0;
                    shots.get(i).height = 0;
                    if (TimeUtils.millis() >= timeLastSpawnShots + currentlevel.timeShotsInterval) {
                        shots.remove(i);
                        break;
                    }
                }
            }
        }
        for (int i = boosts.size() - 1; i >= 0; i--) {
            if (boosts.get(i).OutOfscreen()) boosts.remove(i);
        }

        for (int e = fragments.size() - 1; e >= 0; e--) {
            fragments.get(e).move();
            if (fragments.get(e).OutOfscreen()) fragments.remove(e);
        }

        if (enemies.isEmpty() && bosses.isEmpty() && isgame && emeniesCount == currentlevel.enemiesMax + currentlevel.bossCount) {
            timeGreenSpawn = TimeUtils.millis();
            gameState = GAME_OWER;
            StopGame();

        }
        ship.rotationSpeed = (ship.vX - ship.CheckVx) * 100;
        ship.type = main.shipSkin;
        strmoney = money + "";
        btnMoney.changeText(strmoney);


        /// draw
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (GameBackground bg : bg) {
            batch.draw(imgBG, bg.x, bg.y, bg.width, bg.height);
        }
        if (level == 0) {
            if (earth.nPhases == 1) {
                batch.draw(imgEarth[earth.phase], earth.scrX(), earth.scrY(), earth.width, earth.height);
            } else {
                earthAlpha -= 0.004f; // Уменьшаем прозрачность каждый кадр
                if (earthAlpha < 0) {
                    earthAlpha = 0;
                    isShots = true;
                }

                batch.setColor(1, 1, 1, earthAlpha);
                batch.draw(imgEarth[earth.phase], earth.scrX(), earth.scrY(), earth.width, earth.height);
                batch.setColor(1, 1, 1, 1); // Сбрасываем настройки цвета
            }
        }
        for (Fragment f : fragments) {
            batch.draw(imgFragments[f.type1][f.type2], f.scrX(), f.scrY(), f.width, f.height);
        }
        for (Enemy e : enemies) {
            if (e.isWouded) {
                batch.draw(imgEnemyWouded[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);
            } else batch.draw(imgEnemy[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);

        }
        for (Boss e : bosses) {
            if (e.Dead || e.isWouded) {
                batch.draw(imgEnemyDead[e.phase], e.scrX(), e.scrY(), e.width / 2, e.height / 2, e.width, e.height, 1, 1, e.rotation);
            } else {
                batch.draw(imgEnemyBoses[e.phase], e.scrX(), e.scrY(), e.width / 2, e.height / 2, e.width, e.height, 1, 1, e.rotation);
            }
        }

        for (Shot s : shots) {
            batch.draw(imgShotatlas[main.shotsShots], s.scrX(), s.scrY(), s.width, s.height);
        }
        for (Boost b : boosts) {
            batch.draw(b.type == 1 ? imgPlus : imgMinus, b.scrX(), b.scrY(), b.width, b.height);
        }

        batch.draw(imgShipatlas[main.shipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width / 2, ship.height / 2, ship.width, ship.height, 1, 1, ship.rotation);

        if (gameState == GAME_OWER) {
            batch.draw(imgGrayBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }
        if (timeRed()) {
            batch.draw(imgRED, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }
        if (timeGreen()) {
            batch.draw(imgGreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }

        if (currentlevel.controls == JOYSTIK_LEFT) {
            batch.draw(imgJS, 0, 0, SCR_WIDTH / 3, SCR_WIDTH / 3);
        }
        if (currentlevel.controls == JOYSTIK_RIGHT) {
            batch.draw(imgJS, 2 * SCR_WIDTH / 3, 0, SCR_WIDTH / 3, SCR_WIDTH / 3);
            JSwidth = (SCR_WIDTH - SCR_WIDTH / 6) * 2;
        }

        keyboard.draw(batch);

        batch.draw(imgBack[btnBack.type], btnBack.imgX, btnBack.imgY, btnBack.imgWidth, btnBack.imgHeight);
        btnMoney.font.draw(batch, btnMoney.text, btnMoney.x, btnMoney.y);
        batch.draw(imgMN, btnMoney.x - 70, btnMoney.y - 58, 50, 50);


        //если игра завершена
        if (gameState == GAME_OWER) {

            batch.draw(imgBG2, 0, 0, SCR_WIDTH, SCR_HEIGHT);
            batch.draw(imgLogo, SCR_WIDTH / 2 - 240, 1200, 480, 390);
            font70.draw(batch, !iscomplited() ? LanguageManager.get("gameover") : LanguageManager.get("levelcomplited"), 0, 900, SCR_WIDTH, Align.center, true);
            if (money%10 == 1&&money%100!=11) {
                font70.draw(batch, LanguageManager.get("youcollected") + " " + (Math.max(money, 0)) + " " + LanguageManager.get("coinsY"), 0, 600, SCR_WIDTH, Align.center, true);

            }
            else if (money %10>= 2&&money %10<= 4&&!(money %100>= 12&&money %100<= 14)) {
                font70.draw(batch, LanguageManager.get("youcollected") + " " + (Math.max(money, 0)) + " " + LanguageManager.get("coinsI"), 0, 600, SCR_WIDTH, Align.center, true);

            }
            else  {
                font70.draw(batch, LanguageManager.get("youcollected") + " " + (Math.max(money, 0)) + " " + LanguageManager.get("coins"), 0, 600, SCR_WIDTH, Align.center, true);

            }
            batch.draw(imgLongButton[btnGetMoney.phase], btnGetMoney.imgX, btnGetMoney.imgY, btnGetMoney.imgWidth, btnGetMoney.imgHeight);
            btnGetMoney.font.draw(batch, btnGetMoney.text, btnGetMoney.x, btnGetMoney.y);
        }


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
        shipsPixmap.dispose();
        enemyPixmap.dispose();
        enemyDeadPixmap.dispose();
        enemyWoudedPixmap.dispose();
        BossPixmap.dispose();

    }


    public void spavnEnemy() {
        long currentTime = TimeUtils.millis();

        // Проверяем, что прошло время до первого спавна
        if (currentTime - timeStartGame < currentlevel.timeFirstSpawnEnemy) {
            return;
        }

        // Проверяем интервал между спавнами
        if (currentTime - timeLastSpawnEnemy >= currentlevel.timeSpawnInterval) {
            if (emeniesCount < currentlevel.enemiesMax) {
                enemies.add(new Enemy());
                emeniesCount++;
                timeLastSpawnEnemy = currentTime; // Фиксируем время последнего спавна


            }
        }

    }

    public void spavnBoss() {
        long currentTime = TimeUtils.millis();

        // Проверяем, что прошло время до первого спавна
        if (currentTime - timeStartGame < currentlevel.timeFirstSpawnEnemy || !isBoss) {
            return;
        }

        // Проверяем интервал между спавнами
        if ((currentTime - timeLastSpawnEnemy >= currentlevel.timeSpawnInterval)) {
            if (enemies.isEmpty() && emeniesCount < currentlevel.enemiesMax + currentlevel.bossCount) {

                bosses.add(new Boss());
                timeLastSpawnEnemy = currentTime; // Фиксируем время последнего спавна
                emeniesCount++;

            }
        }


    }

    public void spavnShot() {
        if ((TimeUtils.millis() > timeLastSpawnShots + currentlevel.timeShotsInterval) && isShots) {

            if (shotCount == 0) {
                shots.add(new Shot(ship.scrX() + ship.width / 2 - (ship.rotation) * ship.width / 90, ship.scrY() + 0.85f * ship.height));


            }

            for (int i = 0; i < (shotEven == 0 ? shotCount * 2 :( shotCount * 2 + 1)); i++) {
                shots.add(new Shot(ship.scrX() + ship.width / 2 - (ship.rotation) * ship.width / 90, ship.scrY() + 245));


            }


            timeLastSpawnShots = TimeUtils.millis();
            timeLastSpawnShots = TimeUtils.millis();
            if (main.isActionSounds) sndBlaster.play();


        }


    }

    public void spavnShotLevel6() {
        if ((TimeUtils.millis() > timeLastSpawnShots + currentlevel.timeShotsInterval)) {
            for (Enemy e : enemies) {

                if (e.y > SCR_HEIGHT / 3) {
                    e.vY -= 0.1f;

                    if (shotCount == 0) {
                        shots.add(new Shot(e.scrX() + e.width / 2, e.scrY()));


                    }

                    for (int i = 0; i < (shotEven == 0 ? shotCount * 2 : (shotCount * 2 + 1)); i++) {
                        shots.add(new Shot(e.scrX() + e.width / 2, e.scrY()));


                    }
                    timeLastSpawnShots = TimeUtils.millis();
                    if (main.isActionSounds) sndBlaster.play();


                }


            }
        }


    }

    private void MoveShots() {

        if (!shots.isEmpty()) {


            int j = -shotCount;
            for (int r = (shotEven == 0 ? shots.size() - shotCount * 2 : shots.size() - (shotCount * 2 + 1)); r < shots.size(); r++) {


                shots.get(r).vX = j;

                shots.get(r).move();
                j++;
                if (shotEven == 0) {

                    if (j == 0) j = 1;
                }


            }


            int e = 0;
            while (e < (shotEven == 0 ? shots.size() - shotCount * 2 : shots.size() - shotCount * 2 - 1)) {

                shots.get(e).move(shots.get(e).vX);
                e++;

            }


        }
    }


    public void keyboard() {
        if (enemies.isEmpty() && emeniesCount >= currentlevel.enemiesMax) {
            System.out.println("" + sumCoastEnemyes);
            if (!keyboard.isKeyboardShow) {

                if (strKB.isEmpty()) keyboard.start();


            }
            if (strKB.equals("" + sumCoastEnemyes)) {
                isBoss = true;
                timeGreenSpawn = TimeUtils.millis();
                strKB = "a";


            } else if (!strKB.isEmpty() && !isBoss) {
                sumCoastEnemyes = 0;
                gameState = GAME_OWER;
                timeRedSpawn = TimeUtils.millis();
            }


            // if (keyboard.isKeyboardShow) {


            // }
        }
    }


    public void spavnBoost() {
        if (TimeUtils.millis() > timeLastSpawnBoost + timeBoostInterval) {
            boosts.add(new Boost());
            timeLastSpawnBoost = TimeUtils.millis();
        }


    }

    public void StopGame() {
        sndExplosion.stop();
        FonMusic.stop();
        for (Enemy e : enemies) e.stop();
        for (Boost b : boosts) b.stop();
        for (Shot s : shots) s.stop();
        for (Fragment f : fragments) f.stop();
        SaveGame();
    }


    private void loadLevel(int level) {


        currentlevel = Levels.LEVELS[level];
        if (currentlevel.controls == ACCELEROMETER || currentlevel.isAccelerometrLevel) {
            OrientationHelper.lockCurrentOrientation();
        } else {
            OrientationHelper.unlock(); // Разрешаем автоповорот
        }
        if (level == 0) {
            isShots = false;
            earth = new Earth(SCR_WIDTH / 2, 1120);
        }
        isShots = currentlevel.isShots;
        isBoss = currentlevel.isBoss;
        if (currentlevel.isKeyboard) {
            strKB = "";

        }
    }


    public void GameClear() {

        if (money > 0) {
            main.allmoney += money;
            main.player.money = main.allmoney;
        }

        if (iscomplited() && main.level <= currentlevel.MaxLevel) {
            level += 1;
            main.level = level;
            main.player.level = level;
        }
        loadLevel(main.level);


        SaveGame();
        money = 0;
        btnMoney.changeText(strmoney);
        main.setScreen(main.screenMenu);
        FonMusic.stop();
        enemies.clear();
        bosses.clear();
        boosts.clear();
        shots.clear();
        fragments.clear();
        ship.x = SCR_WIDTH / 2;
        ship.y = SCR_HEIGHT / 5;
        shotCount = 0;
        ship.rotationSpeed = 0;
        ship.rotation = 0;

        emeniesCount = 0;
        emeniesDone = 0;


    }


    public boolean iscomplited() {
        return (bosses.isEmpty() && enemies.isEmpty() && emeniesCount >= currentlevel.enemiesMax + currentlevel.bossCount);


    }

    public void gameStart() {




        for (GameBackground bg : bg) bg.move();
        spavnEnemy();
        if (currentlevel.isRexlexLevel) spavnShotLevel6();
        else spavnShot();
        spavnBoss();

        for (Enemy e : enemies) {
            e.move();

        }
        for (Boss b : bosses) {
            b.move();
        }
        spavnBoost();
        for (Boost b : boosts) b.move();

        ship.move();


        if (shotCount == 0) {
            for (Shot s : shots) s.move();
        }

        if (shotCount > 0) {
            MoveShots();

        }


    }

    private void SaveGame() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

        prefs.putInteger("деньги игрока", main.allmoney);
        prefs.putInteger("игровой уровень", main.level);

        prefs.flush();
    }

    private void LoadGame() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

        main.allmoney = prefs.getInteger("деньги игрока", main.allmoney);
        main.level = prefs.getInteger("игровой уровень", main.level);


    }

    private boolean timeRed() {

        return TimeUtils.millis() - timeRedSpawn <= timeRed;
    }

    private boolean timeGreen() {

        return TimeUtils.millis() - timeGreenSpawn <= timeGreen;
    }

    class Processor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }


        @Override
        public boolean keyUp(int keycode) {
            return false;
        }


        @Override
        public boolean keyTyped(char character) {
            return false;
        }


        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (gameState == GAME) {
                if (currentlevel.controls == SCREEN) {
                    touch.set(screenX, screenY, 0);
                    camera.unproject(touch);
                    ship.touch(touch);

                    ship.rotation = 0;
                    // ship.CheckVx=ship.vX;
                    ship.move();
                    ship.vY /= 30;
                    ship.vY /= 70;


                }
                if (currentlevel.controls == JOYSTIK_LEFT || controls == JOYSTIK_RIGHT) {
                    touch.set(screenX, screenY, 0);
                    camera.unproject(touch);
                    //проверяем попали ли мы касанием в круг используя формулу графика окружности
                    if (Math.pow(touch.x - JSwidth / 2, 2) + Math.pow(touch.y - JSheight / 2, 2) <= Math.pow(JSwidth / 2, 2)) {
                        ship.vX = (touch.x - JSwidth / 2) / 19;

                        ship.vY = (touch.y - JSheight / 2) / 19;
                    } else {
                        ship.stop();
                        ship.vX = ship.vY = 0;
                    }

                }
            }

            return false;

        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            ship.rotation = 0;

            ship.stop();
            return false;
        }


        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }


        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (gameState == GAME) {
                if (currentlevel.controls == SCREEN) {
                    touch.set(screenX, screenY, 0);
                    camera.unproject(touch);
                    ship.touch(touch);
                    ship.move();
                }
                if (currentlevel.controls == JOYSTIK_LEFT || currentlevel.controls == JOYSTIK_RIGHT) {
                    touch.set(screenX, screenY, 0);
                    camera.unproject(touch);
                    //проверяем попали ли мы касанием в круг
                    if (Math.pow(touch.x - JSwidth / 2, 2) + Math.pow(touch.y - JSheight / 2, 2) <= Math.pow(150, 2)) {
                        ship.vX = (touch.x - JSwidth / 2) / 19;
                        ship.vY = (touch.y - JSheight / 2) / 19;
                    } else {
                        ship.vX = (touch.x - JSwidth / 2) / 25;
                        ship.vY = (touch.y - JSheight / 2) / 25;

                    }
                }
                // }


            }
            // }

            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }


        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }


}






