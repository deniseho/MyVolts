package com.example.cassie.myvolts.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by cassie on 13/06/2017.
 */

public class RegexUtil {

    public static String[] spliteRegex(String value){

        String[] search = value.split(" ");

        return search;
    }
}
