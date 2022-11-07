package xyz.proteanbear.capricorn.infrastructure.util;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 单元测试：条形码生成工具
 */
public class BarCodeUtilTest {
    private static final String rootPath = "/Users/maqiang/Downloads/";

    /**
     * 生成条形码（无码号）
     */
    @Test
    public void testGenerateNoNumber() throws WriterException, IOException {
        File outImageFile = new File(rootPath + "barcode_no_number.png");
        if (!outImageFile.exists()) outImageFile.createNewFile();
        ImageIO.write(BarCodeUtil.generate("567893457234"),
                "png", outImageFile
        );
    }

    /**
     * 生成条形码（带码号）
     */
    @Test
    public void testGenerate() throws WriterException, IOException {
        File outImageFile = new File(rootPath + "barcode.png");
        if (!outImageFile.exists()) outImageFile.createNewFile();
        BarCodeUtil.Configuration configuration = new BarCodeUtil.Configuration()
                .setDisplayCode(true);
        ImageIO.write(
                BarCodeUtil.generate("567893457234", configuration),
                "png", outImageFile
        );
    }
}
