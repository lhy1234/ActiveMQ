package com.nb.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lihaoyang
 * @date 2021/1/31
 */
@Configuration
public class BeanConfig {

    @Value("${nbQueue}")
    private String nbQueue;

    @Bean("nbQueue")
    public ActiveMQQueue nbQueue(){
        return new ActiveMQQueue(nbQueue);
    }
}
