package com.apestech.configuration;

import com.hazelcast.config.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能：Hazelcast 配置
 *
 * @author xul
 * @create 2017-12-11 13:40
 */
@Configuration
public class HazelcastConfiguration {

    @Value("${spring.hazelcast.multicast.port}")
    private int multicastPort;

    @Bean
    public Config hazelCastConfig() {

        
        Config config = new Config()
                .setInstanceName("hazelcast-instance")
                .setNetworkConfig(new NetworkConfig().setJoin(new JoinConfig().setMulticastConfig(new MulticastConfig().setMulticastPort(multicastPort))))
//                .addMapConfig(
//                        new MapConfig()
//                                .setName("instruments")
//                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
//                                .setEvictionPolicy(EvictionPolicy.LRU)
//                                .setTimeToLiveSeconds(20))
                ;
//        System.out.println("组播协议端口: " + config.getNetworkConfig().getJoin().getMulticastConfig().getMulticastPort());
        return config;
    }

}