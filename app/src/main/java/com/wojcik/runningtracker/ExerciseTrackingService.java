package com.wojcik.runningtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.wojcik.runningtracker.utility.Calculate;
import com.wojcik.runningtracker.utility.TextFormatter;

public class ExerciseTrackingService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private final int NOTIFICATION_ID = 001;
    private final String CHANNEL_ID = "100";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;


    private Notification buildNotification() {

        // TODO : in exercise activity we need to know if the service is already running
        Intent intent = new Intent(this, ExerciseActivity.class);

        // TODO : could potentially cause problems like in the past
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Tracking your location")
                .setContentIntent(pendingIntent)
                .setContentText("We on your ass boy")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return notificationBuilder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setUpLocationTracking();

        // Create notification channel
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        CharSequence name = "Running tracker channel";
        String description = "Channel for Running tracker app";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }

    private Location lastLocationUsedForDistanceTraveled;


    private float MIN_ACC_METERS_FOR_DISTANCE_TRAVELED = 30;
    private float MAX_WALKING_SPEED = 2;


    private Location lastLocationReceived;
    private float distanceTraveled = 0;
    private float time = 0;
    private float avgPace = 0;

    public float getCumulativeDistanceTraveled() {
        return distanceTraveled - firstCollectedDistance;
    }

    public float getCumulativeTime() {
        return time;
    }

    public float getAveragePace(){
        return Calculate.calculateAvgPace(getCumulativeTime(), getCumulativeDistanceTraveled());
    }

    private float firstCollectedDistance;

    private void setUpLocationTracking(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                time += 1;
                Log.d("Time", ""+time);

                if(lastLocationUsedForDistanceTraveled == null)
                    lastLocationUsedForDistanceTraveled = new Location(locationResult.getLastLocation());

                if(lastLocationReceived == null)
                    lastLocationReceived = new Location(locationResult.getLastLocation());


                lastLocationReceived.set(locationResult.getLastLocation());


                if(lastLocationReceived.hasAccuracy() && lastLocationReceived.getAccuracy() <= MIN_ACC_METERS_FOR_DISTANCE_TRAVELED){

                    if ((lastLocationReceived.getAccuracy() * 1.5) < lastLocationReceived.distanceTo(lastLocationUsedForDistanceTraveled)) {

                        long timeDelta = lastLocationReceived.getTime() - lastLocationUsedForDistanceTraveled.getTime();

                        if ((lastLocationReceived.distanceTo(lastLocationUsedForDistanceTraveled) / timeDelta) < MAX_WALKING_SPEED) {
                            // NOW we have a value we can use to minimize error
                            if(distanceTraveled == 0){
                                firstCollectedDistance = lastLocationReceived.distanceTo(lastLocationUsedForDistanceTraveled);
                            }

                            distanceTraveled += lastLocationReceived.distanceTo(lastLocationUsedForDistanceTraveled);
                            avgPace = (time/60) / (getCumulativeDistanceTraveled()/1000);
                            Log.d("DistanceTraveled", ""+ getCumulativeDistanceTraveled());

                            Log.d("Pace", ""+avgPace);
                            // Update last valid location
                            lastLocationUsedForDistanceTraveled.set(lastLocationReceived);

//                            updateNotificationText();
                        }
                }}



//                for (Location loc : locationResult.getLocations())
//                {
//                    loc.s
////                    updateTrackingData(loc);
////                    updateNotificationText();
////                    Log.d("ExerciseTrackingService", "location " + loc.toString());
//                }
            }
        };
    }

    // TODO : Update notification text to show distance and time maybe
    private void updateNotificationText(){
        notificationBuilder.setContentText("Distance :" + TextFormatter.formatDistance(getCumulativeDistanceTraveled()) + " Time :" + TextFormatter.formatTime(getCumulativeTime()));
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void startTrackingLocation(){
        startForeground(NOTIFICATION_ID, buildNotification());

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build();

        try{
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }catch (SecurityException e){
            Log.d("ExerciseTrackingService", e.getMessage());
        }

    }

    private void stopTrackingLocation(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void stopTrackingService(){
        stopTrackingLocation();
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new ExerciseTrackingBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopTrackingService();
        Log.d("onUnbind", "trying to unbind");

        return super.onUnbind(intent);
    }

    public class ExerciseTrackingBinder extends Binder implements IBinder{
        // Service related
        public void stopService(){stopTrackingService();};

        // Tracking related
        public void startTracking(){startTrackingLocation();};
        public void stopTracking(){stopTrackingLocation();};

        // Getters for data
        public float getDistanceTraveled(){return getCumulativeDistanceTraveled();}
        public float getTime(){return getCumulativeTime();}
        public float getAvgPace(){return getAveragePace();}

    }

    public Location getLastLocationReceived() {
        return lastLocationReceived;
    }

    public void setLastLocationReceived(Location lastLocationReceived) {
        this.lastLocationReceived = lastLocationReceived;
    }

}

