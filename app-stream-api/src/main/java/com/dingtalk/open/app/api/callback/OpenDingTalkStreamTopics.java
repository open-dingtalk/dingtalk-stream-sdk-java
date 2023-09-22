package com.dingtalk.open.app.api.callback;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class OpenDingTalkStreamTopics {
    /**
     * 机器人回调topic
     */
    public static final String BOT_MESSAGE_TOPIC = "/v1.0/im/bot/messages/get";
    /**
     * 卡片回调topic
     */
    public static final String CARD_CALLBACK_TOPIC = "/v1.0/card/instances/callback";
    /**
     * 卡片数据源topic
     */
    public static final String CARD_DYNAMIC_TOPIC = "/v1.0/card/dynamicData/get";
    /**
     * graph接口回调
     */
    public static final String GRAPH_API_TOPIC = "/v1.0/graph/api/invoke";
}
