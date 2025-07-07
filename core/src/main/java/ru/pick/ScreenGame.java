package ru.pick;

import static ru.pick.Main.ACCELEROMETER;
import static ru.pick.Main.GAME;
import static ru.pick.Main.GAME_OVER;
import static ru.pick.Main.JOYSTIK_LEFT;
import static ru.pick.Main.JOYSTIK_RIGHT;
import static ru.pick.Main.SCREEN;
import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;
import static ru.pick.Main.gameState;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
    private Joystick joystick;
    private final OrthographicCamera camera;
    private final Vector3 touch;
    private final BitmapFont font70;
    private final BitmapFont font32;
    private InputNumKeyboard keyboard;
    private AssetManager manager;


    private long timeLastSpawnEnemy;
    private long timeLastSpawnShots;
    private long timeLastSpawnBoost;
    private long timeGreenSpawn, timeGreen = 700;
    private long timeRedSpawn, timeRed = 700;
    private long timeExplosions;
    private long timeStartGame;
    private long timeRenderInterval = 7, timeLastRender;
    public int level;

    private float earthAlpha = 1.0f;

    private boolean isShots = true;

    private int emeniesDone = 0;
    private int emeniesCount = 0;
    private int sumCoastEnemyes = 0;


    public static int shotCount;
    private boolean isShield;
    private boolean isShieldWarring;
    private boolean isRocket = false;
    private boolean isOnStoneRight, isOnStoneLeft, isOnStoneBottom, isOnStoneTop;
    private long timeUseOnShield, timeShield = 9000, timeShieldWarring = 2000;

    private int shotEven;
    private int initialShotCount;
    private long timeLastSpawnStones, timeStonesInterwal = 29000;
    private long timeLastSpawnRocket;
    private long timeLastSpawnNums;
    private long timeLastSpawnShield;
    private int initialEven;
    private int money;
    private float newRS;
    private String strmoney = money + "";
    private String strKB = "";

    public boolean isgame;
    public int shipSkin;
    public int allmoney;
    public int shotsShots;
    public int moneyFactor;
    private int numInt = 0;
    public boolean isBoss = true;

    Texture imgMN;
    Texture imgStone;
    Texture joystickBase, joystickStick;
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
    Texture imgRocketAtlas;
    Texture imgLogo;
    Texture imgShieldBoost;
    Texture imgRocketBoost;
    Texture imgShiledShipAtlas;
    Texture imgShiledShipWarringAtlas;
    Texture imgNumsAtlas;
    Pixmap shipsPixmap;
    Pixmap enemyPixmap;
    Pixmap bossPixmap;
    Pixmap enemyWoudedPixmap;
    Pixmap enemyDeadPixmap;
    Pixmap rocketPixmap;
    Pixmap stonePixmap;
    Pixmap numPixmap;
    TextureRegion[] imgRocket = new TextureRegion[1];
    TextureRegion[][] imgShipatlas = new TextureRegion[5][9];
    TextureRegion[][] imgShiledShip = new TextureRegion[5][8];
    TextureRegion[][] imgShiledShipWarring = new TextureRegion[5][8];
    TextureRegion[][] imgEnemy = new TextureRegion[5][12];
    TextureRegion[] imgEnemyBoses = new TextureRegion[6];
    TextureRegion[] imgNum = new TextureRegion[10];
    TextureRegion[] imgEnemyDead = new TextureRegion[10];
    TextureRegion[][] imgFragments = new TextureRegion[4][4];
    TextureRegion[] imgShotatlas = new TextureRegion[5];
    TextureRegion[][] imgEnemyWouded = new TextureRegion[5][6];
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
    List<Boost> nums = new ArrayList<>();
    List<Boost> shieldsBoosts = new ArrayList<>();
    List<Boost> rocketsBoost = new ArrayList<>();
    List<Rocket> rockets = new ArrayList<>();
    List<Stone> stones = new ArrayList<>();

    public ScreenGame(Main main) {
        this.main = main;
        this.manager = main.manager;
        batch = main.batch;
        camera = main.camera;


        moneyFactor = 5;
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

        imgMN = manager.get("moneta.png", Texture.class);

        imgBG = manager.get(currentlevel.backgroundPath, Texture.class);
        imgShieldBoost = manager.get("shieldBoost.png", Texture.class);
        imgRocketBoost = manager.get("rocketBoost.png", Texture.class);
        imgShiledShipAtlas = new Texture("shieldShip.png");
        imgRocketAtlas = manager.get("rocket.png", Texture.class);
        imgShiledShipWarringAtlas = new Texture("shieldWarringShip.png");
        imgBG2 = manager.get("bgmenu2.png", Texture.class);
        imgShipsatlas = new Texture("atlas.png");
        imgFragmentatlas = manager.get("fragments.png", Texture.class);
//imgEnemyes = manager.get(currentlevel.enemyPath, Texture.class); // Раскомментировать если нужно
        imgBackAtlas = manager.get("buttonsLeftRight.png", Texture.class);
        imgStone = manager.get("stone.png", Texture.class);
        imgNumsAtlas = manager.get("nums.png", Texture.class);

        joystickBase = manager.get("jsBase.png", Texture.class);
        joystickStick = manager.get("jsStick.png", Texture.class);
        imgEnemyesWouded = new Texture("woundedemenies.png");
        imgEnemyesBoses = new Texture("atlasboss.png");
        imgEnemyesDead = new Texture("enemyesDead.png");
        imgLongButtonAtlas = manager.get("longButton.png", Texture.class);
        imgEarthAtlas = manager.get("earthatlas.png", Texture.class);
        imgRED = manager.get("red.png", Texture.class);
        imgMinus = manager.get("minus.png", Texture.class);
        imgPlus = manager.get("plus.png", Texture.class);
        imgGreen = manager.get("green.png", Texture.class);
        imgGrayBG = manager.get("grayBG.png", Texture.class);
        imgShotsatlas = manager.get("shots.png", Texture.class);
        imgLogo = manager.get("logo.png", Texture.class);


        keyboard = new InputNumKeyboard(font70, SCR_WIDTH, SCR_HEIGHT, 10);

        shipsPixmap = new Pixmap(Gdx.files.internal("atlas.png"));
        enemyPixmap = new Pixmap(Gdx.files.internal("enemyes.png"));
        bossPixmap = new Pixmap(Gdx.files.internal("atlasboss.png"));
        enemyDeadPixmap = new Pixmap(Gdx.files.internal("enemyesDead.png"));
        rocketPixmap = new Pixmap(Gdx.files.internal("rocket.png"));
        stonePixmap = new Pixmap(Gdx.files.internal("stone.png"));
        numPixmap = new Pixmap(Gdx.files.internal("black.png"));

        for (int j = 0; j < imgShipatlas.length; j++) {
            for (int i = 0; i < imgShipatlas[j].length; i++) {
                imgShipatlas[j][i] = new TextureRegion(imgShipsatlas, i * 400, (j) * 500, 400, 500);
            }
        }
        for (int j = 0; j < imgShiledShip.length; j++) {
            for (int i = 0; i < imgShiledShip[j].length; i++) {
                imgShiledShip[j][i] = new TextureRegion(imgShiledShipAtlas, (i) * 400, (j) * 500, 400, 500);
            }
        }
        for (int j = 0; j < imgShiledShipWarring.length; j++) {
            for (int i = 0; i < imgShiledShipWarring[j].length; i++) {
                imgShiledShipWarring[j][i] = new TextureRegion(imgShiledShipWarringAtlas, (i) * 400, (j) * 500, 400, 500);
            }
        }
        for (int e = 0; e < imgNum.length; e++) {

            imgNum[e] = new TextureRegion(imgNumsAtlas, (e) * 400, 0, 400, 400);
        }
        for (int e = 0; e < imgRocket.length; e++) {

            imgRocket[e] = new TextureRegion(imgRocketAtlas, 0, 0, 500, 500);
        }
        for (int j = 0; j < imgEnemyWouded.length; j++) {
            for (int i = 0; i < imgEnemyWouded[j].length; i++) {
                imgEnemyWouded[j][i] = new TextureRegion(imgEnemyesWouded, (i) * 400, (j) * 500, 400, 500);
            }
        }

        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }
        for (int e = 0; e < imgEnemyDead.length; e++) {

            imgEnemyDead[e] = new TextureRegion(imgEnemyesDead, (e) * 1025, 0, 1025, 1025);
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
            imgEnemyBoses[e] = new TextureRegion(imgEnemyesBoses, (e) * 1025, 0, 1025, 1025);
        }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e) * 193, 497, 193);
        }
        for (int j = 0; j < imgEarth.length; j++) {

            imgEarth[j] = new TextureRegion(imgEarthAtlas, (j) * 740, 0, 740, 740);
        }


        btnBack = new SpaceButton(10, 1500, 90, 90, 0);
        btnGetMoney = new SpaceButton(font70, LanguageManager.get("getandexit"), imgLongButtonAtlas, 260, 1.58f);
        btnMoney = new SpaceButton(font70, strmoney, SCR_WIDTH - 120, 1550);


        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));
        shipSkin = main.shipSkin;
        shotsShots = main.shotsShots;
        initialShotCount = main.shotsBoostCount;
        initialEven = main.shotEven;
        bg[0] = new GameBackground(0, 0);
        bg[1] = new GameBackground(0, SCR_HEIGHT * 3);
        ship = new Ship(SCR_WIDTH / 2, SCR_HEIGHT / 5);
        //Model model = manager.get("models/type0.obj");



    }


    public void show() {
        ship.type = main.shipSkin;
        gameState = GAME;
        shotCount = main.shotsBoostCount;
        shotEven = main.shotEven;
        timeStartGame = TimeUtils.millis();
        LoadGame();
        if (!main.isAboutLevel) {
            loadLevel(main.level);
        } else {
            loadLevel(main.aboutLevel);

        }

        if (currentlevel.controls == JOYSTIK_RIGHT) {
            joystick = new Joystick(
                joystickBase, joystickStick,
                700, 260,  // Позиция центра
                260, 150    // Размеры base и stick
            ); // (x, y, baseRadius, stickRadius) }
        }
        if (currentlevel.controls == JOYSTIK_LEFT) {
            joystick = new Joystick(
                joystickBase, joystickStick,
                200, 260,  // Позиция центра
                260, 120    // Размеры base и stick
            );
        }

        if (currentlevel.isStoneLevel) {
           /* for(int i=0;i<4;i++){
            stones.add(new Stone(50,MathUtils.random(0f,1200f)));
            stones.add(new Stone(850,MathUtils.random(200f,1400f)));}*/
        }

        imgEnemyes = currentlevel.imgEnemyes;
        for (int j = 0; j < imgEnemy.length; j++) {
            for (int i = 0; i < imgEnemy[j].length; i++) {
                imgEnemy[j][i] = new TextureRegion(imgEnemyes, (i < 10 ? i : 10 - i) * 400, (j) * 500, 400, 500);
            }
        }
        btnGetMoney.changeText(LanguageManager.get("getandexit"));


    }

    @Override
    public void render(float delta) {


        //ship.CheckVx = ship.vX;


        if (currentlevel.controls == JOYSTIK_RIGHT || currentlevel.controls == JOYSTIK_LEFT && joystick != null) {
            Vector2 direction = joystick.getDirection();
            ship.setVelocity(direction.x * 10f, direction.y * 10f); // 5 - множитель скорости

            if (!joystick.isActive()) {
                ship.stop(); // Останавливаем если джойстик неактивен
            }
        }

        if (!currentlevel.isZeroLevel && !currentlevel.isNums) {
            spavnRocketAndShieldBoost();
        }


        if (isRocket) {
            for (Rocket r : rockets) {
                r.update(delta);
            }
        }
        //касания и управление
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (btnBack.hit(touch.x, touch.y)) {
                main.isPlayMove = false;
                main.shotsBoostCount = shotCount;

                if (main.isAboutLevel) {
                    gameState = GAME_OVER;
                    gameClear();
                    main.isAboutLevel = false;
                } else {
                    SaveGame();
                    FonMusic.stop();
                    stopGame();
                }
                main.setScreen(main.screenMenu);


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
                if (gameState == GAME_OVER) {
                    SaveGame();
                    main.shotsBoostCount = initialShotCount;
                    main.shotEven = initialEven;
                    gameClear();
                    main.isPlayMove = true;
                    main.isAboutLevel = false;
                }

            }
            if (keyboard.touch(touch.x, touch.y)) strKB = keyboard.getText();
        }
        btnGetMoney.changePhases();
        if (currentlevel.controls == ACCELEROMETER && (Gdx.app.getType() == Application.ApplicationType.Android)) {
            final float deadzone = 0.2f;
            float rawX = Gdx.input.getAccelerometerX();
            float rawY = Gdx.input.getAccelerometerY();
            if (Math.abs(rawX) <= deadzone) rawX = 0;
            if (Math.abs(rawY) <= deadzone) rawY = 0;
            if (OrientationHelper.getOrientation() == OrientationHelper.ScreenOrientation.PORTRAIT) {
                ship.vY = -rawY;
                ship.vX = -rawX;
            }
            ship.move();


        }
        if (currentlevel.isStoneLevel) {
            for (int i = stones.size() - 1; i >= 0; i--) {
                // stones.get(i).move();
                if (stones.get(i).OfScreen()) {
                    stones.remove(i);
                }
            }
        }
        if (currentlevel.isAccelerometrLevel) {

            final float deadzone = 0.2f;
            float rawX = Gdx.input.getAccelerometerX();
            float rawY = Gdx.input.getAccelerometerY();
            if (Math.abs(rawX) <= deadzone) rawX = 0;
            if (Math.abs(rawY) <= deadzone) rawY = 0;
            for (Shot s : shots) {
                if (OrientationHelper.getOrientation() == OrientationHelper.ScreenOrientation.PORTRAIT) {

                    s.vY = -rawX * 3;

                } else {

                    s.vY = -rawY * 3;
                }
            }
        }
        if (currentlevel.isZeroLevel) {
            if (earth.overlap(ship)) {

                earth.nPhases = 4;

            }

            earth.move();
        }
        if (currentlevel.isNums) {

            spavnNums();

        }

        //события


        if (gameState == GAME) {
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
                    money += moneyFactor * 3;

                    bosses.remove(j);

                    break;

                }

                if (bosses.get(j).overlap(ship, (bosses.get(j).Dead ? enemyDeadPixmap : bossPixmap), shipsPixmap)) {
                    if (!(isShield || isShieldWarring)) {
                        emeniesDone = 0;
                        timeRedSpawn = TimeUtils.millis();
                        gameState = GAME_OVER;
                        stopGame();
                        break;
                    }
                }
                if (isRocket) {
                    if (rockets.isEmpty()) isRocket = false;
                    for (int i = rockets.size() - 1; i >= 0; i--) {
                        if (rockets.get(i).overlapQ(bosses.get(j))) {
                            rockets.remove(i);
                            bosses.get(j).health = 0;

                        }
                    }

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
            if (currentlevel.isReflexLevel && enemies.get(j).y < SCR_HEIGHT / 3)

                enemies.get(j).vY -= 0.15f;

            if ((TimeUtils.millis() - enemies.get(j).timeLastWouded) > 154) {
                enemies.get(j).isWouded = false;
            }
            if (enemies.get(j).health <= 0) {
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
                if (currentlevel.isReflexLevel) {
                    emeniesDone += 1;
                    money += moneyFactor;
                    enemies.remove(j);
                    break;
                } else {
                    {
                        timeRedSpawn = TimeUtils.millis();
                        money -= moneyFactor / 2;
                    }
                }
                enemies.remove(j);
                break;


            }
            if (isRocket) {
                for (int i = rockets.size() - 1; i >= 0; i--) {
                    if (rockets.get(i).overlap(enemies.get(j))) {

                        for (int k = MathUtils.random(2, 9); k >= 0; k--) {
                            fragments.add(new Fragment(enemies.get(j).x, enemies.get(j).y));
                        }
                        for (int k = MathUtils.random(2, 7); k >= 0; k--) {
                            fragments.add(new Fragment(rockets.get(i).x, rockets.get(i).y));
                        }
                        rockets.remove(i);
                        emeniesDone += 1;
                        money += moneyFactor;
                        enemies.get(j).width = 0;
                        sumCoastEnemyes += (4 - enemies.get(j).type) * main.basicSkinCoast;
                        timeExplosions = TimeUtils.millis();
                        enemies.remove(j);
                        break;

                    }
                }
                if (rockets.isEmpty()) isRocket = false;
            }
        }
        for (int j = enemies.size() - 1; j >= 0; j--) {
            if (!(isShield || isShieldWarring)) {
                //если наш корабль столкнулся с врагом
                if (enemies.get(j).overlap(ship, enemyPixmap, shipsPixmap)) {

                    emeniesDone = 0;
                    timeRedSpawn = TimeUtils.millis();
                    gameState = GAME_OVER;
                    stopGame();
                    break;
                }
            }

            for (int i = shots.size() - 1; i >= 0; i--) {

                if (!currentlevel.isTapLevel) {
                    if (!currentlevel.isReflexLevel) {
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
                        } else {
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

        for (int i = rocketsBoost.size() - 1; i >= 0; i--) {
            if (ship.overlap(rocketsBoost.get(i))) {
                timeGreenSpawn = TimeUtils.millis();
                rockets.add(new Rocket(0, MathUtils.random(10, 850)));

                isRocket = true;

                rocketsBoost.remove(i);

            }
        }
        for (int i = nums.size() - 1; i >= 0; i--) {
            if (nums.get(i).overlap(ship)) {
                if (nums.get(i).isTrueNum) {
                    timeGreenSpawn = TimeUtils.millis();
                    money += nums.get(i).num;
                    nums.remove(i);
                } else {
                    timeRedSpawn = TimeUtils.millis();
                    gameState = GAME_OVER;
                    stopGame();
                    break;

                }


            }
        }
        for (int i = shieldsBoosts.size() - 1; i >= 0; i--) {
            if (ship.overlap(shieldsBoosts.get(i))) {
                timeGreenSpawn = TimeUtils.millis();
                isShield = true;
                timeUseOnShield = TimeUtils.millis();
                shieldsBoosts.remove(i);

            }
        }

        shieldOn();
        /// delete
        for (int i = shots.size() - 1; i >= 0; i--) {
            if (currentlevel.isReflexLevel) {
                if (ship.overlap(shots.get(i))) {
                    if (!(isShield || isShieldWarring)) {
                        shots.get(i).width = 0;
                        shots.get(i).height = 0;
                        gameState = GAME_OVER;
                    }


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
        for (int i = nums.size() - 1; i >= 0; i--) {
            if (nums.get(i).OutOfscreen()) {
                if (nums.get(i).isTrueNum) {
                    timeRedSpawn = TimeUtils.millis();
                    gameState = GAME_OVER;
                    stopGame();
                } else {
                    nums.remove(i);
                }
            }
        }
        if (nums.isEmpty()) isBoss = true;

        for (int i = rocketsBoost.size() - 1; i >= 0; i--) {
            if (rocketsBoost.get(i).OutOfscreen()) rocketsBoost.remove(i);
        }
        for (int i = shieldsBoosts.size() - 1; i >= 0; i--) {
            if (shieldsBoosts.get(i).OutOfscreen()) shieldsBoosts.remove(i);
        }

        for (int e = fragments.size() - 1; e >= 0; e--) {
            fragments.get(e).move();
            if (fragments.get(e).OutOfscreen()) fragments.remove(e);
        }

        if (enemies.isEmpty() && bosses.isEmpty() && emeniesCount == currentlevel.enemiesMax + currentlevel.bossCount) {
            timeGreenSpawn = TimeUtils.millis();
            gameState = GAME_OVER;
            stopGame();

        }
        if (currentlevel.controls == JOYSTIK_LEFT || currentlevel.controls == JOYSTIK_RIGHT) {

            ship.update(delta);

        }


        strmoney = money + "";
        btnMoney.changeText(strmoney);


        /// draw

        // Отрисовка







        batch.begin();


        for (GameBackground bg : bg) {
            batch.draw(imgBG, bg.x, bg.y, bg.width, bg.height);
        }
        if (currentlevel.isStoneLevel) {
            for (Stone s : stones) {
                batch.draw(imgStone, s.scrX(), s.y, s.width, s.height);
            }
        }
        if (currentlevel.isZeroLevel) {
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
        for (Boost n : nums) {
            batch.draw(imgNum[n.num], n.scrX(), n.scrY(), n.width, n.height);
        }
        for (Boost b : shieldsBoosts) {
            batch.draw(imgShieldBoost, b.scrX(), b.scrY(), b.width, b.height);
        }
        for (Boost b : rocketsBoost) {
            batch.draw(imgRocketBoost, b.scrX(), b.scrY(), b.width, b.height);
        }


        if (isRocket) {
            for (Rocket rocket : rockets) {
                batch.draw(imgRocket[0], rocket.getX(), rocket.getY(),
                    rocket.width / 2, rocket.height / 2,
                    rocket.width, rocket.height,
                    1, 1,
                    rocket.getRotation());
            }
        }
        if (isShield)
            batch.draw(imgShiledShip[main.shipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width / 2, ship.height / 2, ship.width, ship.height, 1, 1, ship.rotation);
        if (isShieldWarring)
            batch.draw(imgShiledShipWarring[main.shipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width / 2, ship.height / 2, ship.width, ship.height, 1, 1, ship.rotation);

        if (ship.vX < 0) {
            batch.draw(imgShipatlas[main.shipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width / 2, ship.height / 2, ship.width, ship.height, -1, 1, ship.rotation);
        } else {
            batch.draw(imgShipatlas[main.shipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width / 2, ship.height / 2, ship.width, ship.height, 1, 1, ship.rotation);
        }


        if (gameState == GAME_OVER) {
            batch.draw(imgGrayBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }
        if (timeRed()) {
            batch.draw(imgRED, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }
        if (timeGreen()) {
            batch.draw(imgGreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }


        keyboard.draw(batch);

        batch.draw(imgBack[btnBack.type], btnBack.imgX, btnBack.imgY, btnBack.imgWidth, btnBack.imgHeight);

        if (currentlevel.controls == JOYSTIK_LEFT || currentlevel.controls == JOYSTIK_RIGHT && joystick != null) {

            joystick.draw(batch);
        }
        btnMoney.font.draw(batch, btnMoney.text, btnMoney.x, btnMoney.y);
        batch.draw(imgMN, btnMoney.x - 70, btnMoney.y - 58, 50, 50);
        //если игра завершена
        if (gameState == GAME_OVER) {

            //batch.draw(imgBG2, 0, 0, SCR_WIDTH, SCR_HEIGHT);
            batch.draw(imgLogo, SCR_WIDTH / 2 - 240, 1200, 480, 390);
            font70.draw(batch, !iscomplited() ? LanguageManager.get("gameover") : LanguageManager.get("levelcomplited"), 0, 900, SCR_WIDTH, Align.center, true);
            if (money % 10 == 1 && money % 100 != 11) {
                font70.draw(batch, LanguageManager.get("youcollected") + " " + (Math.max(money, 0)) + " " + LanguageManager.get("coinsY"), 0, 600, SCR_WIDTH, Align.center, true);

            } else if (money % 10 >= 2 && money % 10 <= 4 && !(money % 100 >= 12 && money % 100 <= 14)) {
                font70.draw(batch, LanguageManager.get("youcollected") + " " + (Math.max(money, 0)) + " " + LanguageManager.get("coinsI"), 0, 600, SCR_WIDTH, Align.center, true);

            } else {
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

        // Текстуры
        imgMN.dispose();
        imgStone.dispose();
        joystickBase.dispose();
        joystickStick.dispose();
        imgBG2.dispose();
        imgRED.dispose();
        imgShipsatlas.dispose();
        imgShotsatlas.dispose();
        imgFragmentatlas.dispose();
        imgEnemyes.dispose();
        imgEnemyesBoses.dispose();
        imgEnemyesDead.dispose();
        imgEnemyesWouded.dispose();
        imgEarthAtlas.dispose();
        imgLongButtonAtlas.dispose();
        imgMinus.dispose();
        imgBG.dispose();
        imgPlus.dispose();
        imgGreen.dispose();
        imgGrayBG.dispose();
        imgBackAtlas.dispose();
        imgRocketAtlas.dispose();
        imgLogo.dispose();
        imgShieldBoost.dispose();
        imgRocketBoost.dispose();
        imgShiledShipAtlas.dispose();
        imgShiledShipWarringAtlas.dispose();
        imgNumsAtlas.dispose();

        // Пиксельные карты
        shipsPixmap.dispose();
        enemyPixmap.dispose();
        bossPixmap.dispose();
        enemyWoudedPixmap.dispose();
        enemyDeadPixmap.dispose();
        rocketPixmap.dispose();
        stonePixmap.dispose();
        numPixmap.dispose();

        // Аудио
        sndExplosion.dispose();
        sndBlaster.dispose();
        FonMusic.dispose();

        // Шрифты
        font70.dispose();
        font32.dispose();

        // SpriteBatch
        batch.dispose();

    }

    private void spavnRocketAndShieldBoost() {
        if (TimeUtils.millis() > timeLastSpawnRocket + currentlevel.timeRocketInterwal) {
            rocketsBoost.add(new Boost());
            timeLastSpawnRocket = TimeUtils.millis();
        }
        if (TimeUtils.millis() > timeLastSpawnShield + currentlevel.timeShieldInterval) {
            shieldsBoosts.add(new Boost());
            timeLastSpawnShield = TimeUtils.millis();
        }
    }

    private void shieldOn() {
        if (TimeUtils.millis() >= timeUseOnShield + timeShield) {
            isShield = false;
            isShieldWarring = true;
        }
        if (TimeUtils.millis() >= timeUseOnShield + timeShield + timeShieldWarring) {

            isShieldWarring = false;

        }

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
    /*public void  spavnStones(){
        if(currentlevel.isStoneLevel){

            if ((TimeUtils.millis()>= timeLastSpawnStones+timeStonesInterwal)) {
                stones.add(new Stone(0,MathUtils.random(1600f,2000f)));
                stones.add(new Stone(0,MathUtils.random(2000f,2600f)));
                    timeLastSpawnStones = TimeUtils.millis();



            }
        }
    }*/

    public void spavnShot() {
        if ((TimeUtils.millis() > timeLastSpawnShots + currentlevel.timeShotsInterval) && isShots) {

            if (shotCount == 0) {
                shots.add(new Shot(ship.scrX() + ship.width / 2 - (ship.rotation) * ship.width / 90, ship.scrY() + 0.85f * ship.height));


            }

            for (int i = 0; i < (shotEven == 0 ? shotCount * 2 : (shotCount * 2 + 1)); i++) {
                shots.add(new Shot(ship.scrX() + ship.width / 2 - (ship.rotation) * ship.width / 90, ship.scrY() + 245));


            }


            timeLastSpawnShots = TimeUtils.millis();
            timeLastSpawnShots = TimeUtils.millis();
            if (main.isActionSounds) sndBlaster.play();


        }


    }

    public void spavnShotLevel6() {
        if ((TimeUtils.millis() > timeLastSpawnShots + currentlevel.timeShotsInterval)) {

            if (currentlevel.isSettingLevel) {
                for (int i = enemies.size() - 1; i >= 0; i--) {
                    if (i % 8 == 0) {
                        shots.add(new Shot(enemies.get(i).scrX() + enemies.get(i).width / 2, enemies.get(i).scrY()));

                        timeLastSpawnShots = TimeUtils.millis();
                        if (main.isActionSounds) sndBlaster.play();
                        break;
                    }
                }
            } else {
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


    }

    private void MoveShots() {
        if (currentlevel.isReflexLevel) {
            if (!shots.isEmpty()) {

                for (int r = shots.size() - 1; r < shots.size(); r++) {

                    shots.get(r).move();
                }
            }
        } else {

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
    }


    public void keyboard() {
        if (enemies.isEmpty() && emeniesCount >= currentlevel.enemiesMax) {
            System.out.println("" + sumCoastEnemyes);
            if (!keyboard.isKeyboardShow) {

                if (strKB.isEmpty()) keyboard.start();


            }


            if (!strKB.isEmpty() && !isBoss) {
                sumCoastEnemyes = 0;
                gameState = GAME_OVER;
                timeRedSpawn = TimeUtils.millis();
            }


            // if (keyboard.isKeyboardShow) {


            // }
        }
    }


    public void spavnBoost() {
        if (TimeUtils.millis() > timeLastSpawnBoost + currentlevel.timeBoostInterval) {
            boosts.add(new Boost());
            timeLastSpawnBoost = TimeUtils.millis();
        }


    }

    public void spavnNums() {
        if (TimeUtils.millis() > timeLastSpawnNums + currentlevel.timeSpawnInterval && numInt < 10) {
            nums.add(new Boost(randomExclude(0, 9, numInt), false));
            nums.add(new Boost(MathUtils.clamp(numInt, 0, 9), true));
            numInt += 1;
            timeLastSpawnNums = TimeUtils.millis();

        }


    }

    public int randomExclude(int min, int max, int exclude) {
        int result = MathUtils.random(min, max - 1); // Генерируем число в уменьшенном диапазоне
        return (result >= exclude) ? result + 1 : result; // Сдвигаем, если число >= исключаемому
    }

    public void stopGame() {
        sndExplosion.stop();
        FonMusic.stop();
        for (Enemy e : enemies) e.stop();
        for (Boost b : boosts) b.stop();
        for (Shot s : shots) s.stop();
        for (Boost a : shieldsBoosts) a.stop();
        for (Boost a : rocketsBoost) a.stop();
        for (Boost n : nums) n.stop();
        for (Fragment f : fragments) f.stop();
        for (Stone s : stones) s.stop();
        SaveGame();
    }


    private void loadLevel(int level) {


        currentlevel = Levels.LEVELS[level];
        if (((currentlevel.controls == ACCELEROMETER && Gdx.app.getType() == Application.ApplicationType.Android)) || currentlevel.isAccelerometrLevel) {
            OrientationHelper.lockCurrentOrientation();
        } else {
            OrientationHelper.unlock(); // Разрешаем автоповорот
        }
        if (currentlevel.isZeroLevel) {
            isShots = false;
            earth = new Earth(SCR_WIDTH / 2, 1120);
        }
        isShots = currentlevel.isShots;
        isBoss = currentlevel.isBoss;
        if (currentlevel.isNums) {
            strKB = "";

        }
    }


    public void gameClear() {

        if (money > 0) {
            main.allmoney += money;
            main.player.money = main.allmoney;
        }

        if (iscomplited() && main.level <= currentlevel.MaxLevel) {
            if (!main.isAboutLevel) level += 1;
            main.level = level;
            main.player.level = level;
        }
        //loadLevel(main.level);


        SaveGame();
        money = 0;
        btnMoney.changeText(strmoney);
        main.setScreen(main.screenMenu);
        FonMusic.stop();
        enemies.clear();
        bosses.clear();
        shieldsBoosts.clear();
        rocketsBoost.clear();
        boosts.clear();
        rockets.clear();
        shots.clear();
        stones.clear();
        nums.clear();
        numInt = 0;
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
        if (currentlevel.isReflexLevel) spavnShotLevel6();
        else spavnShot();
        spavnBoss();


        for (Enemy e : enemies) {
            e.move();

        }
        for (Boss b : bosses) {
            b.move();
        }


        ship.move();
        spavnBoost();
        for (Boost b : boosts) b.move();
        for (Boost n : nums) n.move();
        for (Boost b : rocketsBoost) b.move();
        for (Boost b : shieldsBoosts) b.move();


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
                    if (!currentlevel.isTapLevel) {
                        touch.set(screenX, screenY, 0);
                        camera.unproject(touch);
                        ship.touch(touch);

                        ship.rotation = 0;
                        // ship.CheckVx=ship.vX;
                        ship.move();
                        ship.vY /= 30;
                        ship.vY /= 70;
                    }


                }
                if (currentlevel.controls == JOYSTIK_LEFT || currentlevel.controls == JOYSTIK_RIGHT && joystick != null) {
                    touch.set(screenX, screenY, 0);
                    camera.unproject(touch);
                    // System.out.println(234567);
                    return joystick.touchDown(touch.x, touch.y, pointer);


                }

            }
            return false;

        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            ship.rotation = 0;
            if ((currentlevel.controls == JOYSTIK_LEFT || currentlevel.controls == JOYSTIK_RIGHT) && joystick != null) {
                ship.rotation = 0;
                ship.stop();
                return joystick.touchUp(pointer);
            } else

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
                if (currentlevel.controls == JOYSTIK_LEFT || currentlevel.controls == JOYSTIK_RIGHT && joystick != null) {
                    touch.set(screenX, screenY, 0);
                    camera.unproject(touch);
                    ship.updateRotatoin();
                    // System.out.println(234567);
                    return joystick.touchDragged(touch.x, touch.y, pointer);


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






