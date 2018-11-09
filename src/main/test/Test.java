import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/11/9.
 */
public class Test {

    @org.junit.Test
    public void packFiles(){
        try {
            FileOutputStream fos = new FileOutputStream("G:/temp/files.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            String file1Name = "G:/temp/file1.mp3";
            String file2Name = "G:/temp/file2.mp3";
            String file3Name = "G:/temp/file3.png";
            String file4Name = "G:/temp/file4.png";

            addToZipFile(file1Name, zos, "file1.mp3");
            addToZipFile(file2Name, zos, "file2.mp3");
            addToZipFile(file3Name, zos, "file3.png");
            addToZipFile(file4Name, zos, "file4.png");

            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 把文件打包成zip
     * @param fileName
     * @param zos
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void addToZipFile(String fileName, ZipOutputStream zos, String fileNameOut) throws FileNotFoundException, IOException {
        System.out.println("Writing '" + fileName + "' to zip file");
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileNameOut);
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
