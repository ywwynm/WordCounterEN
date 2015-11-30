package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by 张启 on 2015/11/28.
 * Utils for file.
 */
public class FileUtils {

    private FileUtils() {
        // avoid instantiating this class.
    }

    public static boolean isFileValid(File file) {
        return file != null && file.exists();
    }

    /**
     * Get postfix of a file including point, which is used to judge its type.
     * @param file file of which to get postfix.
     * @return the postfix of {@param file}.
     */
    public static String getFilePostfix(File file) {
        String pathName = file.getAbsolutePath();
        int lastPoint = pathName.lastIndexOf(".");
        if (lastPoint == -1) {
            return "";
        } else {
            return pathName.substring(lastPoint, pathName.length());
        }
    }

    /**
     * Get file's name excluding its postfix.
     * @param file file of which to get name.
     * @return name of {@param file}.
     */
    public static String getFileName(File file) {
        String name = file.getName();
        int lastPoint = name.lastIndexOf(".");
        if (lastPoint == -1) {
            return "";
        } else {
            return name.substring(0, lastPoint);
        }
    }

    /**
     * Try to get the encoding of file. Not guaranteed to return correct result.
     * @param file file of which to get encoding.
     * @return encoding of {@param file}.
     */
    public static String getFileEncoding(File file) {
        if (!isFileValid(file)) {
            return null;
        }

        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int p = (in.read() << 8) + in.read();
            if (p == 0xefbb) {
                return "UTF-8";
            } else if (p == 0xfffe) {
                return "Unicode";
            } else if (p == 0xfeff) {
                return "UTF-16BE";
            } else {
                return "GBK";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
