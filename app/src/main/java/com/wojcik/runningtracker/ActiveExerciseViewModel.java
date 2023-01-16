package com.wojcik.runningtracker;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.wojcik.runningtracker.utility.Calculate;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ActiveExerciseViewModel extends AndroidViewModel {
    private ExerciseTrackingService2.ExerciseTrackingBinder2 binder;
    private ExecutorService executorService;
    private Application application;

    private MutableLiveData<Float> distanceTraveled;
    private MutableLiveData<Float> totalTime;
    private MutableLiveData<Float> avgPace;


    public MutableLiveData<Float> getDistanceTraveled() {
        if(distanceTraveled == null)
            distanceTraveled = new MutableLiveData<>();
        return distanceTraveled;
    }
    public MutableLiveData<Float> getTotalTime() {
        if(totalTime == null)
            totalTime = new MutableLiveData<>();
        return totalTime;
    }
    public MutableLiveData<Float> getAvgPace() {
        if(avgPace == null)
            avgPace = new MutableLiveData<>();
        return avgPace;
    }
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    // TODO : need to save this data
    private LocalDateTime startDateTime;

    public ActiveExerciseViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        LocalDateTime.now();
    }

    public void startTrackingService(){
        Intent intent = new Intent(application, ExerciseTrackingService2.class);
        application.startForegroundService(intent);
        application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if(startDateTime == null)
            startDateTime = LocalDateTime.now();
    }

    // Stop the service on unbind
    public void stopTrackingService(Application application){
        executorService.shutdownNow();

        boolean isBound = false;

        isBound = application.bindService( new Intent(application, ExerciseTrackingService2.class), serviceConnection, Context.BIND_AUTO_CREATE );

        if (isBound){
            if(binder != null)
                binder.stopTrackingService();
            application.unbindService(serviceConnection);
        }

//        application.stop
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("OnServiceStarted", "onservice");
            binder = (ExerciseTrackingService2.ExerciseTrackingBinder2) iBinder;

            binder.startTrackingService();

            executorService = Executors.newFixedThreadPool(1);

            executorService.execute(new Runnable() {
                Handler uiThread = new Handler(Looper.getMainLooper());

                @Override
                public void run() {
                    while(!Thread.currentThread().isInterrupted()){
                        try{
                            Thread.sleep(1000);

                            if(binder == null)
                                continue;

                            // TODO : also need to save it to the save state bundle
                            uiThread.post(() -> getDistanceTraveled().setValue(binder.getDistance()));
                            uiThread.post(() -> getTotalTime().setValue(binder.getTime()));
                            uiThread.post(() -> getAvgPace().setValue(Calculate.calculateAvgPace(binder.getTime(), binder.getDistance())));

                            Log.d("ThreadInActiveExerciseViewModel", "still going");

                        }catch(InterruptedException e){
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binder = null;
        }
    };


}
