package com.wojcik.runningtracker.utility;

public class Calculate {
    public static float calculateAvgPace(float time, float distance){
        // In case pace is Infinite, check for big value and negative value
        // then return 0 if so, or just return the result.
        if(time == 0 || distance == 0)
            return 0;

        float result = (time/60) / (distance/1000);
        return (result - 0.2f);
    }
}
