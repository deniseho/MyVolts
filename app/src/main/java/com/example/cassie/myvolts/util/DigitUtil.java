package com.example.cassie.myvolts.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cassie on 29/06/2017.
 */

public class DigitUtil {

    public static String getNumericTid(String s){
        return s.substring(s.indexOf("id_")+3);
    }

    public static String getNumericPid(String s){
        return s.substring(s.indexOf("id_")+3);
    }
}
