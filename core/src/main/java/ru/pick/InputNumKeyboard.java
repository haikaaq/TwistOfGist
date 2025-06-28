package ru.pick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class InputNumKeyboard {

    String keysFileName = "keys.png";
    private final BitmapFont font;

    private final float x, y; // координаты
    private final float keyboardWidth, keyboardHeight; // ширина и высота всей клавиатуры
    private final float keyWidth, keyHeight; // ширина и высота каждой кнопки
    private final float padding = 0; // расстояние между кнопками
    private final int enterTextLength; // длина вводимого текста

    boolean isKeyboardShow;
    public boolean endOfEdit;

    private String num ="" ; // вводимый текст
    // текст на кнопках
    private static final String NUM = "123~4567890^";
    private String numbers = NUM;

    private final Texture imgAtlasKeys; // все изображения кнопок
    private final TextureRegion imgEditText; // поле ввода
    private final TextureRegion imgKeyUP, imgKeyDown; // кнопка выпуклая/вдавленная
    private final TextureRegion imgKeyEnter,imgKeyBS; // картинки управляющих кноп

    private long timeStartPressKey, timeDurationPressKey = 150; // длительность надавливания кнопки
    private int keyPressed = -1; // код нажатой кнопки
    private final Array<InputNumKeyboard.Key> keys = new Array<>(); // список всех кноп

    public InputNumKeyboard(BitmapFont font, float scrWidth, float scrHeight, int enterTextLength){
        this.font = font;
        this.enterTextLength = enterTextLength; // количество вводимых символов

        imgAtlasKeys = new Texture(keysFileName);
        imgKeyUP = new TextureRegion(imgAtlasKeys, 0, 0, 256, 256);
        imgKeyDown = new TextureRegion(imgAtlasKeys, 256, 0, 256, 256);
        imgEditText = new TextureRegion(imgAtlasKeys, 256*2, 0, 256, 256);
        imgKeyBS = new TextureRegion(imgAtlasKeys, 256*3, 0, 256, 256);
        imgKeyEnter = new TextureRegion(imgAtlasKeys, 256*4, 0, 256, 256);


        // задаём параметры клавиатуры
        keyboardWidth = scrWidth/1.3f;
        keyboardHeight = scrHeight*3/9.2f;
        x = (scrWidth- keyboardWidth)/2;
        y = keyboardHeight+scrHeight/8f ;
        keyWidth = keyboardWidth/4;
        keyHeight = keyboardHeight/3;
        createKBD();
    }

    // создание кнопок клавиатуры по рядам
    private void createKBD(){
        int j = 0;
        for (int i = 0; i < 4; i++, j++)
            keys.add(new InputNumKeyboard.Key(i*keyWidth+x, y-keyHeight*2, keyWidth-padding, keyHeight-padding, numbers.charAt(j)));

        for (int i = 0; i < 4; i++, j++)
            keys.add(new InputNumKeyboard.Key(i*keyWidth+x, y-keyHeight*3, keyWidth-padding, keyHeight-padding, numbers.charAt(j)));

        for (int i = 0; i < 4; i++, j++)
            keys.add(new InputNumKeyboard.Key(i*keyWidth+x, y-keyHeight*4, keyWidth-padding, keyHeight-padding, numbers.charAt(j)));

    }


    // задаём/меняем раскладку символов на всех кнопках
    private void setCharsKBD() {
        int j = 0;
        for (int i = 0; i < 4; i++, j++)
            keys.get(j).letter = numbers.charAt(j);

        for (int i = 0; i < 4; i++, j++)
            keys.get(j).letter = numbers.charAt(j);

        for (int i = 0; i < 4; i++, j++)
            keys.get(j).letter = numbers.charAt(j);


    }

    // рисуем клавиатуру и вводимый текст
    public void draw(SpriteBatch batch){
        if(isKeyboardShow) {
            // рисуем кнопки
            for (int i = 0; i < keys.size; i++) {
                drawImgKey(batch, i, keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
            }
            // рисуем вводимый текст
            batch.draw(imgEditText,  x + keyWidth/2, y - keyHeight, (keyboardWidth-keyWidth), keyHeight);
            font.draw(batch, num, 2 * keyWidth + x + keyWidth / 2, keys.get(0).letterY + keyHeight, keyboardWidth - 5 * keyWidth - padding, Align.center, false);
        }
    }

    // рисуем каждую кнопку
    private void drawImgKey(SpriteBatch batch, int i, float x, float y, float width, float height){
        float dx, dy;
        if(keyPressed == i){ // если нажата, то рисуем нажатую кнопку
            batch.draw(imgKeyDown, x, y, width, height);
            dx = 2;
            dy = -2;
            if(TimeUtils.millis() - timeStartPressKey > timeDurationPressKey){
                keyPressed = -1;
            }
        }
        else { // рисуем отжатую кнопку
            dx = 0;
            dy = 0;
            batch.draw(imgKeyUP, x, y, width, height);
        }

        // выводим символы на кнопки
        switch (numbers.charAt(i)) {
            case '~': batch.draw(imgKeyBS, x+dx, y+dy, width, height); break; // backspace
            case '^': batch.draw(imgKeyEnter, x+dx, y+dy, width, height); break; // enter
            default: // все прочие символы
                font.draw(batch, ""+keys.get(i).letter, keys.get(i).letterX+dx, keys.get(i).letterY+dy);
        }
    }

    // проверяем, куда нажали
    public boolean touch(float tx, float ty){
        if(isKeyboardShow) {
            for (int i = 0; i < keys.size; i++) {
                if (!keys.get(i).hit(tx, ty).isEmpty()) {
                    keyPressed = i;
                    setText(i);
                    timeStartPressKey = TimeUtils.millis();
                }
            }
            // окончание редактирования ввода (нажата кнопка enter)
            if (endOfEdit) {
                endOfEdit = false;
                isKeyboardShow = false;
                return true;
            }
        }
        return false;
    }

    // обработка нажатия кнопок
    private void setText(int i){
        switch (numbers.charAt(i)) {
            case '~': // backspace
                if(!num.isEmpty()) num = num.substring(0, num.length() - 1);
                break;
            case '^': // enter
                if(num.isEmpty()) break;
                endOfEdit = true;
                break;

            default: // ввод символов
                if(num.length()< enterTextLength) num += numbers.charAt(i);

                setCharsKBD();
        }
    }

    // выдача отредактированного текста
    public String getText() {
        return num;
    }

    // класс отдельной кнопки виртуальной клавиатуры
    private class Key {
        float x, y;
        float width, height;
        char letter; // символ на кнопке
        float letterX, letterY; // координаты вывода символа

        private Key (float x, float y, float width, float height, char letter) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.letter = letter;
            letterX = x + width/3;
            letterY = y + height - (height - font.getCapHeight())/2;
        }

        private String hit(float tx, float ty){
            if (x<tx && tx<x+width && y<ty && ty<y+height) {
                return "" + letter;
            }
            return "";
        }
    }

    public void start(){
        isKeyboardShow = true;
    }

    public void dispose(){
        imgAtlasKeys.dispose();
    }

}
