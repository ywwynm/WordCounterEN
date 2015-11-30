package decoder;

import utils.FileUtils;

import java.io.*;

/**
 * Created by 张启 on 2015/11/27.
 * Factory built to decode text content from kinds of files.
 */
public class StringDecoderFactory {

    private StringDecoderFactory() {
        // avoid instantiating this class.
    }

    // supported special types.
    static String DOC = ".doc";

    public static String decodeFromFile(File file) throws IOException {
        if (!FileUtils.isFileValid(file)) {
            throw new FileNotFoundException();
        }
        return decodeFromFile(file, FileUtils.getFilePostfix(file));
    }

    private static String decodeFromFile(File file, String postfix)
            throws IOException {
        String encoding = FileUtils.getFileEncoding(file);
        if (encoding == null) {
            throw new IOException();
        }

        if (DOC.equals(postfix)) {
            return null;
        } else { // general case
            return NormalDecoder.decode(file, encoding);
        }
    }
}
