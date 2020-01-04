package com.lhstack.config.proxy.properties;

/**
 * author: hp
 * date: 2020/1/3
 **/
public class GlobalConfigProperties {

    private static Boolean ENABLE = true;

    private static String APPLICATION_NAME = "default";

    private static String SINGING_KEY = "asdf153zdg5212sg";

    private static String MONITORY_SQL_URI = "https://admin.lhstack.xyz/remote/sql/push";

    public static Boolean getENABLE() {
        return ENABLE;
    }

    public static void setENABLE(Boolean ENABLE) {
        GlobalConfigProperties.ENABLE = ENABLE;
    }

    public static String getApplicationName() {
        return APPLICATION_NAME;
    }

    public static void setApplicationName(String applicationName) {
        APPLICATION_NAME = applicationName;
    }

    public static String getSingingKey() {
        return SINGING_KEY;
    }

    public static void setSingingKey(String singingKey) {
        SINGING_KEY = singingKey;
    }

    public static String getMonitorySqlUri() {
        return MONITORY_SQL_URI;
    }

    public static void setMonitorySqlUri(String monitorySqlUri) {
        MONITORY_SQL_URI = monitorySqlUri;
    }
}
