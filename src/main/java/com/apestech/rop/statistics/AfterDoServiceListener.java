package com.apestech.rop.statistics;

import com.alibaba.fastjson.JSON;
import com.apestech.framework.event.Listener;
import com.apestech.framework.util.NetUtil;
import com.apestech.oap.event.AfterDoServiceEvent;

import com.apestech.oap.event.RopEventListener;
import com.apestech.oap.session.Session;
import com.apestech.rbac.domain.Post;
import com.apestech.rop.session.SimpleSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 功能：
 *
 * @author xul
 * @create 2017-12-18 18:51
 */
@Listener
public class AfterDoServiceListener implements RopEventListener<AfterDoServiceEvent> {

    @Autowired
    StatisticsRepository repository;

    /**
     * 响应事件
     *
     * @param ropEvent
     */
    @Override
    public void onRopEvent(AfterDoServiceEvent ropEvent) {
        Statistics statistics = new Statistics();
        statistics.setServer(NetUtil.getHostName());
        statistics.setMethod(ropEvent.getRopRequestContext().getMethod());
        statistics.setVersion(ropEvent.getRopRequestContext().getVersion());
        statistics.setAppKey(ropEvent.getRopRequestContext().getAppKey());
        statistics.setSessionId(ropEvent.getRopRequestContext().getSessionId());
        statistics.setIp(ropEvent.getRopRequestContext().getIp());
        statistics.setBeginTime(timeStampToString(ropEvent.getServiceBeginTime()));
        statistics.setEndTime(timeStampToString(ropEvent.getServiceEndTime()));
        statistics.setTime(ropEvent.getServiceEndTime() - ropEvent.getServiceBeginTime());
        statistics.setRequest(JSON.toJSONString(ropEvent.getRopRequestContext().getAllParams()));
        statistics.setResponse(JSON.toJSONString(ropEvent.getRopRequestContext().getRopResponse()));
        Session session = ropEvent.getRopRequestContext().getSession();
        if (session != null) {
            statistics.setUserId(((SimpleSession) session).getUserId());
            Post post = ((SimpleSession) session).getPost();
            if (post != null) {
                statistics.setPostId(String.valueOf(post.getId()));
            }
        }
        repository.save(statistics);
    }

    private String timeStampToString(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return String.valueOf(sdf.format(new Date(timeStamp)));
    }

    /**
     * 执行的顺序号
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 100;
    }
}
