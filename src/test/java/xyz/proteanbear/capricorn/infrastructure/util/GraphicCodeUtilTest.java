package xyz.proteanbear.capricorn.infrastructure.util;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 单元测试：条形码生成工具
 */
public class GraphicCodeUtilTest {
    private static final String rootPath = "/Users/maqiang/Downloads/";

    /**
     * 生成条形码（无码号）
     */
    @Test
    public void testBarcodeNoNumber() throws WriterException, IOException {
        File outImageFile = new File(rootPath + "barcode_no_number.png");
        if (!outImageFile.exists()) outImageFile.createNewFile();
        ImageIO.write(GraphicCodeUtil.barcode("567893457234"),
                "png", outImageFile
        );
    }

    /**
     * 生成条形码（带码号）
     */
    @Test
    public void testBarcode() throws WriterException, IOException {
        File outImageFile = new File(rootPath + "barcode.png");
        if (!outImageFile.exists()) outImageFile.createNewFile();
        GraphicCodeUtil.Configuration configuration = GraphicCodeUtil.Configuration.barcodeDefaultConf()
                .setDisplayCode(true);
        ImageIO.write(
                GraphicCodeUtil.generate("567893457234", configuration),
                "png", outImageFile
        );
    }

    /**
     * 生成二维码（无标题）
     */
    @Test
    public void testQrcodeNothing() throws WriterException, IOException {
        File outImageFile = new File(rootPath + "qrcode_nothing.png");
        if (!outImageFile.exists()) outImageFile.createNewFile();
        ImageIO.write(GraphicCodeUtil.qrcode("567893457234test"),
                "png", outImageFile
        );
    }
}
