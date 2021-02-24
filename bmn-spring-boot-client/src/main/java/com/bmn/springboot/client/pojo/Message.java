package com.bmn.springboot.client.pojo;

/**
 * @date 2020-10-26
 * @author zhangyuqiang02@playcrab.com
 */
public class Message {
    private long messageId;
    private String content;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
