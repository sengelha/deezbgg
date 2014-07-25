package info.deez.deezbgg.utils;

public class StringUtils {
    public static boolean areEqualOrBothNull(String s1, String s2) {
        if (s1 == null && s2 == null)
            return true;
        else if (s1 == null)
            return false;
        else if (s2 == null)
            return false;
        else
            return s1.equals(s2);
    }
}
