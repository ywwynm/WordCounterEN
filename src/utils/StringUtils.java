package utils;

/**
 * Created by 张启 on 2015/11/27.
 * utils for String
 */
public class StringUtils {

    private StringUtils() {
        // avoid instantiating this class.
    }

    public static String charactersOf(String src) {
        return src.replaceAll("\\s+|\t|\r|\n", "");
    }

    public static String[] wordsOf(String src) {
        return src.split("\\pP|\\pS|\\s+|\t|\r|\n");
    }

}
