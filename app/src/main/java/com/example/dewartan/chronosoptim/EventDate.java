package com.example.dewartan.chronosoptim;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class EventDate {
    private static final int MAX_DAYS = 90;
    private static final Date maxDay=createMaxDay();
    private static final SimpleDateFormat parser=createFormat();

    private static Date createMaxDay(){
        Calendar maxCal=new GregorianCalendar();
        maxCal.add(Calendar.DATE,MAX_DAYS+1);
        return maxCal.getTime();
    }

    private static SimpleDateFormat createFormat(){
        return new SimpleDateFormat("MM-dd-yyyy");
    }

    public static boolean beforeMax(Event event){
        Date evDate;
        try {
            evDate = parser.parse(event.getDate());
        }catch(ParseException ex){
            evDate=null;
        }
        return evDate.before(maxDay);
    }

    public static boolean beforeMax(Calendar cal){
        return cal.getTime().before(maxDay);
    }

    public static String pretty(String s){
        // "MM-dd-yyyy" ->  "dow: M/d"
        Date date=null;
        try{
            date=parser.parse(s);
        }catch(ParseException ex){
            //
        }
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.US).format(date);
        s=dayOfWeek + ": " + new SimpleDateFormat("M/d").format(date);
        return s;
    }

    public static String convert(Calendar c){
        return parser.format(c.getTime());
    }

    public static boolean matches(Calendar calendar,Event event){
        Log.d("matches", convert(calendar));
        return event.getDate().equals(convert(calendar));
    }

    public static boolean contains(ArrayList<String> headers,Event event){
        return headers.contains(pretty(event.getDate()));
    }

}
