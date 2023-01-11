package com.wojcik.runningtracker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                // Check if location permission was granted and that it was fine location, not coarse
                if(isGranted && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    startRunningActivity();
                }else{
                    weRequireFineLocation();
                }
            });

    private void weRequireFineLocation(){
        // explain to user that the feature is unavailable because the feature requires
        // a permission that the user has denied. but don't link them to settings or anything

        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setTitle("Location access")
                .setMessage("Sorry, we require precise location permission to accurately track your activity.");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {});
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void trackLocation(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startRunningActivity();
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startRunningActivity(){
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }
}