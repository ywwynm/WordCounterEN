package decoder;

import java.io.*;

/**
 * Created by 张启 on 2015/11/27.
 * A class used to get text content from a file, ignoring its type.
 */
public class NormalDecoder {

    private NormalDecoder() {
        // avoid instantiating this class.
    }

    /**
     * Decode text from a file.
     * @param file file from which text is decoded.
     * @param encoding the encoding of {@param file}.
     * @return text content of {@param file}
     */
    public static String decode(File file, String encoding) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), encoding);
            bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result.toString();
    }
}
