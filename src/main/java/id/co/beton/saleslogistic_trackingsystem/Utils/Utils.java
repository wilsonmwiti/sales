package id.co.beton.saleslogistic_trackingsystem.Utils;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class Utils
 * accommodating function for sharedPreference Utilities
 */
public class Utils {

    /**
     * The Preferences.
     */
    protected SharedPreferences preferences;

    /**
     * function to get String Property from key given
     *
     * @param key the key
     * @return String string property
     */
    public String getStringProperty(String key) {
        return preferences.getString(key, "");
    }

    /**
     * function to set String Property from key and value given
     *
     * @param key   the key
     * @param value the value
     */
    public void setStringProperty(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value).apply();
    }

    /**
     * function to get boolean Property from key given
     *
     * @param key the key
     * @return boolean boolean property
     */
    public boolean getBooleanProperty(String key) {
        return preferences.getBoolean(key, false);
    }

    /**
     * function to set boolean Property from key and value given
     *
     * @param key   the key
     * @param value the value
     */
    public void setBooleanProperty(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value).apply();
    }

    /**
     * function to get integer Property from key given
     *
     * @param key the key
     * @return integer int property
     */
    public int getIntProperty(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * function to set integer Property from key and value given
     *
     * @param key   the key
     * @param value the value
     */
    public void setIntProperty(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value).apply();
    }

    /**
     * function to get Float Property from key given
     *
     * @param key the key
     * @return float float property
     */
    public float getFloatProperty(String key) {
        return preferences.getFloat(key, 0.0F);
    }

    /**
     * function to set Float Property from key and value given
     *
     * @param key   the key
     * @param value the value
     */
    public void setFloatProperty(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value).apply();
    }

    /**
     * function to get Long Property from key given
     *
     * @param key the key
     * @return long long property
     */
    public long getLongProperty(String key) {return preferences.getLong(key, 0);}

    /**
     * function to set Long Property from key and value given
     *
     * @param key   the key
     * @param value the value
     */
    public void setLongProperty(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value).apply();
    }

    public void setStringSet(String key, HashSet<String> value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public Set<String> getStringSets(String key) {
        return preferences.getStringSet(key, null);
    }

    public void saveArrayList(String key, ArrayList<String> list){
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<String> getArrayList(String key){
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }



    /**
     * Clearing all saved preferences, used for logging out
     */
    public void reset() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    /**
     * function to remove data from preferences based on key given
     *
     * @param key the key
     */
    public void removeByKey(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key).commit();
    }
}
