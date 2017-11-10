package org.tbm.server.support;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jason.Xia on 17/11/8.
 */
public class DingTalkWebHook {
    private static final Logger logger = LoggerFactory.getLogger(DingTalkWebHook.class);
    private static DingTalkWebHook instance;

    private ScheduledExecutorService sendExecutor;

    private String webHookUrl;

    private HttpClient httpclient = HttpClients.createDefault();

    private List<DingMsg> msgQueue = new ArrayList<>();

    private DingTalkWebHook(String webHookUrl) {
        this.webHookUrl = webHookUrl;
        sendExecutor = new ScheduledThreadPoolExecutor(1);
        sendExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        }, 15L, 15L, TimeUnit.SECONDS);
    }

    public static void init(String webHookUrl) {
        if (null != instance) {
            return;
        }

        if (Utils.isEmpty(webHookUrl)) {
            throw new IllegalArgumentException("WebHook Url Cannot be Null");
        }

        instance = new DingTalkWebHook(webHookUrl);
    }

    public static DingTalkWebHook getInstance() {
        return instance;
    }

    public void send(DingMsg msg) {
        msgQueue.add(msg);
    }

    public void sendNow(DingMsg msg) {
        try {
            MarkdownMessage message = createMsg(msg.getTopic(), Utils.singleObjectConvertToList(msg));
            if (null == message) {
                return;
            }

            SendResult result = post(webHookUrl, message);
            if (!result.isSuccess()) {
                logger.error("{} WebHook Failed:{}", webHookUrl, result.toString());
            }
        } catch (Exception e) {
            logger.error("DingTalk Invoke WebHook Failed", e);
        }
    }

    private void flush() {
        try {
            MarkdownMessage message = createMsg("TBM-Monitor", msgQueue);
            if (null != message) {
                SendResult post = post(webHookUrl, message);
                if (!post.isSuccess()) {
                    logger.error("{} WebHook Failed:{}", webHookUrl, post.toString());
                }
            }
        } catch (Exception e) {
            logger.error("DingTalk Invoke WebHook Failed", e);
        }
    }

    private SendResult post(String webhook, MarkdownMessage message) throws IOException {
        HttpPost httppost = new HttpPost(webhook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(message.toJsonString(), "utf-8");
        httppost.setEntity(se);

        SendResult sendResult = new SendResult();
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity());
            JSONObject obj = JSONObject.parseObject(result);

            Integer errcode = obj.getInteger("errcode");
            sendResult.setErrorCode(errcode);
            sendResult.setErrorMsg(obj.getString("errmsg"));
            sendResult.setIsSuccess(errcode.equals(0));
        }

        return sendResult;
    }

    private MarkdownMessage createMsg(String title, List<DingMsg> msgs) {
        if (Utils.isEmpty(msgs)) {
            return null;
        }

        MarkdownMessage message = new MarkdownMessage();
        message.setTitle(title);
        for (DingMsg item : msgs) {
            message.add(MarkdownMessage.getBoldText(item.getTopic()));
            message.add("\n\n");
            message.add(item.getContent());
            message.add("\n\n");
            Exception e = item.getE();
            message.add(MarkdownMessage.getReferenceText(e.getClass() + ":" + e.getMessage()));
            message.add("\n\n");
        }

        return message;
    }

    public ScheduledExecutorService getSendExecutor() {
        return sendExecutor;
    }
}
