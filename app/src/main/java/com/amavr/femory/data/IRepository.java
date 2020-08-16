package com.amavr.femory.data;

import androidx.lifecycle.MutableLiveData;

import com.amavr.femory.models.GroupInfo;
import com.amavr.femory.models.ItemInfo;

import java.util.List;

public interface IRepository {
    void addConsumer(IConsumer consumer);
    void removeConsumer(IConsumer consumer);

    void AddGroup(String name);
    void DelGroup(String key);
}
