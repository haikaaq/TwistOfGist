package ru.pick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.List;

public class LanguageManager {
    public static I18NBundle currentBundle;
    public static I18NBundle ruBundle;
    public static I18NBundle enBundle;

    // Загружаем оба языка при старте игры
    public static void loadBundles() {
        ruBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/Strings_ru"));
        enBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/Strings_en"));
        currentBundle = ruBundle; // Язык по умолчанию
    }


    public static void setLanguage(String lang) {
        if (lang.equals("ru")) {
            currentBundle = ruBundle;
        } else {
            currentBundle = enBundle;
        }


    }

    // Получаем строку для текущего языка
    public static String get(String key) {
        return currentBundle.get(key);
    }

   // Обновляем все UI-элементы (пример ниже)

}
