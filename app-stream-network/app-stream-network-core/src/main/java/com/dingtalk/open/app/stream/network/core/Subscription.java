package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.protocol.CommandType;

import java.util.Objects;

/**
 * @author feiyin
 * @date 2023/3/23
 */
public class Subscription {

    private CommandType type;
    private String topic;
    public Subscription() {

    }
    public Subscription(CommandType type, String topic) {
        this.type = type;
        this.topic = topic;
    }
    public CommandType getType() {
        return type;
    }
    public void setType(CommandType type) {
        this.type = type;
    }
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return type == that.type && Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, topic);
    }
}
