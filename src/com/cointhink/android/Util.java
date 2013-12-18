package com.cointhink.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static Date iso8601ToDate(final String iso8601string) {
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 19) + s.substring(s.length()-6);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();                
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
