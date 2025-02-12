package service;

public class ValidationUtils {

    public static boolean isValidString(String str, int minLength, int maxLength) {
        return str != null && str.length() >= minLength && str.length() <= maxLength;
    }

    public static boolean isValidNumber(int num, int min, int max) {
        return num >= min && num <= max;
    }

    public static boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
}
