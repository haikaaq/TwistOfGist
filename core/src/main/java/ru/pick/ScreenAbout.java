package ru.pick;

import static ru.pick.Main.SCR_HEIGHT;
import static ru.pick.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;


public class ScreenAbout implements Screen {
    private Main main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    Texture imgBG;
    Texture imgBG2;
    Texture imgBackAtlas;
    TextureRegion[] imgBack = new TextureRegion[2];
    boolean is1=true;
    boolean is2=false;

    SpaceButton btnBack;
    SpaceButton btnRight;
    SpaceButton btnLeft;
    SpaceButton btnText1;
    SpaceButton btnText2;
    SpaceButton btnText3;
    SpaceButton btnText4;


    public ScreenAbout(Main main) {
        this.main = main;
        batch= main.batch;
        camera= main.camera;
        touch= main.touch;
        font=main.font70;
        imgBG=new Texture("bgabout0.png");
        imgBG2=new Texture("bgabout1.png");
        imgBackAtlas= new Texture("buttonsLeftRight.png");


        btnBack = new SpaceButton(10,1500,90,90,0);
        btnRight = new SpaceButton(font,"ooo",SCR_WIDTH-80,SCR_HEIGHT/2 );
        btnLeft  = new SpaceButton(font,"ooo",10,SCR_HEIGHT/2);
        btnText1= new SpaceButton(font,"you have to shoot at enemies", 950);
        btnText2= new SpaceButton(font,"you must dodge enemies", 150);
        btnText3= new SpaceButton(font,"you get coins for each level", 950);
        btnText4= new SpaceButton(font,"even if you lost", 150);

        for (int e = 0; e < imgBack.length; e++) {

            imgBack[e] = new TextureRegion(imgBackAtlas, (e) * 200, 0, 200, 200);
        }









    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touch);
            if(btnBack.hit(touch.x,touch.y)){
                main.setScreen(main.screenMenu);
            }
            if(btnRight.hit(touch.x,touch.y)){
                is2=true;
                is1=false;
            }
            if(btnLeft.hit(touch.x,touch.y)){
                is1=true;
                is2=false;
            }

        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(is1){
            batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
            btnText1.font.draw(batch,btnText1.text,btnText1.x,btnText1.y);
            btnText2.font.draw(batch,btnText2.text,btnText2.x,btnText2.y);
        }
        else {
            batch.draw(imgBG2, 0, 0, SCR_WIDTH, SCR_HEIGHT);
            btnText3.font.draw(batch,btnText3.text,btnText3.x,btnText3.y);
            btnText4.font.draw(batch,btnText4.text,btnText4.x,btnText4.y);
        }
        btnLeft.font.draw(batch,btnLeft.text,btnLeft.x,btnLeft.y);
        btnRight.font.draw(batch,btnRight.text,btnRight.x,btnRight.y);

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

    }
}
