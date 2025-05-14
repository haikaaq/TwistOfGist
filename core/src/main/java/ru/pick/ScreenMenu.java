package ru.pick;

import static ru.pick.Main.*;
import static ru.pick.ScreenLeaderboard.*;


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
    private InputKeyboard keyboard;


    SpaceButton btnPlay;
    SpaceButton btnSetting;
    SpaceButton btnLeaderboard;
    SpaceButton btnAbout;
    SpaceButton btnExit;
    SpaceButton btnAllmoney;
    SpaceButton btnShop;

    Music FonMusic;

    Texture imgMN;
    Texture imgFlyingSaucer;
    TextureRegion[] imgBG = new TextureRegion[4];
    Texture imgBGAtlas;


    FlyingSoucer flyingSauser;

    public ScreenMenu(Main main) {
        this.main = main;
        batch= main.batch;
        camera= main.camera;
        touch= main.touch;
        font70=main.font70;

        FonMusic= main.FonMusic;



        imgFlyingSaucer = new Texture("flyingSaucer.png");
        imgBGAtlas=new Texture("bgmenu.png");
        imgMN=new Texture("moneta.png");

        for (int i = 0; i< imgBG.length;i++) {
            imgBG[i] = new TextureRegion(imgBGAtlas, (i) * 900, 0, 900, 1600);
        }

        btnPlay = new SpaceButton(font70,"PLAY",480);
        btnSetting = new SpaceButton(font70,"setting",40,1100);
        btnLeaderboard = new SpaceButton(font70,"leaderboard",250);
        btnAbout = new SpaceButton(font70,"about",40,800);
        btnExit = new SpaceButton(font70,"exit",SCR_WIDTH-180,800);
        btnAllmoney= new  SpaceButton(font70,""+main.Allmoney,SCR_WIDTH-100,1550);
        btnShop = new SpaceButton(font70,"shop",SCR_WIDTH-200,1100);
        flyingSauser= new FlyingSoucer(250,SCR_HEIGHT/2);


    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touch);
            if(btnPlay.hit(touch.x,touch.y)){
               main.setScreen(main.screenGame);
                FonMusic.play();
                FonMusic.setVolume(0.3f);
                GameState=GAME;
                ;

            }
            if(btnSetting.hit(touch.x,touch.y)){
                main.setScreen(main.screenSettings);
            }
            if(btnLeaderboard.hit(touch.x,touch.y)){
                main.setScreen(main.screenLeaderboard);
                Iskeyboard=true;
            }
            if(btnAbout.hit(touch.x,touch.y)){
                main.setScreen(main.screenAbout);
            }
            if(btnExit.hit(touch.x,touch.y)){
                Gdx.app.exit();
            }
            if(btnShop.hit(touch.x,touch.y)){
                main.setScreen(main.screenShop);}



        }
        //flyingSauser.vX=2.5f;


       flyingSauser.moveflyingSauser();

        btnAllmoney.changeText(main.Allmoney);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG[phaseBg], 0, 0, SCR_WIDTH, SCR_HEIGHT);
        changePhase();
        batch.draw(imgFlyingSaucer,flyingSauser.scrX(),flyingSauser.scrY(),flyingSauser.width,flyingSauser.height);
        btnPlay.font.draw(batch,btnPlay.text,btnPlay.x,btnPlay.y);
        btnSetting.font.draw(batch,btnSetting.text,btnSetting.x,btnSetting.y);
        btnAbout.font.draw(batch,btnAbout.text,btnAbout.x, btnAbout.y);
        btnLeaderboard.font.draw(batch,btnLeaderboard.text,btnLeaderboard.x,btnLeaderboard.y);
        btnExit.font.draw(batch,btnExit.text,btnExit.x,btnExit.y);
        btnAllmoney.font.draw(batch,main.Allmoney<1000? btnAllmoney.text:main.Allmoney/1000+"k",btnAllmoney.x,btnAllmoney.y);
        btnShop.font.draw(batch,btnShop.text,btnShop.x, btnShop.y);
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
