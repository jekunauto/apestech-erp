package com.apestech.rop.statistics;

import com.apestech.framework.util.DateUtil;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * 功能：
 *
 * @author xul
 * @create 2017-12-18 19:05
 */
@Document(collection = "Statistics")
@Data
public class Statistics {

    @Id  // 主键
    private String id; //消息编号
    private String server;
    private String method;
    private String version;
    private String appKey;
    private String sessionId;
    private String userId;
    private String postId;
    private String ip;
    private String beginTime;
    private String endTime;
    private long time; //耗时(毫秒)
    private String request; //请求数据
    private String response; //响应数据
    private String created = DateUtil.formatTime();  //创建时间
}
