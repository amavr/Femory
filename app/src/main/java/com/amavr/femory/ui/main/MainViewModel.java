package com.amavr.femory.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.amavr.femory.data.IConsumer;
import com.amavr.femory.data.IRepository;
import com.amavr.femory.models.GroupInfo;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel implements IConsumer {

    static final String TAG = "XDBG.MainViewModel";

    MutableLiveData<List<GroupInfo>> groups = new MutableLiveData<>();
    IRepository repository;

    public MainViewModel() {
        init();
    }

    public MainViewModel(IRepository rep) {
        repository = rep;
        repository.addConsumer(this);
        init();
    }

    private void init() {
//        groups.add(generateGroupInfo("One"));
//        groups.add(generateGroupInfo("Two"));
//        groups.add(generateGroupInfo("Three"));
//        groups.add(generateGroupInfo("Four"));
    }


    public void setRepository(IRepository rep){
        repository = rep;
        repository.addConsumer(this);
    }

    /// запрос групп от интерфейса
    public MutableLiveData<List<GroupInfo>> getGroups() {
        return groups;
    }

    private GroupInfo generateGroupInfo(String name) {
        GroupInfo gi = new GroupInfo();
        gi.name = name;
        return gi;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
    }

    @Override
    public void setData(List<GroupInfo> data) {
        groups.setValue(data);
    }

    public void addGroup(String name) {
        repository.AddGroup(name);
    }

    public void delGroup(String key) {

    }
}
