package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.network.api.ServiceType;
import com.dingtalk.open.app.stream.protocol.CommandType;

import java.util.Objects;

/**
 * @author feiyin
 * @date 2023/3/23
 */
public class Subscription {

    private CommandType type;
    private String topic;

    private ServiceType serviceType;

    public Subscription() {

    }
    public Subscription(CommandType type, String topic, ServiceType serviceType) {
        this.type = type;
        this.topic = topic;
        this.serviceType = serviceType;
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

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
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
