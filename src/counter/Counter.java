package counter;

import utils.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by 张启 on 2015/11/27.
 * A toolkit class that counts every kinds of characters'(or words') recurrences in a
 * {@link String}.
 */
public class Counter {

    public static int COUNTER_MODE_CHARACTER = 0;
    public static int COUNTER_MODE_WORD      = 1;

    private String mStrToCount;

    public Counter(String str) {
        mStrToCount = str;
    }

    /**
     * Set string to count.
     * @param str string to count.
     */
    @SuppressWarnings("unused")
    public void setStrToCount(String str) {
        this.mStrToCount = str;
    }

    /**
     * Count recurrences of single characters or words in {@link #mStrToCount}.
     * @param mode judge what kind of content we want to count.
     *             Should be one of:
     *             {@link #COUNTER_MODE_CHARACTER},
     *             {@link #COUNTER_MODE_WORD}.
     * @param ignoreCase {@code true} if we should ignore case of each character
     *                    or word. {@code false} otherwise.
     * @return a {@link HashMap} that contains character(or word)-recurrence
     *          entries.
     */
    public HashMap count(int mode, boolean ignoreCase) {
        // mStrToCount isn't possible to be null during normal executing procedure.
        assert mStrToCount != null;

        if (mode == COUNTER_MODE_CHARACTER) {
            return countCharacters(ignoreCase);
        } else if (mode == COUNTER_MODE_WORD) {
            return countWords(ignoreCase);
        }
        return null;
    }

    private HashMap<Character, Integer> countCharacters(boolean ignoreCase) {
        String str = StringUtils.charactersOf(mStrToCount);
        if (ignoreCase) {
            str = str.toLowerCase();
        }

        // Count characters in different ways according to text's length.
        int len = str.length();
        if (len <= 256) {
            return countCharactersForShortString(str);
        } else {
            return countCharactersForLongString(str);
        }
    }

    private HashMap<Character, Integer> countCharactersForShortString(String str) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            Integer count = map.get(c);
            map.put(c, count == null ? 1 : count + 1);
        }
        return map;
    }

    private HashMap<Character, Integer> countCharactersForLongString(String str) {
        HashMap<Character, Integer> map = new HashMap<>();
        try {
            // Using reflection to achieve better performance/speed.
            final Field field = String.class.getDeclaredField("value");
            field.setAccessible(true);

            final char[] value = (char[]) field.get(str);
            final int len = value.length;
            for (int i = 0; i < len; i++) {
                Integer count = map.get(value[i]);
                map.put(value[i], count == null ? 1 : count + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private HashMap<String, Integer> countWords(boolean ignoreCase) {
        String[] words = StringUtils.wordsOf(mStrToCount);
        HashMap<String, Integer> map =
                ignoreCase ? countWordsIgnoreCase(words) : countWords(words);
        map.remove("");
        map.remove(" ");
        return map;
    }

    private HashMap<String, Integer> countWords(String[] words) {
        HashMap<String, Integer> map = new HashMap<>();
        final int len = words.length;
        for (int i = 0; i < len; i++) {
            Integer count = map.get(words[i]);
            map.put(words[i], count == null ? 1 : count + 1);
        }
        return map;
    }

    private HashMap<String, Integer> countWordsIgnoreCase(String[] words) {
        HashMap<String, Integer> map = new HashMap<>();
        final int len = words.length;
        for (int i = 0; i < len; i++) {
            String word = words[i].toLowerCase();
            Integer count = map.get(word);
            map.put(word, count == null ? 1 : count + 1);
        }
        return map;
    }

}
