package com.song.example.util;

/**
 * Created by Sushant on 01-12-2016.
 */
public class StringUtil {

    public static String formatHttp(String str) {
        str = str.replace("?", "%3F");
        str = str.replace(":", "%3A");
        str = str.replace("/", "%2F");
        str = str.replace("\\", "%5C");
        str = str.replace("=", "%3D");
        str = str.replace("&", "%26");
        return str;
    }

    public static String reformatHttp(String str) {
        str = str.replace("%3F", "?");
        str = str.replace( "%3A", ":");
        str = str.replace( "%2F", "/");
        str = str.replace("%5C", "\\");
        str = str.replace("%3D","=");
        str = str.replace("%26","&");

        return str;
    }
}
