package org.feng.utils.object;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class ObjectToStringTest {

    @Test
    void objectTOString(){
        POJOOBjectTransferTest.Human ch = new POJOOBjectTransferTest.Human("ch", null, 30);
        log.info(ObjectToString.from(new POJOOBjectTransferTest.Human("root", ch, 30)));
    }

    @Test
    void aVoid(){
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            list.add(i);
        }
        log.info(ObjectToString.from(list));
    }


}
