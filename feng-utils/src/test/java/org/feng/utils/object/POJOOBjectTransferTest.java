package org.feng.utils.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.Serializable;

@SpringBootTest
public class POJOOBjectTransferTest {


    @Test
    void deep() throws IllegalAccessException, IOException, ClassNotFoundException, InstantiationException {
        POJOObjectTransfer<HumanDTO, Human> transfer = new POJOObjectTransfer<>(Human.class);

        Human ch = new Human("children", null, 30);
        HumanDTO humanDTO = new HumanDTO("root", ch);

        //deep
        Human human = transfer.deep(humanDTO);
        humanDTO.setName("newName");
        ch.setName("newChildren");
        human.setAge(1);
        System.out.println(human);
    }

    @Test
    void shallow() throws Exception {
        POJOObjectTransfer<HumanDTO, Human> transfer = new POJOObjectTransfer<>(Human.class);

        Human ch = new Human("children", null, 30);
        HumanDTO humanDTO = new HumanDTO("root", ch);

        //shallow
        Human human = transfer.shallow(humanDTO);
        humanDTO.setName("newName");
        ch.setName("newChildren");
        human.setAge(2);
        System.out.println(human);
    }



    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class HumanDTO implements Serializable {
        private final long serialVersionUID = 1L;
        String name;
        Human children;

        @Override
        public String toString() {
            return "HumanDTO{" +
                    "serialVersionUID=" + serialVersionUID +
                    ", name='" + name + '\'' +
                    ", children=" + children +
                    '}';
        }
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class Human implements Serializable {
        private final long serialVersionUID = 2L;
        String name;
        Human children;
        Integer age;

        @Override
        public String toString() {
            return "Human{" +
                    "serialVersionUID=" + serialVersionUID +
                    ", name='" + name + '\'' +
                    ", children=" + children +
                    ", age=" + age +
                    '}';
        }
    }
}
