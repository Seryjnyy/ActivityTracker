package com.wojcik.runningtracker.utility;

import android.util.Log;

import java.text.DecimalFormat;
import java.time.Duration;

public class TextFormatter {
    public static String formatDistance(float distance){
        float rounded = ((float) Math.round((distance/1000)*100))/100;
        return ""+rounded+"km";
    }

    public static String formatTime(float time){
        Duration duration = Duration.ofSeconds((int) time);
        long seconds = duration.getSeconds();

        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;

        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }

    // TODO : could be problems if there is no .
    public static String formatAvgPace(float avgPace){
        float rounded = ((float) Math.round((avgPace*100))/100);
        String[] separated = (""+rounded).split("\\.");
        return  ""+ separated[0] + ":" + separated[1] + "/km";
    }

    public static String formatDate(int day, int month, int year){
       return "" + day + "/" + month + "/" + year;
    }

    public static String getEmojiFromName(String name){
        if(name == null)
            return "missing";

        switch (name){
            case "walk":
                return new String(Character.toChars(0x1F6B6));
            case "run":
                return new String(Character.toChars(0x1F3C3));
            case "bike":
                return new String(Character.toChars(0x1F6B4));

            case "snowing":
                return new String(Character.toChars(0x1F328));
            case "raining":
                return new String(Character.toChars(0x1F327));
            case "cloudy":
                return new String(Character.toChars(0x2601));
            case "cloudySun":
                return new String(Character.toChars(0x26C5));
            case "sunny":
                return new String(Character.toChars(0x2600));

            case "awful":
                return new String(Character.toChars(0x1F915));
            case "bad":
                return new String(Character.toChars(0x1F641));
            case "average":
                return new String(Character.toChars(0x1F610));
            case "good":
                return new String(Character.toChars(0x1F642));
            case "great":
                return new String(Character.toChars(0x1F601));
            default:
                return "missing";
        }
    }

}
