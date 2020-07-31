package com.thoughtworks.todo_list.ui.utils;

import java.util.regex.Pattern;

public class Validator {
    public final static String USERNAME_REGULAR = "\\w{3,12}$";
    public final static String PASSWORD_REGULAR = "[\\s\\S]{6,18}$";

    public static Boolean isValid(String regular, String value) {
        return Pattern.matches(regular, value);
    }
}
