package xyz.proteanbear.capricorn.infrastructure.util;

import com.google.zxing.*;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.*;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * 工具类：图形码生成和识别工具
 */
public class GraphicCodeUtil {
    /**
     * 将指定的号码生成条形码（使用默认配置）
     *
     * @param number 号码内容
     * @return 图片缓存
     */
    public static BufferedImage barcode(String number) throws WriterException {
        return generate(number, Configuration.barcodeDefaultConf());
    }

    /**
     * 将指定的内容生成二维码（使用默认配置）
     *
     * @param content 二维码内容
     * @return 图片缓存
     */
    public static BufferedImage qrcode(String content) throws WriterException {
        return generate(content, Configuration.qrcodeDefaultConf());
    }

    /**
     * 将指定的号码生成条形码（指定配置）
     *
     * @param content       内容
     * @param configuration 配置项
     * @return 图片缓存
     */
    public static BufferedImage generate(String content, Configuration configuration) throws WriterException {
        //更加配置选择生成器
        Writer writer = null;
        BarcodeFormat format = configuration.getBarcodeFormat();
        switch (format) {
            case AZTEC -> writer = new AztecWriter();
            case CODABAR -> writer = new CodaBarWriter();
            case CODE_39 -> writer = new Code39Writer();
            case CODE_93 -> writer = new Code93Writer();
            case CODE_128 -> writer = new Code128Writer();
            case DATA_MATRIX -> writer = new DataMatrixWriter();
            case EAN_8 -> writer = new EAN8Writer();
            case EAN_13 -> writer = new EAN13Writer();
            case ITF -> writer = new ITFWriter();
            case PDF_417 -> writer = new PDF417Writer();
            case QR_CODE -> writer = new MultiFormatWriter();
            default -> throw new WriterException();
        }
        BitMatrix bitMatrix = writer.encode(
                content, format,
                configuration.getWidth(), configuration.getHeight(),
                new HashMap<>() {{
                    put(EncodeHintType.CHARACTER_SET, configuration.getCharacterSet());
                    put(EncodeHintType.ERROR_CORRECTION, configuration.getCorrectionLevel());
                    put(EncodeHintType.MARGIN, configuration.getMargin());
                }}
        );
        BufferedImage barcode = MatrixToImageWriter.toBufferedImage(bitMatrix);
        //不显示码号内容，直接返回
        if (!configuration.displayCode) return barcode;

        //如果显示码号，创建新的图片
        //高度为条形码高度+文字一行高度
        int imageHeight = configuration.getHeight() + configuration.getWordLineHeight() + configuration.getMargin() * 2;
        BufferedImage outImage = new BufferedImage(
                configuration.getWidth(),
                imageHeight,
                BufferedImage.TYPE_INT_BGR
        );
        //使用画布
        Graphics2D graphics = whiteGraphics(outImage, imageHeight, configuration);
        //绘制条形码
        graphics.drawImage(barcode, 0, configuration.getMargin(), barcode.getWidth(), barcode.getHeight(), null);
        //绘制号码内容
        drawText(graphics, content, configuration.getMargin() * 2 + barcode.getHeight() + 10, configuration);
        graphics.dispose();
        outImage.flush();
        return outImage;
    }

    /**
     * 设置为白色画布
     */
    public static Graphics2D whiteGraphics(BufferedImage outImage, int toHeight, Configuration configuration) {
        //使用画布
        Graphics2D graphics = outImage.createGraphics();
        //抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        //背景白色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0,
                configuration.getWidth() + configuration.getMargin() * 2,
                toHeight + configuration.getMargin() * 2
        );
        return graphics;
    }

    /**
     * 绘制位置到画板的指定位置（居中）
     */
    private static void drawText(Graphics2D graphics, String text, int startY, Configuration configuration) {
        //笔刷为黑色
        graphics.setColor(Color.BLACK);
        //字体、字形、字号
        graphics.setFont(configuration.getTextFont());
        //计算文字长度
        int textWidth = graphics.getFontMetrics().stringWidth(text);
        //设置到居中位置
        drawText(graphics, text,
                (configuration.getWidth() - textWidth) / 2,
                startY, configuration
        );
    }

    /**
     * 绘制位置到画板的指定位置
     */
    private static void drawText(Graphics2D graphics, String text, int startX, int startY, Configuration configuration) {
        //笔刷为黑色
        graphics.setColor(Color.BLACK);
        //字体、字形、字号
        graphics.setFont(configuration.getTextFont());
        //写位置
        graphics.drawString(text, startX, startY);
    }

    /**
     * 配置项，用于在生成条形码时配置相关参数
     */
    public static class Configuration {
        /**
         * 默认配置（条形码）
         */
        public static Configuration barcodeDefaultConf() {
            return new Configuration()
                    .setBarcodeFormat(BarcodeFormat.CODE_128)
                    .setDisplayCode(true)
                    ;
        }

        /**
         * 默认配置（二维码）
         */
        public static Configuration qrcodeDefaultConf() {
            return new Configuration()
                    .setWidth(500)
                    .setHeight(500)
                    .setBarcodeFormat(BarcodeFormat.QR_CODE)
                    .setMargin(2)
                    ;
        }

        /**
         * 指定长宽
         */
        public static Configuration of(int width, int height) {
            return new Configuration()
                    .setWidth(width)
                    .setHeight(height)
                    ;
        }

        /**
         * 条形码宽度
         */
        private int width = 300;

        /**
         * 条形码高度
         */
        private int height = 52;

        /**
         * 文字行高度
         */
        private int wordLineHeight = 16;

        /**
         * 文字编码
         */
        private String characterSet = "utf-8";

        /**
         * 容错级别
         */
        private ErrorCorrectionLevel correctionLevel = ErrorCorrectionLevel.H;

        /**
         * 边距
         */
        private int margin = 5;

        /**
         * 是否显示条形码码号
         */
        private boolean displayCode = false;

        /**
         * 条形码编码方式
         */
        private BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        /**
         * 文字字体
         */
        private Font textFont = new Font("宋体", Font.PLAIN, 16);

        public int getWidth() {
            return width;
        }

        public Configuration setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Configuration setHeight(int height) {
            this.height = height;
            return this;
        }

        public int getWordLineHeight() {
            return wordLineHeight;
        }

        public Configuration setWordLineHeight(int wordLineHeight) {
            this.wordLineHeight = wordLineHeight;
            return this;
        }

        public String getCharacterSet() {
            return characterSet;
        }

        public Configuration setCharacterSet(String characterSet) {
            this.characterSet = characterSet;
            return this;
        }

        public ErrorCorrectionLevel getCorrectionLevel() {
            return correctionLevel;
        }

        public Configuration setCorrectionLevel(ErrorCorrectionLevel correctionLevel) {
            this.correctionLevel = correctionLevel;
            return this;
        }

        public int getMargin() {
            return margin;
        }

        public Configuration setMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public boolean isDisplayCode() {
            return displayCode;
        }

        public Configuration setDisplayCode(boolean displayCode) {
            this.displayCode = displayCode;
            return this;
        }

        public BarcodeFormat getBarcodeFormat() {
            return barcodeFormat;
        }

        public Configuration setBarcodeFormat(BarcodeFormat barcodeFormat) {
            this.barcodeFormat = barcodeFormat;
            return this;
        }

        public Font getTextFont() {
            return textFont;
        }

        public Configuration setTextFont(Font textFont) {
            this.textFont = textFont;
            return this;
        }
    }
}