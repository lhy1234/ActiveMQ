package com.nb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NbActivemqApplicationTests {

    @Test
    void contextLoads() {

        print(-1);

    }

    public static void print(int num){
        for(int i=31;i>=0;i--){
            System.out.print(((num & 1 << i) & num) == 0 ? "0":"1");
        }
    }

}
