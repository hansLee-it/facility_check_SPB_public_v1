package hans.firefighting.check.facility_check.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomFileUtil {
    public static String imageToBase64(String filePath, String fileName, String fileSuffix) throws IOException {
        File file = new File(filePath + fileName + "." + fileSuffix);
        InputStream finput = new FileInputStream(file);
        byte[] imageBytes = new byte[(int) file.length()];
        finput.read(imageBytes, 0, imageBytes.length);
        finput.close();
        // Base64
        String imageStr = Base64.encodeBase64String(imageBytes);
        return imageStr;
    }
}