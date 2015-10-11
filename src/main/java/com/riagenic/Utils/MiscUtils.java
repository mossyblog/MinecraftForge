package com.riagenic.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Scott on 10/10/2015.
 */
public class MiscUtils {

    private static final Logger logger = LogManager.getLogger();
    public static boolean isInteger(String s)
    {
        try
        {
            Integer.parseInt(s);
        }catch(NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    public static int countMatches(String string, String regex)
    {
        Matcher matcher = Pattern.compile(regex).matcher(string);
        int count = 0;
        while(matcher.find())
            count++;
        return count;
    }
}
