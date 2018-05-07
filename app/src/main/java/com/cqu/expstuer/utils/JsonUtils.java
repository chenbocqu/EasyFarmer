package com.cqu.expstuer.utils;


public class JsonUtils {

    /**
     * 判断字符是否为JSONObj
     */
    public static boolean isJSONObj(String response) {

        if (response == null || response.length() == 0) return false;

        if ('{' == response.charAt(0))
            return true;

        return false;
    }

}
