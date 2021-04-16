package org.feng.question.stream;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VStream {

    @ToString
    @Setter
    @Getter
    static class User{
        private int id;

        public User(int i) {
            this.id = i;
        }
    }

    private static Stream<User> getStreamData(){
        return Stream.of(new User(1), new User(2), new User(3), new User(4));
    }

    public static void main(String[] args) {

        List<Integer> ids = getStreamData()
                .map(User::getId).collect(Collectors.toList());
        System.out.println(ids);

        List<Integer> filterIDS = getStreamData()
                .filter(user -> user.getId() < 4)
                .map(User::getId).collect(Collectors.toList());
        System.out.println(filterIDS);

    }

}
