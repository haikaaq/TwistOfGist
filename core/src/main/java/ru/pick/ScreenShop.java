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
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenShop implements Screen {
    private static int SHOP=0,SKIN=1,SHOTS=2,BOOST=3;
    private long timeLastSpawnShots, timeShotsInterval = 300;
    private int ShotEven=0;
    public int ShipSkin=0;
    public int ShotsShots;
    public int ShotsBostCount;
    private int ShipDistance;
    private int ScreenShipNum=0;
    private int ScreenBoostNum=0;
    private int ScreenSHOTSNum=0;
    private int BuyBoostLevel=0;
    private int BuyShotLevel=0;
    private int BuyShipLevel=0;
    private int RealBoostCount;
    private int RealShotEven;
    private int RealShotsSkin;
    private int RealShipSkin;
    private int MinBoostLevel=0;
    private int MinSkinLevel=0;
    private int MinShotsLevel =0;
    private int ScreenState=SHOP;
    private boolean IsNewShipSkin;
    private boolean  IsNewShotSkin;
    private long timeRedSpawn, timeRed=700;
    private long timeGreenSpawn, timeGreen=700;

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
    Texture imgBG2;
    Texture imgGreen;
    Texture imgRed;
    Texture imgLongButtonAtlas;

    SpaceButton btnBack;
    SpaceButton btnSkins;
    SpaceButton btnShots;
    SpaceButton btnBoosts;
    SpaceButton btnRight;
    SpaceButton btnLeft;
    SpaceButton btnBuy;



    TextureRegion[] imgLongButton = new  TextureRegion[3];
    TextureRegion[][] imgShipatlas = new TextureRegion[5][12];
    TextureRegion[] imgShotatlas = new TextureRegion[4];

    Ship ship;

    List<Shot> shots = new ArrayList<>();
    public ScreenShop(Main main) {
        this.main = main;
        batch= main.batch;
        camera= main.camera;
        touch= main.touch;
        font=main.font70;
        font32=main.font32;

        imgGreen= new Texture("green.png");
        imgRed =new Texture("red.png");
        imgBG=new Texture("bgshop.png");
        imgBG2=new Texture("bgmenu2.png");
        imgLongButtonAtlas=new Texture("LongButton.png");
        btnAllmoney= new  SpaceButton(font,""+(main.Allmoney>=1000? main.Allmoney:main.Allmoney/1000+'k'),SCR_WIDTH-100,1550);
        btnBack = new SpaceButton(font,"Back",30,SCR_HEIGHT-50);

        btnBoosts= new SpaceButton(font,"BOOSTS",imgLongButtonAtlas,229,1.4f);

        btnSkins= new SpaceButton(font,"SKINS",70,229,1.4f);

        btnShots = new SpaceButton(font,"SHOTS",SCR_WIDTH-btnSkins.widht-70,229, 1.4f);


        btnBuy = new SpaceButton(font,"buy for "+price()+ " coins",255);
        btnRight = new SpaceButton(font32,"ooo",SCR_WIDTH-80,SCR_HEIGHT/2 );
        btnLeft  = new SpaceButton(font32,"ooo",10,SCR_HEIGHT/2);
        btnLeft.widht +=50;
        btnRight.widht +=50;



        imgShipsatlas =new Texture("atlas.png");
        imgShotsatlas = new Texture("shots.png");
        imgPlus= new Texture("plus.png");




        for (int j = 0; j < imgShipatlas.length; j++) {
            for (int i = 0; i < imgShipatlas[j].length; i++) {
                imgShipatlas[j][i] = new TextureRegion(imgShipsatlas, (i<7?i:12-i)*800, j*800, 800, 800);
            }}

        for (int i = 0; i < imgShotatlas.length;i++) {
            imgShotatlas[i] = new TextureRegion(imgShotsatlas, (i) * 100, 0, 100, 350);
        }
        for (int e = 0; e < imgLongButton.length; e++) {

            imgLongButton[e] = new TextureRegion(imgLongButtonAtlas, 0, (e)*193, 497, 193);
        }


        ship= new Ship(SCR_WIDTH/2,SCR_HEIGHT/2) ;
        ship.width=ship.height=460;
        ship.vY=0;
        LoadShop();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Vector3 Mousepose = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        camera.unproject(Mousepose);

        btnShots.ButtonsState(Mousepose.x,Mousepose.y);
        btnSkins.ButtonsState(Mousepose.x,Mousepose.y);
        btnBoosts.ButtonsState(Mousepose.x,Mousepose.y);
        btnBuy.ButtonsState(Mousepose.x,Mousepose.y);
        btnLeft.ButtonsState(Mousepose.x,Mousepose.y);
        btnRight.ButtonsState(Mousepose.x,Mousepose.y);
        btnBack.ButtonsState(Mousepose.x,Mousepose.y);





            if(btnBack.SetScreenButton){
                if (ScreenState==SHOP)main.setScreen(main.screenMenu);
               else ScreenState=SHOP;
            }
            if(btnShots.SetScreenButton){
                ScreenState=SHOTS;

            }
            if(btnBoosts.SetScreenButton){
                ScreenState=BOOST;
            }
            if(btnSkins.SetScreenButton){
                ScreenState=SKIN;

               // ChangeShip();
            }
            if (btnRight.SetScreenButton&&ship.x==SCR_WIDTH/2){
               // btnBuy.changeText("buy for "+price()+ " coins");
               if (ScreenState==BOOST){
                   ChangePlusShots();

               }
               if (ScreenState==SKIN){
                   ChangeShip(btnRight);

               }

               if (ScreenState==SHOTS){
                   ChangeShot(btnRight);

                }
            }
            if (btnLeft.SetScreenButton&&ship.x==SCR_WIDTH/2){

                if (ScreenState==BOOST) {
                    ChangeMinusShots();

                }
                if (ScreenState==SKIN){
                    ChangeShip(btnLeft);

                }
                if (ScreenState==SHOTS){
                    ChangeShot(btnLeft);


                }
            }
            if (btnBuy.SetScreenButton){
                if (main.Allmoney>=price()){


                    if (ScreenState==BOOST) {
                        if(ScreenBoostNum<=1&&price()!=0){
                        MinBoostLevel-=1;
                        timeGreenSpawn=TimeUtils.millis();
                        main.Allmoney-=price();
                        BuyBoostLevel=0;
                        ScreenBoostNum=0;
                        main.ShotsBostCount=ShotsBostCount;
                        main.ShotEven=ShotEven;}
                        if (price()==0){
                            main.ShotsBostCount=ShotsBostCount;
                            main.ShotEven=ShotEven;
                            timeGreenSpawn=TimeUtils.millis();
                        }
                        if(ScreenBoostNum>1)timeRedSpawn=TimeUtils.millis();}
                    if (ScreenState==SKIN){
                        if(ScreenShipNum==1&&price()!=0) {
                        MinSkinLevel-=1;
                        BuyShipLevel=0;
                        timeGreenSpawn=TimeUtils.millis();
                        main.Allmoney-=price();
                        ScreenShipNum=0;
                        main.ShipSkin=ShipSkin ;}
                        if (price()==0){
                            main.ShipSkin=ShipSkin;
                            timeGreenSpawn=TimeUtils.millis();
                        }
                        if(ScreenShipNum>1)timeRedSpawn=TimeUtils.millis();



                    }
                    if (ScreenState==SHOTS){
                        if(ScreenSHOTSNum==1&&price()!=0) {
                            BuyShotLevel=0;
                            MinShotsLevel -=1;
                            timeGreenSpawn=TimeUtils.millis();
                            main.Allmoney-=price();
                            ScreenSHOTSNum=0;
                            main.ShotsShots=ShotsShots;
                        }
                        if (price()==0){
                            main.ShotsShots=ShotsShots;
                            timeGreenSpawn=TimeUtils.millis();
                        }

                        if(ScreenSHOTSNum>1)timeRedSpawn=TimeUtils.millis();



                    }


                }
                else{timeRedSpawn=TimeUtils.millis();}









        }
        btnShots.changePhases();
        btnBoosts.changePhases();
        btnSkins.changePhases();
        btnLeft.changePhases();
        btnRight.changePhases();
        ///действия
        btnAllmoney.changeText(main.Allmoney);
        spavnShot();
        MoveShots();
        //if(!IsShipMove)
        moveship(0);
        btnBuy.changeText("buy for "+price()+ " coins");
        if (ScreenState==BOOST&&price()==0) {
            btnBuy.changeText("get it");
           }
        if (ScreenState==SKIN&&price()==0) {
            btnBuy.changeText("get it");
        }
        if (ScreenState==SHOTS&price()==0) {
            btnBuy.changeText("get it");
        }


        if (ScreenState==SHOP){
            ShotsBostCount= main.ShotsBostCount;
            ShotEven=main.ShotEven;
            ShipSkin= main.ShipSkin;
            ShotsShots = main.ShotsShots;
        }
        if (ScreenState==BOOST){
            ShotsBostCount=RealBoostCount;
            ShotEven=RealShotEven;
        }
        if (ScreenState==SHOTS){
            ShotsShots= RealShotsSkin;

        }
        if (ScreenState==SKIN){
            ShipSkin= RealShipSkin;

        }


        //for (Shot s: shots)s.vY=3;
        for (int i = shots.size() - 1; i >= 0; i--) {
        if (shots.get(i).OutOfscreen() ){
            shots.get(i).width=0;
            shots.get(i).height=0;
            if (TimeUtils.millis()>=timeLastSpawnShots+timeShotsInterval) {
                shots.remove(i);
                break;
            }}}



        ///отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        batch.draw(imgBG2, 0, 0, SCR_WIDTH, SCR_HEIGHT);



        if (ScreenState==SCREEN){
            batch.draw(imgLongButton[btnSkins.phase],btnSkins.imgX,btnSkins.imgY,btnSkins.imgWidht,btnSkins.imgHeight);
            batch.draw(imgLongButton[btnShots.phase],btnShots.imgX,btnShots.imgY,btnShots.imgWidht,btnShots.imgHeight);
            batch.draw(imgLongButton[btnBoosts.phase],btnBoosts.imgX,btnBoosts.imgY,btnBoosts.imgWidht,btnBoosts.imgHeight);

            btnSkins.font.draw(batch,btnSkins.text,btnSkins.x,btnSkins.y);
            btnShots.font.draw(batch,btnShots.text,btnShots.x,btnShots.y);
            btnBoosts.font.draw(batch,btnBoosts.text,btnBoosts.x,btnBoosts.y);

            for(Shot s : shots) {
                batch.draw(imgShotatlas[main.ShotsShots],s.scrX() , s.scrY(), s.width,s.height);}

            batch.draw(imgShipatlas[main.ShipSkin][ship.phase],ship.scrX(),ship.scrY(),ship.width,ship.height );

        }
        if (!(ScreenState==SCREEN)){
            btnRight.font.draw(batch,btnRight.text,btnRight.x,btnRight.y);
           btnLeft.font.draw(batch,btnLeft.text,btnLeft.x,btnLeft.y);
            btnBuy.font.draw(batch,btnBuy.text,btnBuy.x,btnBuy.y);

            for(Shot s : shots) {
                batch.draw(imgShotatlas[ShotsShots],s.scrX() , s.scrY(), s.width,s.height);}

            batch.draw(imgShipatlas[ShipSkin][ship.phase],ship.scrX(),ship.scrY(),ship.width,ship.height );
        }

        if (timeGreen()) batch.draw(imgGreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        if (timeRed()) batch.draw(imgRed, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, "Shop", 350, SCR_HEIGHT-20);
        btnAllmoney.font.draw(batch,btnAllmoney.text,btnAllmoney.x,btnAllmoney.y);

        btnBack.font.draw(batch,btnBack.text,btnBack.x,btnBack.y);
        batch.draw(main.screenMenu.imgMN,btnAllmoney.x-70,btnAllmoney.y-58,50,50);
        batch.end();


       SaveShop();




    }

    private boolean timeGreen(){

        return TimeUtils.millis()-timeGreenSpawn<=timeGreen;
    }
    private boolean timeRed(){

        return TimeUtils.millis()-timeRedSpawn<=timeRed;
    }


    private int price(){

        if (ScreenState==BOOST&&BuyBoostLevel>0) return 100*(BuyBoostLevel+Math.abs(MinBoostLevel));
        if (ScreenState==SKIN&&BuyShipLevel>0) return 300*(BuyShipLevel+Math.abs(MinSkinLevel));
        if (ScreenState==SHOTS&&BuyShotLevel>0) return 50*(BuyShotLevel+Math.abs(MinShotsLevel));
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
    private void spavnShot(){
        if(TimeUtils.millis()>timeLastSpawnShots+timeShotsInterval) {

            if (ShotsBostCount == 0) {
                shots.add(new Shot(ship.scrX() + ship.width / 2, ship.scrY() + 245));

            }

            for (int i = 0; i < (ShotEven==0?ShotsBostCount*2:(ShotsBostCount*2+1)); i++) {
                shots.add(new Shot(ship.scrX() + ship.width / 2, ship.scrY() + 245));


            }
            timeLastSpawnShots = TimeUtils.millis();



        }

    }
    public void MoveShots() {

    if (ShotsBostCount>0){
        int j =- ShotsBostCount;
        for(int r =(ShotEven==0? shots.size()-ShotsBostCount*2:shots.size()-(ShotsBostCount*2+1));r<shots.size();r++){

            shots.get(r).vX=j;
            shots.get(r).move();
            j++;
            if(ShotEven==0){
                if (j == 0) j=1;}

        }
        int e = 0;
        while(e <(ShotEven==0? shots.size()-ShotsBostCount*2:shots.size()-ShotsBostCount*2-1))  {

            shots.get(e).move(shots.get(e).vX);
            e++;

        }}
     else{
         for (Shot s: shots)s.move();
    }
    }





    private void MoveShip(SpaceButton b) {
       ShipDistance=0;
       if (b==btnRight) ship.vX=-40;
       else ship.vX=40;
    };




    private void ChangePlusShots(){

        if (ShotsShots<=imgShotatlas.length&&ScreenState==BOOST){


            if (ShotsBostCount<4) {
                BuyBoostLevel+=1;
                ScreenBoostNum+=1;
                MoveShip(btnRight);
                if (ShotEven < ShotsBostCount)  ShotEven= ShotsBostCount;
                else {
                    ShotEven= 0;
                    ShotsBostCount += 1;

                }
                RealBoostCount =ShotsBostCount;
                RealShotEven = ShotEven;
            }

        }




    }

    private void ChangeMinusShots() {
        if (ShotsShots <=imgShotatlas.length && ScreenState == BOOST) {

            if (ShotsBostCount>=0) {
                if (BuyBoostLevel>MinBoostLevel){
                    BuyBoostLevel-=1;
                    ScreenBoostNum-=1;
                    MoveShip(btnLeft);}
                if (ShotEven >= ShotsBostCount) ShotEven = 0;
                else {
                    ShotEven = ShotsBostCount;
                    ShotsBostCount -= 1;
                }
                RealBoostCount =ShotsBostCount;
                RealShotEven = ShotEven;
            }
        }
    }
    public void moveship(int a) {
        if (ShipDistance>=-(SCR_WIDTH+ship.width)&&ShipDistance<=(SCR_WIDTH+ship.width)){
        ship.x+=ship.vX;
        ShipDistance+=ship.vX;
        if(ScreenState==SKIN){
            if(ShipDistance<-SCR_WIDTH+ship.width/2&& IsNewShipSkin){ShipSkin+=1 ; RealShipSkin=ShipSkin; IsNewShipSkin=false;}
            if(ShipDistance>SCR_WIDTH-ship.width/2&& IsNewShipSkin) {ShipSkin-=1;RealShipSkin=ShipSkin; IsNewShipSkin=false;}

        }
        if(ScreenState==SHOTS){
                if(ShipDistance<-SCR_WIDTH+ship.width/2&& IsNewShotSkin){ShotsShots+=1 ; RealShotsSkin=ShotsShots ; IsNewShotSkin=false;}
                if(ShipDistance>SCR_WIDTH-ship.width/2&& IsNewShotSkin) {ShotsShots-=1; RealShotsSkin=ShotsShots; IsNewShotSkin=false;}
        }

        if(ship.x<-ship.width/2){ ship.x=SCR_WIDTH+ship.width/2;}}
        if(ship.x>SCR_WIDTH+ship.width/2){ ship.x=-ship.width/2;}
        ship.changePhase();



    }
    private void ChangeShip(SpaceButton b) {
        if (b == btnRight) {
            if (ShipSkin < imgShipatlas.length-1 && ScreenState == SKIN) {
                IsNewShipSkin=true;
                BuyShipLevel += 1;
                ScreenShipNum+=1;
                MoveShip(btnRight);
            }

        }

        if (b == btnLeft) {
                if (ShipSkin > 0 && ScreenState == SKIN) {

                    if (BuyShipLevel >= MinSkinLevel) {
                        BuyShipLevel -= 1;
                        ScreenShipNum-=1;
                        MoveShip(btnLeft);
                        if (IsNewShipSkin)ShipSkin-=1;
                        IsNewShipSkin=true;

                    }

                }
        }
     }
    private void ChangeShot(SpaceButton b) {
        if (b == btnRight) {
            if (ShotsShots < imgShotatlas.length-1&& ScreenState == SHOTS) {
                IsNewShotSkin=true;

                BuyShotLevel += 1;
                ScreenSHOTSNum+=1;
                MoveShip(btnRight);




            }}
        if (b == btnLeft) {
            if (ShotsShots >= 0 && ScreenState == SHOTS) {

                if (BuyShotLevel > MinShotsLevel) {

                    IsNewShotSkin=true;
                    BuyShotLevel -= 1;
                    ScreenSHOTSNum-=1;

                    MoveShip(btnLeft);



                }}}}



   private void SaveShop(){
            Preferences prefs = Gdx.app.getPreferences("игровые ресурсы");

            prefs.putInteger("скин космолета",main.ShipSkin);
            prefs.putInteger("скин выстрела",main.ShotsShots);
            prefs.putInteger("четность",main.ShotEven);
            prefs.putInteger("количество выстрелов",main.ShotsBostCount);
          //  prefs.putInteger();
            prefs.flush();}

   private void LoadShop() {
        Preferences prefs = Gdx.app.getPreferences("TableRecords");

            main.ShotsShots= prefs.getInteger("скин выстрела",0);
            main.ShotEven=prefs.getInteger("четность",0);
            main.ShotsBostCount=prefs.getInteger("количество выстрелов",0);
            main.ShipSkin=prefs.getInteger("скин космолета",0);


    }

}



