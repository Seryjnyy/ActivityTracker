package com.wojcik.runningtracker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wojcik.runningtracker.annotation.AnnotatingActivity;
import com.wojcik.runningtracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private enum AvailableFragment {
        HOME,
        STATS,
        ALL_ACTIVITY
    }

    private AvailableFragment currentFragment;



    private ActivityMainBinding binding;

    private void changeFragment(AvailableFragment fragment){
        switch (fragment){
            case HOME:
                replaceFragment(new HomeFragment());
                currentFragment = AvailableFragment.HOME;
                break;
            case STATS:
                replaceFragment(new StatsFragment());
                currentFragment = AvailableFragment.STATS;
                break;
            case ALL_ACTIVITY:
                replaceFragment(new AllActivityFragment());
                currentFragment = AvailableFragment.ALL_ACTIVITY;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Intent intent = new Intent(this, AnnotatingActivity.class);
//        startActivity(intent);

        // Initialise in case on saved instance is called and it tried to get string from null
        currentFragment = AvailableFragment.HOME;

        if(savedInstanceState != null){
            if(savedInstanceState.getString("currentFragment") != null){
                changeFragment(getAsAvailableFragment(savedInstanceState.getString("currentFragment")));
            }else{
                changeFragment(AvailableFragment.HOME);
            }
        }else{
            changeFragment(AvailableFragment.HOME);
        }


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.home:
                    changeFragment(AvailableFragment.HOME);
                    break;
                case R.id.allActivity:
                    changeFragment(AvailableFragment.ALL_ACTIVITY);
                    break;
                case R.id.stats:
                    changeFragment(AvailableFragment.STATS);
                    break;
            }

            return true;
        });



//        // TODO : Remove this because its for testing

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("currentFragment", currentFragment.toString());

    }

    private AvailableFragment getAsAvailableFragment(String name){
        switch(name){
            case "HOME":
                return AvailableFragment.HOME;
            case "STATS":
                return AvailableFragment.STATS;
            case "ALL_ACTIVITY":
                return AvailableFragment.ALL_ACTIVITY;
            default:
                return AvailableFragment.HOME;
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState.getString("currentFragment") != null){
            currentFragment = getAsAvailableFragment(savedInstanceState.getString("currentFragment"));
        }
    }
}