package com.example.mid1;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Utility class to manage app themes (Light/Dark)
 * Persists theme preference in SharedPreferences
 */
public class ThemeManager {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String THEME_KEY = "selected_theme";
    private static final String THEME_LIGHT = "light";
    private static final String THEME_DARK = "dark";

    private static ThemeManager instance;
    private SharedPreferences sharedPreferences;
    private String currentTheme;

    private ThemeManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getString(THEME_KEY, THEME_LIGHT);
        applyTheme(currentTheme);
    }

    public static synchronized ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context);
        }
        return instance;
    }

    /**
     * Apply theme globally
     */
    public void applyTheme(String theme) {
        if (THEME_DARK.equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        currentTheme = theme;
        saveThemePreference(theme);
    }

    /**
     * Toggle between light and dark themes
     */
    public void toggleTheme() {
        if (THEME_LIGHT.equals(currentTheme)) {
            applyTheme(THEME_DARK);
        } else {
            applyTheme(THEME_LIGHT);
        }
    }

    /**
     * Get current theme
     */
    public String getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Check if dark theme is active
     */
    public boolean isDarkTheme() {
        return THEME_DARK.equals(currentTheme);
    }

    /**
     * Save theme preference to SharedPreferences
     */
    private void saveThemePreference(String theme) {
        sharedPreferences.edit().putString(THEME_KEY, theme).apply();
    }
}

