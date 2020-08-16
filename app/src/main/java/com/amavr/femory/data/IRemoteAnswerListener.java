package com.amavr.femory.data;

import com.amavr.femory.models.GroupInfo;

public interface IRemoteAnswerListener {
    void onAnswer(String key, GroupInfo group);
    void onError(Exception exception);
}
