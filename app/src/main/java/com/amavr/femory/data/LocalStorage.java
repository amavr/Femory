package com.amavr.femory.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amavr.femory.ext.Tools;
import com.amavr.femory.models.GroupInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalStorage implements ILocalStorage {

    private static final String TAG = "XDBG.LocalStorage";
    private static final String APPKEY = "APPID";
    private static final String PREF = "femory.db";
    private static final String GRPKEY = "groups";

    Context context;
    /// В локальном хранилище лежат только ключи списков и ключ приложения
    SharedPreferences mPrefs = null;

    Gson gson = new Gson();

    /// Ключ приложения
    String app_id = "";
    /// Ключи списков
    List<GroupInfo> groups = new ArrayList<>();

    public LocalStorage(Context context) {
        this.context = context;
        this.mPrefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        /// уникальный ключ приложения
        if (!this.mPrefs.contains(APPKEY)) {
            app_id = Tools.generateKey();
            this.mPrefs.edit().putString(APPKEY, app_id).commit();
        } else {
            app_id = this.mPrefs.getString(APPKEY, "");
        }
        Log.d(TAG, String.format("app_id: %s", app_id));

//        clear();
        loadGroups();
//        init();
    }

    private void clear() {
        this.groups.clear();
        saveGroups();
    }

    private void init() {
        groups = new ArrayList<>();
        groups.add(generateGroupInfo("One"));
        groups.add(generateGroupInfo("Two"));
        groups.add(generateGroupInfo("Three"));
        groups.add(generateGroupInfo("Four"));
    }

    GroupInfo generateGroupInfo(String name){
        GroupInfo gi = new GroupInfo();
        gi.name = name;
        gi.key = getNewGroupId();
        return gi;
    }

    @Override
    public String getAppId() {
        return this.app_id;
    }

    @Override
    public String getNewGroupId() {
        return getAppId() + ':' + Tools.generateKey();
    }

    @Override
    public List<GroupInfo> getGroups() {
        return this.groups;
    }

    @Override
    public GroupInfo getGroup(String key) {
        Log.d(TAG, String.format("key: %s", key));
        if(this.groups == null || key == null || key.length() == 0){
            return null;
        }
        for (GroupInfo gi: groups) {
            if(gi.key.equals(key))
                return gi;
        }
        return null;
    }

    @Override
    public void setGroups(List<GroupInfo> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        saveGroups();
    }

    @Override
    public void updateGroup(String key, GroupInfo group) {
        int i = findGroupOf(key);
        /// есть такая группа?
        if(i >= 0) {
            /// новое значение - null, значит была удалена из FB,
            /// надо удалять и локально
            if (group == null) {
                groups.remove(i);
            }
            /// замена объекта на новый
            else {
                groups.set(i, group);
            }
        }
        /// нет такой
        else{
            /// если группа не пустая - добавить
            if(group != null){
                groups.add(group);
            }
        }

        saveGroups();
    }

    void loadGroups() {
        groups.clear();
        String json = this.mPrefs.getString(GRPKEY, "[]");
        GroupInfo[] arr = this.gson.fromJson(json, GroupInfo[].class);
        groups.addAll(Arrays.asList(arr));
//        groups = new ArrayList<GroupInfo>(Arrays.asList(arr));
//        while (groups.remove(null)) ;
        Log.d(TAG, String.format("groups: %s", gson.toJson(groups)));
    }

    void saveGroups() {
        this.mPrefs.edit().putString(GRPKEY, gson.toJson(this.groups)).commit();
    }

    int findGroupOf(String key){
        Log.d(TAG, String.format("key: %s", key));
        int i = -1;
        if(this.groups == null || key == null || key.length() == 0){
            return i;
        }
        for (GroupInfo gi: groups) {
            i++;
            if(gi.key.equals(key))
                return i;
        }
        return -1;
    }
}
