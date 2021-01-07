package com.nb;

import com.nb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAsync
@SpringBootApplication
public class NbActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbActivemqApplication.class, args);

        System.err.println("xxoo");
    }

    @Autowired
    UserService userService;

    @GetMapping("send")
    public String send() throws Exception{
        System.err.println("j进入control--------");
        long start = System.currentTimeMillis();
        userService.send();
        long end = System.currentTimeMillis();
        System.err.println("耗时--------"+(end-start));
        return "OKOK";
    }



}
