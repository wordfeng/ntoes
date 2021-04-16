package org.feng.utils.images;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@ContextConfiguration
@SpringBootConfiguration
@SpringBootTest
public class ThumbnailTest {

    @Test
    void test() throws IOException, InterruptedException {

        URL file =
                ResourceUtils.getURL("classpath:images/1.jpg");
        String filename = file.toString().replace("file:", "");
        int quality = (int) (new FileInputStream(filename).available() * 0.75);
        long size = Files.readAttributes(Paths.get(filename), BasicFileAttributes.class).size();
        log.info(String.format("quality: %s, size: %s", quality, size));
        Thumbnail.createThumbnail(filename,
                100, 100, quality,
                filename.replace("1.jpg", "1_thumbnail.jpg"));
    }
}
