package org.tbm.server.support;

import org.tbm.common.Serialize;

/**
 * Created by Jason.Xia on 17/11/10.
 */
public class DingMsg extends Serialize {
    private String topic;
    private String content;
    private Exception e;

    public DingMsg() {
    }

    public DingMsg(String topic, String content, Exception e) {
        this.topic = topic;
        this.content = content;
        this.e = e;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }
}
