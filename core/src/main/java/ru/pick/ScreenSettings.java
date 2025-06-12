package ru.pick;

import static ru.pick.Main.ACCELEROMETER;
import static ru.pick.Main.JOYSTIK;
import static ru.pick.Main.JOYSTIK_LEFT;
import static ru.pick.Main.JOYSTIK_RIGHT;
import static ru.pick.Main.SCREEN;
import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;
import static ru.pick.Main.controls;

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


public class ScreenSettings implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font70;
    private Levels.Level curentlevel;
    Texture imgBG;
    Texture imgBG2;
    Texture imgON;
    Texture imgOFF;
    Texture imgLogo;
    Texture imgBackAtlas;
    TextureRegion[] imgBack = new TextureRegion[2];
    SpaceButton btnControls;
    SpaceButton btnScreen;
    SpaceButton btnJoystic;
    SpaceButton btnAccelerometr;
    SpaceButton btnBack;
    SpaceButton btnLeft;
    SpaceButton btnRight;
    SpaceButton btnShoot;
    SpaceButton btnActionSounds;
    SpaceButton btnFonMusic;
    SpaceButton btnMusic;


    public ScreenSettings(Main main) {
        this.main = main;
        batch= main.batch;
        camera= main.camera;
        touch= main.touch;
        font70=main.font70;
       loadLevel(main.level);
        controls=SCREEN;
        imgBG=new Texture("bgset.png");
        imgON=new Texture("on.png");
        imgOFF=new Texture("off.png");
        imgBG2 = new Texture("bgmenu2.png");
        imgLogo= new Texture("logo.png");


        imgBackAtlas= new Texture("buttonsLeftRight.png");

       // btnControls = new SpaceButton(font70,"Controls",100,1100);
        btnMusic = new SpaceButton(font70,"Music",100,1100);
      /*  btnScreen = new SpaceButton(font70,"Screen",900);
        btnJoystic = new SpaceButton(font70,"Joystick",1000);
       // btnAccelerometr = new SpaceButton(font70,"Accelerometr",800);*/
        //btnLeft= new SpaceButton(font70, "Left",SCR_WIDTH/2-60,900);
        //btnRight= new SpaceButton(font70, "Right",SCR_WIDTH/2-60,800);
        btnFonMusic = new SpaceButton(font70, "Background music",900);
        btnActionSounds = new SpaceButton(font70, "Actions Sounds",800);

        if (curentlevel.isSettingLevel){
            btnShoot= new SpaceButton(font70,"Enemies are shooting",400);
            btnShoot.widht+=60;
        }

        btnBack = new SpaceButton(10,1500,90,90,0);

        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }

        btnActionSounds.widht+=60;
        btnFonMusic.widht+=60;
       // btnScreen.widht+=60;
       // btnJoystic.widht+=60;
        //btnLeft.widht+=60;
      //  btnRight.widht+=60;
       // btnAccelerometr.widht+=60;


    }


    @Override
    public void show() {
        loadLevel(main.level);

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touch);
            if(btnBack.hit(touch.x,touch.y))
               {main.setScreen(main.screenMenu);}

            /*if(btnAccelerometr.hit(touch.x-60, touch.y)) {
                if(controls==ACCELEROMETER)
                    controls=SCREEN;
                else{
                    controls=ACCELEROMETER;}

                }

            if(btnScreen.hit(touch.x-60, touch.y)) {
                if(controls==SCREEN)
                        controls=ACCELEROMETER;
                else {controls = SCREEN;}}

            if(btnJoystic.hit(touch.x, touch.y)) {
                if(controls==JOYSTIK)
                     controls=SCREEN;

                else{
                    controls=JOYSTIK;}}
            if(controls==JOYSTIK||controls==JOYSTIK_LEFT||controls==JOYSTIK_RIGHT){
                if(btnRight.hit(touch.x-60, touch.y)) {
                    if(controls==JOYSTIK_RIGHT)
                        controls=JOYSTIK_LEFT;
                    else{
                        controls=JOYSTIK_RIGHT;}}
                if(btnLeft.hit(touch.x-60, touch.y)) {
                    if(controls==JOYSTIK_LEFT)
                    controls=JOYSTIK_RIGHT;
                    else{
                        controls=JOYSTIK_LEFT;}}}*/
            if(curentlevel.isSettingLevel) {
                if (btnShoot.hit(touch.x - 60, touch.y)) {
                    curentlevel.isRexlexLevel = false;
                    curentlevel.isShots = true;
                }
            }
            if (btnFonMusic.hit(touch.x-60,touch.y)){
                if(main.isFonMusic)main.isFonMusic=false;
                else main.isFonMusic=true;
            }

                if (btnActionSounds.hit(touch.x - 60, touch.y)) {
                    if (main.isActionSounds) main.isActionSounds = false;
                    else main.isActionSounds = true;

            }

        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        batch.draw(imgLogo,SCR_WIDTH/2-240,1070,480,380);
        batch.draw(imgBG2, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        font70.draw(batch, "SETTINGS", 0, 1550,SCR_WIDTH, Align.center,true);
       /* btnControls.font.draw(batch, btnControls.text, btnControls.x, btnControls.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        batch.draw(controls==SCREEN?imgON:imgOFF,btnScreen.x+btnScreen.widht, btnScreen.y-btnScreen.height -10,70,70 );
        btnJoystic.font.draw(batch, btnJoystic.text, btnJoystic.x, btnJoystic.y);
        btnAccelerometr.font.draw(batch, btnAccelerometr.text, btnAccelerometr.x, btnAccelerometr.y);*/
        btnMusic.font.draw(batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnFonMusic.font.draw(batch, btnFonMusic.text, btnFonMusic.x, btnFonMusic.y);
        batch.draw(main.isFonMusic?imgON:imgOFF,btnFonMusic.x+btnFonMusic.widht, btnFonMusic.y-btnFonMusic.height -10,70,70 );
        btnActionSounds.font.draw(batch, btnActionSounds.text, btnActionSounds.x, btnActionSounds.y);
       if (curentlevel.isSettingLevel){btnShoot.font.draw(batch,btnShoot.text,btnShoot.x,btnShoot.y);}
        if(curentlevel.isSettingLevel) {
            batch.draw(curentlevel.isRexlexLevel ? imgON : imgOFF, btnShoot.x + btnShoot.widht, btnShoot.y - btnShoot.height - 10, 70, 70);

        }
        batch.draw(main.isActionSounds ? imgON : imgOFF, btnActionSounds.x + btnActionSounds.widht, btnActionSounds.y - btnActionSounds.height - 10, 70, 70);
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

        batch.draw(imgBack[btnBack.type],btnBack.imgX,btnBack.imgY,btnBack.imgWidht,btnBack.imgHeight);


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
    }



    private void loadLevel(int level){
        curentlevel=Levels.LEVELS[level];
    }

}
