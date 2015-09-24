package com.x.dauglas.quickframework.extend;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ConfigTool class
 */
@SuppressWarnings("unused")
public class ConfigUtils {
    private final static HashMap<String, ConfigUtils> sConfigMap = new HashMap<>();
    private static String DefaultName = "AppConfig";
    private String _name;
    private SharedPreferences _preferences;

    public ConfigUtils(Context context, String name) {
        _name = name;
        _preferences = context.getSharedPreferences(_name, Context.MODE_PRIVATE);
    }

    public static ConfigUtils create(Context context) {
        ConfigUtils ret;
        synchronized (sConfigMap) {
            if (!sConfigMap.containsKey(DefaultName)) {
                ret = new ConfigUtils(context, DefaultName);
                sConfigMap.put(DefaultName, ret);
            } else {
                ret = sConfigMap.get(DefaultName);
            }
        }
        return ret;
    }

    public static ConfigUtils create(Context context, String name) {
        ConfigUtils ret;
        synchronized (sConfigMap) {
            if (!sConfigMap.containsKey(name)) {
                ret = new ConfigUtils(context, name);
                sConfigMap.put(name, ret);
            } else {
                ret = sConfigMap.get(name);
            }
        }
        return ret;
    }

    public static ConfigUtils getInstance() {
        ConfigUtils ret = null;
        if (sConfigMap.containsKey(DefaultName)) {
            ret = sConfigMap.get(DefaultName);
        } else {
            LogUtils.e("ConfigTool must call create() before.");
        }
        return ret;
    }

    public static ConfigUtils getInstance(String name) {
        ConfigUtils ret = null;
        if (sConfigMap.containsKey(name)) {
            ret = sConfigMap.get(name);
        } else {
            LogUtils.e(name + "not found, " + "must call create() before.");
        }
        return ret;
    }

    public boolean hasKey(String key) {
        return _preferences.contains(key);
    }

    public void config(String key, boolean value) {
        Editor editor = _preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void config(String key, float value) {
        Editor editor = _preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void config(String key, double value) {
        Editor editor = _preferences.edit();
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    public void config(String key, String value) {
        Editor editor = _preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void config(String key, long value) {
        Editor editor = _preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void config(String key, int value) {
        Editor editor = _preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return _preferences.getBoolean(key, false);
    }

    public float getFloat(String key) {
        return _preferences.getFloat(key, -1.0f);
    }

    public int getInt(String key) {
        return _preferences.getInt(key, -1);
    }

    public long getLong(String key) {
        return _preferences.getLong(key, -1);
    }

    public String getString(String key) {
        return _preferences.getString(key, "");
    }

    public double getDouble(String key) {
        return Double.valueOf(_preferences.getString(key, "-1.0"));
    }

    public void clear() {
        Editor editor = _preferences.edit();
        editor.clear();
        editor.apply();
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("config file name: ").append(_name);
        sb.append("\r\n");

        Iterator itr = _preferences.getAll().entrySet().iterator();
        if (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            sb.append(entry.getKey()).append(" = ").append(entry.getValue());
            sb.append("\r\n");

            while (itr.hasNext()) {
                entry = (Map.Entry) itr.next();
                sb.append(entry.getKey()).append(" = ").append(entry.getValue());
                sb.append("\r\n");
            }
        } else {
            sb.append(" is empty.");
        }

        return sb.toString();
    }
}
