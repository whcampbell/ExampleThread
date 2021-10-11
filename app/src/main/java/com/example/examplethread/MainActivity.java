package com.example.examplethread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public class ExampleRunnable implements Runnable {
        public void run() {
            mockFileDownloader();
        }
    }

    private static final String TAG = "MainActivity";
    private Button startButton;
    private TextView progText;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mockFileDownloader() {
        startButton = findViewById(R.id.button);
        progText = findViewById(R.id.textView);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Downloading...");
            }
        });


        for (int downloadProgress = 0; downloadProgress <=100; downloadProgress=downloadProgress + 10) {

            if (stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                        progText.setText("");
                    }
                });
                return;
            }

            Log.d(TAG, "Download progress: " + downloadProgress + "%");
            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progText.setText("Progress: " + finalDownloadProgress + "%");
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
                progText.setText("");
            }
        });


    }

    public void startDownload(View view) {
        stopThread = false;
        ExampleRunnable rbl = new ExampleRunnable();
        new Thread(rbl).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }
}

