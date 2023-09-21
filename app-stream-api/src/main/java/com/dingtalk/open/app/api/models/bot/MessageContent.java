package com.dingtalk.open.app.api.models.bot;

import java.io.Serializable;
import java.util.List;

public class MessageContent implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 适用的类型：text
     */
    String content;
    /**
     * 适用的类型：picture, richText[index]
     */
    String pictureDownloadCode;
    /**
     * 适用的类型：picture, video, audio, file, richText[index]
     */
    String downloadCode;
    /**
     * 适用的类型：video
     */
    String duration;
    /**
     * 适用的类型：video
     */
    String videoType;
    /**
     * 适用的类型：audio
     */
    String recognition;
    /**
     * 适用的类型：file
     */
    String spaceId;
    /**
     * 适用的类型：file
     */
    String fileName;
    /**
     * 适用的类型：file
     */
    String fileId;
    /**
     * 适用的类型：richText
     */
    List<MessageContent> richText;
    /**
     * 适用的类型：richText[index]
     */
    String text;
    /**
     * 适用的类型：richText[index]
     */
    String type;
    /**
     * 适用的类型：unknownMsgType
     */
    String unknownMsgType;
}
