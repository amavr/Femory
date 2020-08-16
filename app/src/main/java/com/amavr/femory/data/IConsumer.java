package com.amavr.femory.data;

import com.amavr.femory.models.GroupInfo;

import java.util.List;

public interface IConsumer {
    /**
     * Передача групп в интерфейс
     * @param data группы
     */
    void setData(List<GroupInfo> data);
}
