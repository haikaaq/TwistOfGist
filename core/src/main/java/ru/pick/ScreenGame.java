package ru.pick;

import static ru.pick.Main.*;

import static ru.pick.Shot.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Vector3 touch;

    private final BitmapFont font70;
    private final BitmapFont font32;
    private long timeLastSpawnEnemy, timeEnemyInterval = 1450;
    private static long timeLastEnemyWoud, timeEnemyWounded = 60;
    private long timeLastSpawnShots, timeShotsInterval = 190;
    private long timeLastSpawnBoost, timeBoostInterval = 9000;
    private long timeGreenSpawn, timeGreen = 700;
    private long timeRedSpawn, timeRed = 700;

    public int level = 1;
    public int EmeniesMAX = MathUtils.random(16, 18);
    private int EmeniesDone = 0;
    private int EmeniesCount = 0;

    public static int ShotCount;

    private int ShotEven;
    private int InitialShotCount;
    private int InitialEven;


    private int money;


    private boolean isgame;

    public int ShipSkin;
    public int Allmoney;
    public Player[] players = new Player[10];
    public int ShotsShots;
    public int MoneyFactor;

    Texture imgJS;
    Texture imgMN;
    Texture imgBG;
    Texture imgRED;
    Texture imgShipsatlas;
    Texture imgShotsatlas;
    Texture imgFragmentatlas;
    Texture imgEnemyes;
    Texture imgEnemyesBoses;
    Texture imgEnemyesDead;
    Texture imgEnemyesWouded;

    Texture imgMinus;
    Texture imgPlus;
    Texture imgGreen;
    Texture imgGrayBG;

    TextureRegion[][] imgShipatlas = new TextureRegion[5][12];
    TextureRegion[][] imgEnemy = new TextureRegion[4][12];
    TextureRegion[] imgEnemyBoses = new TextureRegion [6];
    TextureRegion[] imgEnemyDead = new TextureRegion[10];
    TextureRegion[][] imgFragments = new TextureRegion[4][4];
    TextureRegion[] imgShotatlas = new TextureRegion[5];
    TextureRegion[][] imgEnemyWouded = new TextureRegion[4][12];



    SpaceButton btnMoney;
    String moneyStr = "" + money;

    Sound sndExplosion;
    Sound sndBlaster;
    Sound sndPlus;
    Music FonMusic;

    SpaceButton btnBack;
    SpaceButton btnGetMoney;

    Ship ship;
    GameBackground[] bg = new GameBackground[2];
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    List<Boost> boosts = new ArrayList<>();


    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;

        touch = main.touch;
        font70 = main.font70;
        font32 = main.font32;
        level = main.level;
        FonMusic = main.FonMusic;
        Allmoney = main.Allmoney;
        MoneyFactor = Math.min(level, 10);

        Gdx.input.setInputProcessor(new Processor());
        imgMN = new Texture("moneta.png");
        imgJS = new Texture("js.png");
        imgBG = new Texture("bggame.png");
        imgShipsatlas = new Texture("atlas.png");
        imgShotsatlas = new Texture("shots.png");
        imgFragmentatlas = new Texture("fragments.png");
        imgEnemyes = new Texture("enemyes.png");
        imgEnemyesWouded = new Texture("woundedemenies.png");
        imgEnemyesBoses = new Texture("atlasboss.png");
        imgEnemyesDead = new Texture("emenyesDead.png");
        imgRED = new Texture("red.png");
        imgMinus = new Texture("minus.png");
        imgPlus = new Texture("plus.png");
        imgGreen = new Texture("green.png");
        imgGrayBG = new Texture("GrayBG.png");
        imgShotsatlas = new Texture("shots.png");

        for (int j = 0; j < imgShipatlas.length; j++) {
            for (int i = 0; i < imgShipatlas[j].length; i++) {
                imgShipatlas[j][i] = new TextureRegion(imgShipsatlas, (i < 7 ? i : 12 - i) * 800, (j) * 800, 800, 800);
            }
        }

        for (int j = 0; j < imgEnemy.length; j++) {
            for (int i = 0; i < imgEnemy[j].length; i++) {
                imgEnemy[j][i] = new TextureRegion(imgEnemyes, (i < 7 ? i : 12 - i) * 800, (j) * 800, 800, 800);
            }
        }
        for (int j = 0; j < imgEnemyWouded.length; j++) {
            for (int i = 0; i < imgEnemyWouded[j].length; i++) {
                imgEnemyWouded[j][i] = new TextureRegion(imgEnemyesWouded, (i < 7 ? i : 12 - i) * 800, (j) * 800, 800, 800);
            }
        }


        for (int e = 0; e < imgEnemyDead.length; e++) {

                imgEnemyDead[e] = new TextureRegion(imgEnemyesDead, (e < 6 ? e : 10 - e) * 450, 0, 450, 450);

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

            imgEnemyBoses[e] = new TextureRegion(imgEnemyesBoses, (e<6? e:10-e) *450, 0, 450, 450);
        }


        btnBack = new SpaceButton(font70, "Back", 30, 1550);
        btnGetMoney = new SpaceButton(font70, "get and exit", SCR_WIDTH / 2 - 220, 400);
        btnMoney = new SpaceButton(font70, moneyStr, SCR_WIDTH * 4 / 5, btnBack.y);

        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));

        ShipSkin = main.ShipSkin;
        ShotsShots = main.ShotsShots;
        InitialShotCount = main.ShotsBostCount;
        InitialEven = main.ShotEven;

        bg[0] = new GameBackground(0, 0);
        bg[1] = new GameBackground(0, SCR_HEIGHT);


        ship = new Ship(SCR_WIDTH / 2, SCR_HEIGHT / 5);
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();


        }


        LoadTable();
        LoadGame();


    }


    public void show() {


    }

    @Override
    public void render(float delta) {
        ShotCount=main.ShotsBostCount;
        ShotEven=main.ShotEven;
        ship.CheckVx=ship.vX;


         SaveGame();
        //касания и управление
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (btnBack.hit(touch.x, touch.y)) {
                main.setScreen(main.screenMenu);
                FonMusic.stop();
                main.ShotsBostCount=ShotCount;
                StopGame();
            }
            if (btnGetMoney.hit(touch.x, touch.y)) {
                if (GameState == GAME_OWER) {
                    main.ShotsBostCount=InitialShotCount;
                    main.ShotEven=InitialEven;
                    GameClear();

                    gameStart();
                }

            }
        }
        if (controls == ACCELEROMETER) {
            ship.vX = -Gdx.input.getAccelerometerX() * 4;
            ship.vY = -Gdx.input.getAccelerometerY() * 4;
        }


        //события
        if (GameState == GAME) {

            gameStart();
        }


        //
        for (int j = enemies.size() - 1; j >= 0; j--) {
            if((TimeUtils.millis()-enemies.get(j).timeLastWouded)>154){
                enemies.get(j).isWouded=false;
            }

            if (enemies.get(j).EnemyIsBoss) {

                /*if (enemies.get(j).y < SCR_HEIGHT - 50)
                    enemies.get(j).vY += MathUtils.random(-0.13f, 0.15f);
                if (enemies.get(j).y > SCR_HEIGHT - 50) enemies.get(j).vY = -2.6f;
                if (enemies.get(j).y < SCR_HEIGHT / 4)
                    enemies.get(j).vY = -0.1f * enemies.get(j).type;*/
            }
            if (enemies.get(j).health == 0) {

                EmeniesDone += 1;

                sndExplosion.play();
                enemies.get(j).health=-1;


                btnMoney.changeText(money);

                if (enemies.get(j).EnemyIsBoss && enemies.get(j).health <= 0) {
                    enemies.get(j).EmenyDead = true;
                    enemies.get(j).vY = -7.69f;
                    money += MoneyFactor;
                    enemies.get(j).EnemyIsBoss=false;
                    btnMoney.changeText(money);

                } else {

                    money += MoneyFactor;
                    btnMoney.changeText(money);
                    for (int k = MathUtils.random(2, 9); k >= 0; k--) {
                        fragments.add(new Fragment(enemies.get(j).x, enemies.get(j).y));
                    }
                    enemies.get(j).width = 0;
                    enemies.remove(j);

                    break;
                }


            }


            if (enemies.get(j).BelowTheScreen()) {


                if (!enemies.get(j).EnemyIsBoss) {
                    timeRedSpawn = TimeUtils.millis();
                    money -= 4 + MoneyFactor;
                } else money += MoneyFactor;
                enemies.remove(j);
                btnMoney.changeText(money);
                break;

            }
            if (enemies.get(j).overlab(ship, enemies.get(j).EnemyIsBoss)) {
                EmeniesDone = 0;
                timeRedSpawn = TimeUtils.millis();
                GameState = GAME_OWER;
                StopGame();
                break;
            }

            //enemies.get(j).vX=MathUtils.random(-1.97f,1.97f);


        }


        for (int j = enemies.size() - 1; j >= 0; j--) {
            for (int i = shots.size() - 1; i >= 0; i--) {

                if (shots.get(i).overlab(enemies.get(j))) {
                    if (enemies.get(j).health > 0) {
                        enemies.get(j).health--;
                        enemies.get(j).isWouded=true;
                        enemies.get(j).timeLastWouded=TimeUtils.millis();

                    }
                    shots.get(i).isoverlab = true;

                    isgame = true;


                    //break;


                }


                if (shots.get(i).isoverlab || shots.get(i).OutOfscreen()) {
                    shots.get(i).width = 0;
                    shots.get(i).height = 0;
                    if (TimeUtils.millis() >= timeLastSpawnShots + timeShotsInterval) {
                        shots.remove(i);
                        break;
                    }

                }


            }

        }
        ship.rotationSpeed=(ship.vX-ship.CheckVx)*100;

        for (int i = boosts.size() - 1; i >= 0; i--) {
            if (ship.overlab(boosts.get(i))) {

                if (boosts.get(i).type == 1) {
                    timeGreenSpawn = TimeUtils.millis();
                    if (ShotCount < 4) {
                        if (ShotEven < ShotCount) ShotEven = ShotCount;
                        else {

                            main.ShotsBostCount+=1;
                            main.ShotEven=0;
                        }
                    }

                } else {
                    timeRedSpawn = TimeUtils.millis();

                    main.ShotsBostCount = 0;
                    main.ShotEven = 0;
                }

                boosts.remove(i);
            }
        }


        for (int i = boosts.size() - 1; i >= 0; i--) {
            if (boosts.get(i).OutOfscreen()) boosts.remove(i);
        }

        for (int e = fragments.size() - 1; e >= 0; e--) {
            fragments.get(e).move();
            if (fragments.get(e).OutOfscreen()) fragments.remove(e);
        }

        if (enemies.isEmpty() && isgame && EmeniesCount == EmeniesMAX) {
            timeGreenSpawn = TimeUtils.millis();
            GameState = GAME_OWER;
            StopGame();

        }

        /*for (int j = enemies.size() -1; j >= 0; j--) {
            if(( EmeniesCount>=EmeniesMAX-4)){
                enemies.getLast().EnemyIsBoss = true;
                enemies.getLast().height= enemies.getLast().width=MathUtils.random(200f,300);

                enemies.getLast().health= MathUtils.random(25*(ShotCount+1),25*(ShotCount+1));
                enemies.getLast().vX= MathUtils.random(-2f,2f);
                break;

            }}*/


        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (GameBackground bg : bg) {
            batch.draw(imgBG, bg.x, bg.y, bg.width, bg.height);
        }
        for (Fragment f : fragments) {
            batch.draw(imgFragments[f.type1][f.type2], f.scrX(), f.scrY(), f.width, f.height);
        }

        for (Enemy e : enemies) {
            if (e.EmenyDead) {
                batch.draw(imgEnemyDead[e.phase], e.scrX(), e.scrY(),e.width/2,e.height/2, e.width, e.height,1,1,e.rotation);
            }
            //else {
            if (e.EnemyIsBoss && !e.EmenyDead) {
                if(e.isWouded){

                    batch.draw(imgEnemyDead[e.phase],e.scrX(), e.scrY(),e.width/2,e.height/2, e.width, e.height,1,1,e.rotation);}

                   else batch.draw(imgEnemyBoses[e.phase], e.scrX(), e.scrY(),e.width/2,e.height/2, e.width, e.height,1,1,e.rotation);
            }
            if ( !e.EnemyIsBoss&&!e.EmenyDead) {
                if(e.isWouded){
                    batch.draw(imgEnemyWouded[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);}
                else batch.draw(imgEnemy[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);

            }
        }


        for (Shot s : shots) {
            batch.draw(imgShotatlas[main.ShotsShots], s.scrX(), s.scrY(), s.width, s.height);
        }
        for (Boost b : boosts) {
            batch.draw(b.type == 1 ? imgPlus : imgMinus, b.scrX(), b.scrY(), b.width, b.height);
        }

        batch.draw(imgShipatlas[main.ShipSkin][ship.phase], ship.scrX(), ship.scrY(), ship.width/2,ship.height/2, ship.width, ship.height,1,1,ship.rotation);
        if (GameState == GAME_OWER) {
            batch.draw(imgGrayBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }
        if (timeRed()) {
            batch.draw(imgRED, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }
        if (timeGreen()) {
            batch.draw(imgGreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        }

        if (controls == JOYSTIK_LEFT) {
            batch.draw(imgJS, 0, 0, SCR_WIDTH / 3, SCR_WIDTH / 3);
        }
        if (controls == JOYSTIK_RIGHT) {
            batch.draw(imgJS, 2 * SCR_WIDTH / 3, 0, SCR_WIDTH / 3, SCR_WIDTH / 3);
            JSwidth = (SCR_WIDTH - SCR_WIDTH / 6) * 2;
        }
        /*if (GameState==GAME_OVER) {
            font120.draw(batch, "GAME OVER", 0, 650, SCR_WIDTH, Align.center, true);

            for (int i = 0; i < player.length-1; i++) {
                font36.draw(batch, player[i].name, 400, 533 - i * 70);
                font36.draw(batch, currentTime(player[i].time), 750, 533 - i * 70);
            }

            restart.font.draw(batch, restart.text, restart.x, restart.y);
            clearTable.font.draw(batch, clearTable.text, clearTable.x, clearTable.y);
        }*/


        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        btnMoney.font.draw(batch, btnMoney.text, btnMoney.x, btnMoney.y);
        batch.draw(imgMN, btnMoney.x - 70, btnMoney.y - 58, 50, 50);



        //если игра завершена
        if (GameState == GAME_OWER) {


            font70.draw(batch, !iscomplited() ? "GAME OVER" : "LEVEL COMPLETED", 0, 1000, SCR_WIDTH, Align.center, true);
            font70.draw(batch, "you collected  " + money + "  coins ", 0, 800, SCR_WIDTH, Align.center, true);
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

    }


    public void spavnEnemy() {
        if (TimeUtils.millis() > timeLastSpawnEnemy + timeEnemyInterval) {
            if (EmeniesCount < EmeniesMAX) {
                enemies.add(new Enemy());
                if (EmeniesCount >= EmeniesMAX - 4 ) {
                    enemies.get(enemies.size() - 1).EnemyIsBoss = true;
                    enemies.get(enemies.size() - 1).height = enemies.get(enemies.size() - 1).width = MathUtils.random(200f, 300);


                    enemies.get(enemies.size() - 1).health = MathUtils.random(25 * (ShotCount + 1), 25 * (ShotCount + 1));
                    enemies.get(enemies.size() - 1).vX = MathUtils.random(-2.211f, 2f);
                }


                timeLastSpawnEnemy = TimeUtils.millis();
                EmeniesCount += 1;
            }
        }
    }

    public void spavnShot() {
        if (TimeUtils.millis() > timeLastSpawnShots + timeShotsInterval && !enemies.isEmpty()) {

            if (ShotCount == 0) {
                shots.add(new Shot(ship.scrX() + ship.width / 2, ship.scrY() + 245));

            }

            for (int i = 0; i < (ShotEven == 0 ? ShotCount * 2 : (ShotCount * 2 + 1)); i++) {
                shots.add(new Shot(ship.scrX() + ship.width / 2, ship.scrY() + 245));


            }
            timeLastSpawnShots = TimeUtils.millis();
            sndBlaster.play();


        }

    }

    private void MoveShots() {






           int j =- ShotCount;
           for(int r =(ShotEven==0? shots.size()-ShotCount*2:shots.size()-(ShotCount*2+1));r<shots.size();r++){


               shots.get(r).vX=j;
               shots.get(r).move();
               j++;
              if(ShotEven==0){

               if (j == 0) j=1;}


             }


      int e = 0;
          while(e <(ShotEven==0? shots.size()-ShotCount*2:shots.size()-ShotCount*2-1))  {

               shots.get(e).move(shots.get(e).vX);
            e++;

        }


    }



    public void spavnBoost(){
        if(TimeUtils.millis()>timeLastSpawnBoost+timeBoostInterval){
            boosts.add(new Boost());
            timeLastSpawnBoost=TimeUtils.millis();
            }


    }

    public void StopGame(){
        sndExplosion.stop();
        FonMusic.stop();
        for (Enemy e:enemies) e.stop();
        for(Boost b : boosts) b.stop();
        for(Shot s : shots) s.stop();
        for (Fragment f:fragments) f.stop();
        SaveGame();

        ship.rotation=0;
        ship.CheckVx=ship.vX;

        }





    public void sortTable() {


        for (int i = 0; i < players.length-1; i++) {
            for (int j = 0; j < players.length-i-1 ; j++) {
                if (players[j].level < players[j + 1].level) {
                    Player p = players[j];
                    players[j] = players[j + 1];
                    players[j + 1] = p;
                }
                if (players[j].level == players[j + 1].level) {
                    if (players[j].money < players[j + 1].money) {
                        Player p = players[j];
                        players[j] = players[j + 1];
                        players[j + 1] = p;
                    }
                }

            }
        }
    }

    private void LoadTable() {
        Preferences prefs = Gdx.app.getPreferences("TableRecords");
        for (int i=0; i<players.length;i++){
            players[i].name= prefs.getString("name"+i,"Noname");
            players[i].level=prefs.getInteger("level"+i,0);
            players[i].money=prefs.getInteger("money"+i,0);
        }
    }


    private void saveTable() {


        Preferences prefs = Gdx.app.getPreferences("TableRecords");
        for (int i=0; i<players.length;i++){
           // main.writeToSheet("level"+"B"+Integer.toString(i+1), Integer.toString( players[i].level));
           // main.writeToSheet("monety"+"C"+Integer.toString(i+1), Integer.toString(players[i].money));
           // main.writeToSheet("name"+"A"+Integer.toString(i+1), players[i].name);
            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("level"+i, players[i].level);
            prefs.putInteger("money"+i,players[i].money);

        }
        prefs.flush();
    }


    public void GameClear(){

        main.Allmoney+=money;
        main.player.money+=money;
        if(iscomplited()){level+=1;main.player.level+=1;}
        if (main.player.level>players[players.length-1].level||(main.player.level==players[players.length-1].level&&main.player.money>=players[players.length-1].money)){
            players[players.length-1].copy(main.player);
            sortTable();
            saveTable();


        }



        SaveGame();
        money=0;
        btnMoney.changeText(money);
        main.setScreen(main.screenMenu);
        FonMusic.stop();
        enemies.clear();
        boosts.clear();
        shots.clear();
        fragments.clear();
        ship.x=SCR_WIDTH/2;
        ship.y  =SCR_HEIGHT/5;
        ShotCount=0;
        ship.rotationSpeed=0;
        ship.rotation=0;

        EmeniesCount=0;
        EmeniesDone=0;


    }




    public boolean iscomplited(){
        return (EmeniesMAX-EmeniesDone<=6&&enemies.isEmpty());

    }

    public  void gameStart(){


        GameState=GAME;

        for (GameBackground bg : bg) bg.move();

        spavnEnemy();
        spavnShot();


        //
        for (Enemy e : enemies) {
            e.move() ;


        }
        spavnBoost();
        for(Boost b: boosts)b.move();

        ship.move();


        if (ShotCount==0){
            for ( Shot s: shots) s.move();}

        if (ShotCount>0){
            MoveShots();

        }


    }
    private void SaveGame(){
    Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

        prefs.putInteger("деньги игрока",main.Allmoney);
        prefs.putInteger("игровой уровень",main.level);

        prefs.flush();}

    private void LoadGame() {
        Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

        main.Allmoney= prefs.getInteger("деньги игрока",main.Allmoney);
        main.level=prefs.getInteger("игровой уровень", main.level);



    }






/*
    private void gameOver(String name) {


    */

    private boolean timeRed(){

        return TimeUtils.millis()-timeRedSpawn<=timeRed;
    }

    private boolean timeGreen(){

        return TimeUtils.millis()-timeGreenSpawn<=timeGreen;
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
        if (GameState==GAME)   {
            if (controls == SCREEN) {
                touch.set(screenX, screenY, 0);
                camera.unproject(touch);
                ship.touch(touch);


                ship.CheckVx=ship.vX;
                 ship.move();
                 ship.vY/=30;
                 ship.vY/=70;



                }
            if (controls == JOYSTIK_LEFT||controls==JOYSTIK_RIGHT) {
                touch.set(screenX, screenY, 0);
                camera.unproject(touch);
                //проверяем попали ли мы касанием в круг используя формулу графика окружности
                if(Math.pow(touch.x-JSwidth/2,2)+Math.pow(touch.y-JSheight/2,2)<=Math.pow(JSwidth/2,2)){
                ship.vX=(touch.x-JSwidth/2)/19;

                ship.vY=(touch.y-JSheight/2)/19;}
                else{ship.stop();
                ship.vX=ship.vY=0;}

            }}

                return false;

        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            ship.stop();
            return false;
        }


        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }


        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
         if (GameState==GAME)   {
            if(controls==SCREEN){
            touch.set(screenX,screenY,0);
            camera.unproject(touch);
            ship.touch(touch);
             ship.move();
              }
            if (controls == JOYSTIK_LEFT||controls==JOYSTIK_RIGHT) {
                touch.set(screenX, screenY, 0);
                camera.unproject(touch);
                //проверяем попали ли мы касанием в круг
               // if(Math.pow(touch.x-JSwidth/2,2)+Math.pow(touch.y-JSheight/2,2)<=Math.pow(150,2)){
                    ship.vX=(touch.x-JSwidth/2)/19;
                    ship.vY=(touch.y-JSheight/2)/19;
                if(ship.vX>=(JSwidth/2)/19)ship.vX=(JSheight/2)/19;
                if(ship.vY>(JSheight/2)/19)ship.vY=(JSheight/2)/19;
             if(ship.vX<-(JSwidth/2)/19)ship.vX=(JSheight/2)/19;
             if(ship.vY<-(JSheight/2)/19)ship.vY=-(JSheight/2)/19;}
        // }



    }
           // }

            return false;
        }
        private void Woud(Enemy e, Shot s)  {



             if(Math.abs(s.y-e.y)<=100 &&Math.abs(s.x-e.x)<=100){
            e.isWouded=true;
            }
             else e.isWouded=false;


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






