package ru.pick;

import static ru.pick.Main.*;
import static ru.pick.ScreenLeaderboard.*;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;


public class ScreenMenu implements Screen  {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font70;
    private long TimeLastChangeBg,TimeChangeBg= 700;
    private int phaseBg;




    SpaceButton btnPlay;
    SpaceButton btnSetting;
    SpaceButton btnLeaderboard;
    SpaceButton btnAbout;
    SpaceButton btnExit;
    SpaceButton btnAllmoney;
    SpaceButton btnShop;

    Music FonMusic;
    Texture imgLogo;
    Texture imgMN;
    Texture imgBG2;
    Texture imgEnemyesBoses;
    Texture imgLongButtonAtlas;
    Texture imgButtonsAtlas;
    TextureRegion[] imgEnemyBoses = new TextureRegion [6];
    TextureRegion[] imgBG = new TextureRegion[4];
    TextureRegion[] imgLongButton = new  TextureRegion[3];
    TextureRegion[][] imgButtons = new  TextureRegion[3][3];
    Texture imgBGAtlas;

    Enemy MenuEnemy;


    public ScreenMenu(Main main) {
        this.main = main;
        batch= main.batch;
        camera= main.camera;
        touch= main.touch;
        font70=main.font70;

        FonMusic= main.FonMusic;


        imgEnemyesBoses = new Texture("atlasboss.png");
        imgLongButtonAtlas=new Texture("LongButton.png");
        imgButtonsAtlas=new Texture("AtlasButtons.png");
        imgBGAtlas=new Texture("bgmenu.png");
        imgBG2=new Texture("bgmenu2.png");
        imgMN=new Texture("moneta.png");
        imgLogo= new Texture("logo.png");

        for (int i = 0; i< imgBG.length;i++) {
            imgBG[i] = new TextureRegion(imgBGAtlas, (i) * 900, 0, 900, 1600);
        }
        for (int e = 0; e < imgEnemyBoses.length; e++) {

            imgEnemyBoses[e] = new TextureRegion(imgEnemyesBoses, (e<6? e:10-e) *450, 0, 450, 450);
        }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e)*193, 497, 193);
        }

        for (int j = 0; j < imgButtons.length; j++) {
            for (int i = 0; i < imgButtons[j].length; i++) {
                imgButtons[j][i] = new TextureRegion(imgButtonsAtlas, i  * 209, (j) * 180, 209, 180);
            }
        }
        btnPlay = new SpaceButton(font70, "PLAY",imgLongButtonAtlas,SCR_HEIGHT/3.6f,4.2f);
        btnLeaderboard = new SpaceButton(font70,"leaderboard",250);

        btnAbout = new SpaceButton(0,1140,200,170,0);
        btnSetting = new SpaceButton(SCR_WIDTH-200,1140,200,170,1);
        btnShop = new SpaceButton(0,950,200,170,2);

        btnExit = new SpaceButton(font70,"x",20,SCR_HEIGHT-30);
        btnAllmoney= new  SpaceButton(font70,""+main.Allmoney,SCR_WIDTH-100,1550);



        MenuEnemy=new Enemy();
        MenuEnemy.MenuEnemy=true;



    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Vector3 Mousepose = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        camera.unproject(Mousepose);

        btnPlay.ButtonsState(Mousepose.x,Mousepose.y);
        btnSetting.ButtonsState(Mousepose.x,Mousepose.y);
        btnAbout.ButtonsState(Mousepose.x,Mousepose.y);
        btnShop.ButtonsState(Mousepose.x,Mousepose.y);
        btnLeaderboard.ButtonsState(Mousepose.x,Mousepose.y);
        btnExit.ButtonsState(Mousepose.x,Mousepose.y);

        if(btnAbout.SetScreenButton)main.setScreen(main.screenAbout);
        if(btnSetting.SetScreenButton)main.setScreen(main.screenSettings);
        if(btnShop.SetScreenButton)main.setScreen(main.screenShop);
        if(btnPlay.SetScreenButton){
            main.setScreen(main.screenGame);
            if (main.isFonMusic)FonMusic.play();
            FonMusic.setVolume(0.3f);
            GameState=GAME;
        }



            if(btnLeaderboard.SetScreenButton){
                main.setScreen(main.screenLeaderboard);
                Iskeyboard=true;
            }

            if(btnExit.SetScreenButton){
                Gdx.app.exit();
            }







        MenuEnemy.y=SCR_HEIGHT/5;
        MenuEnemy.width=MenuEnemy.height=SCR_WIDTH;
        MenuEnemy.x=12*SCR_WIDTH/15;


        MenuEnemy.move();
        btnSetting.changePhases();
        btnAbout.changePhases();
        btnShop.changePhases();
        btnPlay.changePhases();



        btnAllmoney.changeText(main.Allmoney);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG[phaseBg], 0, 0, SCR_WIDTH, SCR_HEIGHT);

        changePhase();

        batch.draw(imgEnemyBoses[MenuEnemy.phase], MenuEnemy.scrX(), MenuEnemy.scrY(),MenuEnemy.width/2,MenuEnemy.height/2, MenuEnemy.width, MenuEnemy.height,1,1,MenuEnemy.rotation);
        batch.draw(imgBG2, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        batch.draw(imgLogo,SCR_WIDTH/2-240,btnShop.imgY+btnShop.imgWidht/2-29,480,390);
        batch.draw(imgLongButton[btnPlay.phase],btnPlay.imgX,btnPlay.imgY,btnPlay.imgWidht,btnPlay.imgHeight);
        batch.draw(imgButtons[btnSetting.type][btnSetting.phase],btnSetting.imgX,btnSetting.imgY,btnSetting.imgWidht,btnSetting.imgHeight);
        batch.draw(imgButtons[btnAbout.type][btnAbout.phase],btnAbout.imgX,btnAbout.imgY,btnAbout.imgWidht,btnAbout.imgHeight);
        batch.draw(imgButtons[ btnShop.type][ btnShop.phase], btnShop.imgX, btnShop.imgY, btnShop.imgWidht, btnShop.imgHeight);

        btnPlay.font.draw(batch,btnPlay.text,btnPlay.x,btnPlay.y);
        btnLeaderboard.font.draw(batch,btnLeaderboard.text,btnLeaderboard.x,btnLeaderboard.y);
        btnExit.font.draw(batch,btnExit.text,btnExit.x,btnExit.y);
        btnAllmoney.font.draw(batch,main.Allmoney<1000? btnAllmoney.text:main.Allmoney/1000+"k",btnAllmoney.x,btnAllmoney.y);

        batch.draw(imgMN,btnAllmoney.x-70,btnAllmoney.y-58,50,50);
        font70.draw(batch,"level "+main.player.level,370,1540);

        batch.end();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePhase() {
     if( TimeUtils.millis()>=TimeChangeBg+TimeLastChangeBg){
         phaseBg+=1;
         if( phaseBg==4) phaseBg=0;
         TimeLastChangeBg=TimeUtils.millis();

          }
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
