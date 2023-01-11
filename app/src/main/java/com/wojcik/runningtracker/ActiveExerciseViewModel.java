package com.wojcik.runningtracker;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class ActiveExerciseViewModel extends AndroidViewModel {
    private ExerciseTrackingService.ExerciseTrackingBinder binder;
    private final Application application;

    public ActiveExerciseViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
    }

    public void startTrackingService(){
        Log.d("trying", "trying to start");
        Intent intent = new Intent(application, ExerciseTrackingService.class);

        application.startForegroundService(intent);
        application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    // TODO : Instead of the service stopping on unbind could have a way to stop it
    public void stopTrackingService(){
        // in case we try to stop service before we even got a binder
        if(binder != null)
            binder.stopTracking();

        application.stopService(new Intent(application, ExerciseTrackingService.class));
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (ExerciseTrackingService.ExerciseTrackingBinder) iBinder;
            binder.startTracking();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binder = null;
        }
    };
}
