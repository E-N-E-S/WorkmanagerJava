package com.example.workmanagerjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder().putInt("intKey", 1).build();

        Constraints constraints = new Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED)// It works, if device connected to WIFI
                .setRequiresCharging(false) // It works, if device charging (if true)
                .build();

        /*
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class) // One Time Work
                .setConstraints(constraints)
                .setInputData(data)
                // .setInitialDelay(5, TimeUnit.MINUTES) // It will start working 5 minutes after the application is opened.
                // .addTag("myTag")
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
         */

        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);

        // Logs
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo.getState() == WorkInfo.State.FAILED){
                    System.out.println("running");
                } else if (workInfo.getState() ==  WorkInfo.State.SUCCEEDED) {
                    System.out.println("succeeded");
                } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                    System.out.println("failed");
                }
            }
        });

         // Cancel
         /*
         WorkManager.getInstance(this).cancelAllWork();
         WorkManager.getInstance(this).cancelAllWorkByTag();
         */

        // Chaining
        /*
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
                .then(oneTimeWorkRequest2)
                .then(oneTimeWorkRequest3);
                .enqueue();
         */



    }
}