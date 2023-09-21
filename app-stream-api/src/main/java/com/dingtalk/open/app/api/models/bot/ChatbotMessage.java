package com.dingtalk.open.app.api.models.bot;

import java.util.List;

public class ChatbotMessage {
    String conversationId;
    List<MentionUser> atUsers;
    String chatbotCorpId;
    String chatbotUserId;
    String msgId;
    String senderNick;
    Boolean isAdmin;
    String senderStaffId;
    Long sessionWebhookExpiredTime;
    Long createAt;
    String senderCorpId;
    String conversationType;
    String senderId;
    String conversationTitle;
    Boolean isInAtList;
    String sessionWebhook;
    String msgtype;
    MessageContent text;
    MessageContent content;
}
