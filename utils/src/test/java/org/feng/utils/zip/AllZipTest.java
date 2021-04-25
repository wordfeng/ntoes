package org.feng.utils.zip;

import org.feng.utils.SpringbootTest;
import org.feng.utils.zip.bzip2.Bzip2;
import org.feng.utils.zip.deflate.Deflate;
import org.feng.utils.zip.lz.LZ4;
import org.feng.utils.zip.lz.LZO;
import org.feng.utils.zip.snappy.Snappys;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class AllZipTest extends SpringbootTest {

    @Test
    void test() throws Exception {
        //耗时： 2000次平均耗时
        // 对比只能参考，比如Deflate使用压缩率最高的级别
        test1(Deflate.class);    // 压缩率：99.9%  平均压缩耗时: 3.8885 ms 平均解压耗时: 2.1005 ms
        test1(Bzip2.class);      // 压缩率：99.9%  平均压缩耗时: 3.7835 ms 平均解压耗时: 1.9455 ms
        test1(LZ4.class);        // 压缩率：98.1%  平均压缩耗时: 0.5335 ms 平均解压耗时: 0.996 ms
        test1(LZO.class);        // 压缩率：99.5%  平均压缩耗时: 3.98 ms   平均解压耗时: 1.2005 ms
        test1(Snappys.class);// 压缩率：95%    平均压缩耗时: 0.2935 ms 平均解压耗时: 0.5475 ms
    }

    void test1(Class cls) throws Exception {
        System.out.println("\n算法：" + cls.getName());
        Method compress = cls.getDeclaredMethod("compress", byte[].class);
        Method decompress = cls.getDeclaredMethod("decompress", byte[].class);

        // 数据在Mac下使用dd生成：dd if=/dev/zero of=test.mp4 bs=1024 count=1024
        String filepath = ResourceUtils.getURL("classpath:data/test.mp4").toString().replace("file:", "");

        FileInputStream fis = new FileInputStream(filepath);
        FileChannel channel = fis.getChannel();
        ByteBuffer bb = ByteBuffer.allocate((int) channel.size());
        channel.read(bb);
        byte[] beforeBytes = bb.array();

        int times = 2000;
        System.out.println("压缩前大小：" + beforeBytes.length + " bytes");
        long startTime1 = System.currentTimeMillis();
        byte[] afterBytes = null;
        for (int i = 0; i < times; i++) {
            afterBytes = (byte[]) compress.invoke(null, beforeBytes);
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("压缩后大小：" + afterBytes.length + " bytes");
        System.out.println("压缩次数：" + times + "，时间：" + (endTime1 - startTime1)
                + "ms");
        BigDecimal after = new BigDecimal(afterBytes.length);
        BigDecimal before = new BigDecimal(beforeBytes.length);

        System.out.println(String.format("压缩率: %s",
                before.subtract(after).divide(before)
                        .round(new MathContext(4))));

        byte[] resultBytes = null;
        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            resultBytes = (byte[]) decompress.invoke(null, afterBytes);
        }
        System.out.println("解压缩后大小：" + resultBytes.length + " bytes");
        long endTime2 = System.currentTimeMillis();
        System.out.println("解压缩次数：" + times + "，时间：" + (endTime2 - startTime2)
                + "ms");
    }

}
