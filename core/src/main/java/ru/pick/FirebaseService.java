package ru.pick;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;


import java.lang.reflect.Constructor;


public class FirebaseService {

    public static FirebaseManager create() {
        try {
            if (Gdx.app.getType() == Application.ApplicationType.Android) {
                Class<?> clazz = Class.forName("ru.pick.android.FirebaseAndroid");
               Constructor<?> constructor = clazz.getDeclaredConstructor();
               return (FirebaseManager) constructor.newInstance();


            } else {
                return new FirebaseDesktop();
            }
        } catch (Exception e) {
            Gdx.app.error("Firebase", "Error creating manager", e);
            return new FirebaseDesktop(); // Fallback
        }


    }
}
