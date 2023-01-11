package com.wojcik.runningtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
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

public class ExerciseTrackingService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private final int NOTIFICATION_ID = 001;
    private final String CHANNEL_ID = "100";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private Location location;

    public Location getLocation() {
        return location;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createNotification() {
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Tracking your location")
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

    private void setUpLocationTracking(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location loc : locationResult.getLocations())
                {
                    location = loc;
                    updateNotificationText();
                    Log.d("ExerciseTrackingService", "location " + loc.toString());
                }
            }
        };
    }

    // TODO : Update notification text to show distance and time maybe
    private void updateNotificationText(){
        notificationBuilder.setContentText("location :" + location.getLongitude());
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void startTrackingLocation(){
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

    @Override
    public void onDestroy() {
        // Make sure we stop tracking location before destroying service
        stopTrackingLocation();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ExerciseTrackingBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class ExerciseTrackingBinder extends Binder implements IBinder{
        public void startTracking(){startTrackingLocation();};
        public void stopTracking(){stopTrackingLocation();};
    }
}
