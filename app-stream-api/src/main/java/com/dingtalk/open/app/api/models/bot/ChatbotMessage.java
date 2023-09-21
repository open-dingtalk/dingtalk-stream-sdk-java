package com.dingtalk.open.app.api.models.bot;

import java.io.Serializable;
import java.util.List;

public class ChatbotMessage implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<MentionUser> getAtUsers() {
        return atUsers;
    }

    public void setAtUsers(List<MentionUser> atUsers) {
        this.atUsers = atUsers;
    }

    public String getChatbotCorpId() {
        return chatbotCorpId;
    }

    public void setChatbotCorpId(String chatbotCorpId) {
        this.chatbotCorpId = chatbotCorpId;
    }

    public String getChatbotUserId() {
        return chatbotUserId;
    }

    public void setChatbotUserId(String chatbotUserId) {
        this.chatbotUserId = chatbotUserId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getSenderStaffId() {
        return senderStaffId;
    }

    public void setSenderStaffId(String senderStaffId) {
        this.senderStaffId = senderStaffId;
    }

    public Long getSessionWebhookExpiredTime() {
        return sessionWebhookExpiredTime;
    }

    public void setSessionWebhookExpiredTime(Long sessionWebhookExpiredTime) {
        this.sessionWebhookExpiredTime = sessionWebhookExpiredTime;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public String getSenderCorpId() {
        return senderCorpId;
    }

    public void setSenderCorpId(String senderCorpId) {
        this.senderCorpId = senderCorpId;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    public Boolean getInAtList() {
        return isInAtList;
    }

    public void setInAtList(Boolean inAtList) {
        isInAtList = inAtList;
    }

    public String getSessionWebhook() {
        return sessionWebhook;
    }

    public void setSessionWebhook(String sessionWebhook) {
        this.sessionWebhook = sessionWebhook;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public MessageContent getText() {
        return text;
    }

    public void setText(MessageContent text) {
        this.text = text;
    }

    public MessageContent getContent() {
        return content;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }
}
