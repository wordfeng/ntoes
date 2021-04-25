package org.feng.utils.zip.deflate;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 同时使用了LZ77算法(重复的序列,滑动窗口)与哈夫曼编码的一个无损数据压缩算法，
 * https://zh.wikipedia.org/wiki/DEFLATE
 */
public class Deflate {

    public static byte[] compress(byte[] input) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        /*
            可以指定算法的压缩级别，这样你可以在压缩时间和输出文件大小上进行平衡。
            可选的级别有0（不压缩），以及1(快速压缩)到9（慢速压缩）
         */
        Deflater compressor = new Deflater(9);
        try {
            compressor.setInput(input);
            compressor.finish();
            final byte[] buf = new byte[2048];
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            compressor.end();
        }
        return bos.toByteArray();
    }

    public static byte[] decompress(byte[] input) throws DataFormatException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(input);
            final byte[] buf = new byte[2048];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            decompressor.end();
        }
        return bos.toByteArray();
    }
}
