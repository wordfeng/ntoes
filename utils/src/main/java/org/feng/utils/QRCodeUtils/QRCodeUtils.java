package org.feng.utils.QRCodeUtils;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.common.primitives.Bytes;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeUtils {

    public static byte[] writeTo(String content) throws IOException {
        QrConfig config = new QrConfig();
        config.setMargin(3);
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        config.setForeColor(new Color(0, 60, 130));
        config.setBackColor(new Color(250, 250, 250));
        BufferedImage bufferedImage = QrCodeUtil.generate(content, config);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", baos);
        return baos.toByteArray();
    }

    public static byte[] writeToPNGBase64(String content) throws IOException {
        byte[] prefix = "data:image/png;base64,".getBytes();
        return Bytes.concat(prefix, Base64Utils.encode(writeTo(content)));
    }

    public static void writeTo(HttpServletResponse response, String content) {
        QrConfig config = new QrConfig();
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:static/qrcode/qr.jpg");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        //附带logo
//        config.setImg(file);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 高纠错级别
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(new Color(0, 60, 130).getRGB());
        // 设置背景色（灰色）
        config.setBackColor(new Color(242, 242, 242).getRGB());
       /* try {
            OutputStream  out = new FileOutputStream("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        BufferedImage bufferedImage = QrCodeUtil.generate(content, config);
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "PNG", os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
