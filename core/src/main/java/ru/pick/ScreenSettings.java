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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;


public class ScreenSettings implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font70;
    Texture imgBG;
    Texture imgON;
    Texture imgOFF;

    SpaceButton btnControls;
    SpaceButton btnScreen;
    SpaceButton btnJoystic;
    SpaceButton btnAccelerometr;
    SpaceButton btnBack;
    SpaceButton btnLeft;
    SpaceButton btnRight;


    public ScreenSettings(Main main) {
        this.main = main;
        batch= main.batch;
        camera= main.camera;
        touch= main.touch;
        font70=main.font70;

        controls=SCREEN;
        imgBG=new Texture("bgset.png");
        imgON=new Texture("on.png");
        imgOFF=new Texture("off.png");
        btnBack = new SpaceButton(font70,"Back",30,1550);


        btnControls = new SpaceButton(font70,"Controls ",100,1200);
        btnScreen = new SpaceButton(font70,"Screen ",1000);
        btnJoystic = new SpaceButton(font70,"Joystick ",1100);
        btnAccelerometr = new SpaceButton(font70,"Accelerometr ",900);
        btnLeft= new SpaceButton(font70, "Left",SCR_WIDTH/2-60,1000);
        btnRight= new SpaceButton(font70, "Right",SCR_WIDTH/2-60,900);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touch);
            if(btnBack.hit(touch.x,touch.y))
               {main.setScreen(main.screenMenu);}

            if(btnAccelerometr.hit(touch.x-60, touch.y)) {
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
                        controls=JOYSTIK_LEFT;}}}

        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        font70.draw(batch, "SETTINGS", 0, 1500,SCR_WIDTH, Align.center,true);
        btnControls.font.draw(batch, btnControls.text, btnControls.x, btnControls.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        batch.draw(controls==SCREEN?imgON:imgOFF,btnScreen.x+btnScreen.widht, btnScreen.y-btnScreen.height-10,70,70 );
        btnJoystic.font.draw(batch, btnJoystic.text, btnJoystic.x, btnJoystic.y);
        btnAccelerometr.font.draw(batch, btnAccelerometr.text, btnAccelerometr.x, btnAccelerometr.y);
        batch.draw(controls==ACCELEROMETER?imgON:imgOFF,btnAccelerometr.x+btnAccelerometr.widht, btnAccelerometr.y-btnAccelerometr.height-10,70,70 );
        if (controls==JOYSTIK||controls==JOYSTIK_LEFT||controls==JOYSTIK_RIGHT){
            btnLeft.font.draw(batch,btnLeft.text,btnLeft.x,btnLeft.y);
            batch.draw(controls==JOYSTIK_LEFT?imgON:imgOFF,btnLeft.x+btnLeft.widht, btnLeft.y-btnLeft.height-10,70,70 );
            btnRight.font.draw(batch,btnRight.text,btnRight.x,btnRight.y);
            batch.draw(controls==JOYSTIK_RIGHT?imgON:imgOFF,btnRight.x+btnRight.widht, btnRight.y-btnRight.height-10,70,70 );
            btnAccelerometr.y=700;
            btnScreen.y=800;
        }
        else {btnAccelerometr.y=900;
              btnScreen.y=1000;}

        btnBack.font.draw(batch,btnBack.text,btnBack.x,btnBack.y);

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

    private void saveSettings(){
        Preferences prefs=Gdx.app.getPreferences("PickofSettongs");
        prefs.putInteger("Controls", controls);




    }



}
