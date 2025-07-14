package com.scopevisio.praemiepro.util;

import java.util.ResourceBundle;

public class I18NProvider {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n.messages");

    public static String getMessage(String key) {
        return resourceBundle.getString(key);
    }
}
