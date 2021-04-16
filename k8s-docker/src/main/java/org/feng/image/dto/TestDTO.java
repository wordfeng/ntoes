package org.feng.image.dto;

import lombok.Data;

@Data
public class TestDTO {
    String id;

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                '}';
    }
}
