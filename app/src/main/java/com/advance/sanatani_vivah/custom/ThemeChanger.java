package com.advance.sanatani_vivah.custom;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeChanger {
    public static final String lightMode = "light";
    public static final String darkMode = "dark";
    public static final String batterySaverMode = "battery";
    public static final String defaulMode = "default";

    public void applyTheme(String theme){
        switch (theme) {
            case lightMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case darkMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case batterySaverMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
            case defaulMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}
