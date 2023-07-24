package xyz.proteanbear.capricorn.infrastructure.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>基础设施：文件名处理工具类</p>
 *
 * @author 马强
 */
public class FileNameUtil {
    /**
     * 通过上传的文件资源自动生成文件名称
     *
     * @param file 上传资源
     * @return 文件名称
     */
    public static String generate(MultipartFile file) {
        if (file == null) return null;
        if (file.isEmpty()) return null;
        return EncryptUtil.encryptByMd5(
                System.currentTimeMillis() + file.getOriginalFilename() + file.getContentType()
        ) + "." + suffix(file.getOriginalFilename());
    }

    /**
     * <p>获取文件名中的文件类型后缀</p>
     *
     * @param fileName 文件全名
     * @return 后缀（全小写）
     */
    public static String suffix(final String fileName) {
        if (fileName == null) return null;
        final int extensionPos = fileName.lastIndexOf(".");
        final int lastSeparator = Math.max(fileName.lastIndexOf("/"), fileName.lastIndexOf("\\"));
        return (lastSeparator > extensionPos) ? "" : fileName.substring(extensionPos + 1);
    }
}
