package com.apestech.rop.session;

import com.apestech.framework.util.Tools;
import com.apestech.oap.Constants;
import com.apestech.oap.session.Session;
import com.apestech.rbac.domain.Post;
import com.apestech.rbac.domain.User;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleSession implements Session {

    private final Map<String, Object> attributeCache = new ConcurrentHashMap();

    private String sessionId; //session Id
    private String ip; //用户
    private String userId; //用户Id
    private User user; //用户
    private Post post; //岗位

    public Map<String, Object> getAttributeCache() {
        return attributeCache;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        attributeCache.put(Constants.SESSION_CHANGED, true);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
        attributeCache.put(Constants.SESSION_CHANGED, true);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        attributeCache.put(Constants.SESSION_CHANGED, true);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        attributeCache.put(Constants.SESSION_CHANGED, true);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
        attributeCache.put(Constants.SESSION_CHANGED, true);
    }

    /**
     * 设置属性
     *
     * @param name
     * @param obj
     */
    @Override
    public void setAttribute(String name, Object obj) {
        attributeCache.put(name, obj);
        attributeCache.put(Constants.SESSION_CHANGED, true);
    }

    /**
     * 获取属性
     *
     * @param name
     * @param clazz
     * @return
     */
    @Override
    public <T> T getAttribute(String name, Class<T> clazz) {
        Object value = attributeCache.get(name);
        if (value != null && clazz != null && !clazz.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + clazz.getName() + "]: " + value);
        } else {
            return (T) value;
        }
    }

    /**
     * 获取所有的属性
     *
     * @return
     */
    @Override
    public Map<String, Object> getAllAttributes() {
        return attributeCache;
    }

    /**
     * 删除属性条目
     *
     * @param name
     */
    @Override
    public void removeAttribute(String name) {
        attributeCache.remove(name);
        if (!name.equals(Constants.SESSION_CHANGED)) {
            attributeCache.put(Constants.SESSION_CHANGED, true);
        }
    }

    /**
     * ROP使用外部缓存管理器保存{@link Session}的内容，在每次接收到请求时，从外部缓存服务器复制会话并反序列化出 会话对象
     * {@link Session}。在处理请求时，业务逻辑允许更改{@link Session}中的内容，所以需要在{@link Session}
     * 内容发生变化后，将 {@link Session}重写到外部缓存中。
     * <p>
     * 如果每次访问会话后，ROP都重写会话，将比较影响性能，往往大部分的请求并不会更改会话的内容。 所以需要有一种机制在{@link Session}
     * 内容发生变更后告之ROP，以便ROP在{@link Session}内容发生变化时， 在请求结束时，将其同步到外部缓存服务器中。
     * <p>
     * ROP根据{@code isChanged}判断会话内容是否有变化，监测到会话内容的变化，它将自动执行会话重写操作，以便将本
     * 些更改同步到外部缓存服务器中。
     */
    @Override
    public boolean isChanged() {
        Object o = attributeCache.get(Constants.SESSION_CHANGED);
        return o == null ? false : true;
    }
}
