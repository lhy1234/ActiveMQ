package com.nb.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author lihaoyang
 * @date 2020/12/30
 */
@Service
public class UserService {



    public void send() throws InterruptedException {
        CompletableFuture.runAsync(()->{
            send22();
        });
    }

    private void send22(){

        System.err.println("UserService进入send1111");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("UserService进入send2222");
    }
}
