package com.apestech.configuration;

import com.hazelcast.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * 功能：Hazelcast 配置
 *
 * @author xul
 * @create 2017-12-11 13:40
 */
@Configuration
public class HazelcastConfiguration {
    @Autowired
    private Environment env;

    @Bean
    public Config hazelCastConfig() {
        Config config = new Config().setInstanceName("hazelcast-instance");

        String value = env.getProperty("spring.hazelcast.networking");
        Assert.notNull(value, "属性值： spring.hazelcast.networking 没有配置。");
        if (value.equals("tcp_ip")) {
            value = env.getProperty("spring.hazelcast.tcp_ip.members");
            Assert.notNull(value, "属性值： spring.hazelcast.tcp_ip.members 没有配置。");
            String[] members = value.split(",");
            Assert.isTrue(members.length >= 1, "属性值： spring.hazelcast.tcp_ip.members 没有配置。");
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true).setMembers(Arrays.asList(members));

            config.getManagementCenterConfig().setEnabled(true).setUrl("http://10.2.4.196:8080/mancenter");

        } else if (value.equals("multicast")) {
            value = env.getProperty("spring.hazelcast.multicast.port");
            int port = value != null ? Integer.valueOf(value.trim()) : 54327;
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true).setMulticastPort(port);
        } else {
            throw new RuntimeException("组网类型配置错误。");
        }

        return config;
    }

}
