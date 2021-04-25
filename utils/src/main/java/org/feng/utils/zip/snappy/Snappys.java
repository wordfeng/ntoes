package org.feng.utils.zip.snappy;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * 最佳选择
 * 非常高的速度和合理的压缩率
 * https://zh.wikipedia.org/wiki/Snappy
 */
public class Snappys {

    public static byte[] compress(byte[] srcBytes) throws IOException {
        return Snappy.compress(srcBytes);
    }

    public static byte[] decompress(byte[] bytes) throws IOException {
        return Snappy.uncompress(bytes);
    }
}
