package com.amavr.femory.data;

import com.amavr.femory.models.GroupInfo;

import java.util.List;

public interface IRemoteStorage {
    void queryGroups(List<GroupInfo> sour_groups, IRemoteAnswerListener listener);
    GroupInfo addGroup(String key, String name);
    void delGroup(GroupInfo group);
}
