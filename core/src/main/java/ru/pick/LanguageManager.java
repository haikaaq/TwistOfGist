package ru.pick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

public class LanguageManager {
    private static I18NBundle currentBundle;
    private static I18NBundle ruBundle;
    private static I18NBundle enBundle;

    // Загружаем оба языка при старте игры
    public static void loadBundles() {
        ruBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/Strings_ru"));
        enBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/Strings_en"));
        currentBundle = enBundle; // Язык по умолчанию
    }


    public static void setLanguage(String lang) {
        if (lang.equals("ru")) {
            currentBundle = ruBundle;
        } else {
            currentBundle = enBundle;
        }

        //updateAllUI();
    }

    // Получаем строку для текущего языка
    public static String get(String key) {
        return currentBundle.get(key);
    }

   /* // Обновляем все UI-элементы (пример ниже)
    private static void updateAllUI() {
        for (LocalizableUI element : uiElements) {
            element.updateLanguage();
        }
    }

    // Список всех элементов, которые нужно обновить
    private static List<LocalizableUI> uiElements = new ArrayList<>();

    // Регистрируем UI-элемент для обновления
    public static void registerUI(LocalizableUI element) {
        uiElements.add(element);
    }*/
}
