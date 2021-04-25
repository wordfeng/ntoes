package org.feng.utils.zip.deflate;

import com.google.common.io.ByteStreams;
import org.feng.utils.SpringbootTest;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

import static org.feng.utils.zip.deflate.Deflate.compress;
import static org.feng.utils.zip.deflate.Deflate.decompress;

public class DefLateTest extends SpringbootTest {

    @Test
    void test2() throws DataFormatException, IOException {
        URL file = ResourceUtils.getURL("classpath:images/1.jpg");
        Path path = Paths.get(file.toString().replace("file:", ""));

        byte[] data = ByteStreams.toByteArray(Files.newInputStream(path));

        System.out.println(String.format("data.length: %s", data.length));

        System.out.println(String.format("compress.length: %s", data.length));
        byte[] compress = compress(data);
        Path compressedPath = Paths.get(path.getParent().toString() + "/compress.jpg");
        output(compress, compressedPath);
        byte[] decompress = decompress(compress);
        Path decompressedPath = Paths.get(path.getParent().toString() + "/decompress.jpg");
        output(decompress, decompressedPath);

        System.out.println(String.format("old size: %s, \ncompressedPath Size: %s \ndecompressedPath Size: %s",
                Files.size(path),Files.size(compressedPath),Files.size(decompressedPath)));
        System.out.println(String.format("decompress.length: %s", decompress.length));

        /*
         old size: 208181,
         compressedPath Size: 208068
         decompressedPath Size: 208181
         */
    }

    static void output(byte[] data, Path path) throws IOException {
        System.out.println("outpath: "+ path.toString());
        OutputStream os = Files.newOutputStream(path);
        os.write(data, 0, data.length);
        os.flush();
        os.close();
    }
}
