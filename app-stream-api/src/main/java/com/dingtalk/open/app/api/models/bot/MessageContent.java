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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictureDownloadCode() {
        return pictureDownloadCode;
    }

    public void setPictureDownloadCode(String pictureDownloadCode) {
        this.pictureDownloadCode = pictureDownloadCode;
    }

    public String getDownloadCode() {
        return downloadCode;
    }

    public void setDownloadCode(String downloadCode) {
        this.downloadCode = downloadCode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<MessageContent> getRichText() {
        return richText;
    }

    public void setRichText(List<MessageContent> richText) {
        this.richText = richText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnknownMsgType() {
        return unknownMsgType;
    }

    public void setUnknownMsgType(String unknownMsgType) {
        this.unknownMsgType = unknownMsgType;
    }
}
