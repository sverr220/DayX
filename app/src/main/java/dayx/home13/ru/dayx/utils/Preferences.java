package dayx.home13.ru.dayx.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class Preferences {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String PUSH_TOKEN = "push_token";

    // ===========================================================
    // Fields
    // ===========================================================

    private static OnSharedPreferenceChangeListener onChangeListener =
            new OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                }
            };

    private static SharedPreferences preferences;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Получить настройки из памяти и запустить их работу.
     */
    public static void start(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.registerOnSharedPreferenceChangeListener(onChangeListener);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public static String getToken() {
        return getString(PUSH_TOKEN, "");
    }

    public static void setToken(String pValue) {
        setValue(PUSH_TOKEN, pValue);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Сохранить данные.
     */
    private static void setValue(final String key, final Object value) {
        try {
            if (preferences == null)
                return;

            if (value instanceof String)
                preferences.edit().putString(key, (String) value).apply();
            else if (value instanceof Integer)
                preferences.edit().putInt(key, (Integer) value).apply();
            else if (value instanceof Float)
                preferences.edit().putFloat(key, (Float) value).apply();
            else if (value instanceof Boolean)
                preferences.edit().putBoolean(key, (Boolean) value).apply();
            else if (value instanceof Long)
                preferences.edit().putLong(key, (Long) value).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Получить данные.
     */
    private static String getString(final String key, final String defValue) {
        try {
            if (preferences == null || !preferences.contains(key))
                return defValue;
            return preferences.getString(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Получить данные.
     */
    public static int getInt(final String key, final int defValue) {
        try {
            if (preferences == null || !preferences.contains(key))
                return defValue;
            return preferences.getInt(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Получить данные.
     */
    private static boolean getBoolean(final String key, final boolean defValue) {
        try {
            if (preferences == null || !preferences.contains(key))
                return defValue;
            return preferences.getBoolean(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * Получить данные.
     */
    private static long getLong(final String key, final long defValue) {
        try {
            if (preferences == null || !preferences.contains(key))
                return defValue;

            return preferences.getLong(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }
}
