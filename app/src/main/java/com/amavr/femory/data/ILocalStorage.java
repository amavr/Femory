package com.amavr.femory.data;

import com.amavr.femory.models.GroupInfo;

import java.util.List;

public interface ILocalStorage {
    String getAppId();
    String getNewGroupId();
    List<GroupInfo> getGroups();
    GroupInfo getGroup(String key);
    void setGroups(List<GroupInfo> groups);
    void updateGroup(String key, GroupInfo group);
}
