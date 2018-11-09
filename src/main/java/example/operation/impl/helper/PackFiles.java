package example.operation.impl.helper;

import example.tool.common.Common;
import example.tool.config.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/11/9.
 */
public class PackFiles {

    private static final Logger logger = LoggerFactory.getLogger(PackFiles.class);
    private static String basePath = GlobalConfig.getProperties(Common.REPORT_RESOURCE_BASEPATH);

    /**
     * 把多个文件打包成zip压缩包整体操作
     */
    public void packFiles(String zipFileName, List<String> fileNames) {
        try {
            //文件输出打包成zip的流初始化
            FileOutputStream fos = new FileOutputStream(basePath + zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            //添加每个文件到zip中
            for (String fileName : fileNames) {
                addToZipFile(fileName, zos);
            }
            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            logger.debug("packFiles files not found", e);
        } catch (IOException e) {
            logger.debug("packFiles io error", e);
        }
    }


    /**
     * 单独每个文件打包成zip操作
     *
     * @param fileName
     * @param zos
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
        File file = new File(basePath + fileName); //文件的全路径名
        FileInputStream fis = new FileInputStream(file); //打包到zip的文件输入流
        ZipEntry zipEntry = new ZipEntry(fileName); //zip包中的文件名称
        zos.putNextEntry(zipEntry);

        //数据打包
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}
