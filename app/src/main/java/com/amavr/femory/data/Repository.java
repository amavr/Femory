package com.amavr.femory.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.amavr.femory.models.GroupInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository, IRemoteAnswerListener {

    static final String TAG = "XDBG.Repository";

    Gson gson = new Gson();

    List<IConsumer> consumers = new ArrayList<>();
    ILocalStorage localStorage;
    IRemoteStorage remoteStorage;

    public Repository(ILocalStorage localStorage, IRemoteStorage remoteStorage){
        this.localStorage = localStorage;
        this.remoteStorage = remoteStorage;
        init();
    }

    void init(){
        /// запрос того что есть локально
        List<GroupInfo> groups = localStorage.getGroups();
        sendToConsumers(groups);
        /// запрос внешних данных, их обновлений для локальных данных
        remoteStorage.queryGroups(groups, this);
    }

    void sendToConsumers(List<GroupInfo> groups){
        for(IConsumer consumer: consumers){
            consumer.setData(groups);
        }
    }

    @Override
    public void addConsumer(IConsumer consumer) {
        consumers.add(consumer);
        consumer.setData(localStorage.getGroups());
    }

    @Override
    public void removeConsumer(IConsumer consumer) {
        consumers.remove(consumer);
    }

    @Override
    public void AddGroup(String name) {
        String key = this.localStorage.getNewGroupId();
        GroupInfo gi = this.remoteStorage.addGroup(key, name);
        this.localStorage.updateGroup(key, gi);
    }

    @Override
    public void DelGroup(String key) {
        GroupInfo gi = this.localStorage.getGroup(key);
        this.remoteStorage.delGroup(gi);
    }

    @Override
    public void onAnswer(String key, GroupInfo group) {
        localStorage.updateGroup(key, group);
        List<GroupInfo> groups = localStorage.getGroups();
        sendToConsumers(groups);
        Log.d(TAG, String.format("onAnswer(%s, %s) ", key, gson.toJson(group)));
    }

    @Override
    public void onError(Exception exception) {
        /// TODO: отдать в интерфейс
        Log.e(TAG, "onError: ", exception);
   }
}
