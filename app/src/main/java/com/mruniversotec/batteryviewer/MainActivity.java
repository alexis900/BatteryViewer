package com.mruniversotec.batteryviewer;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity {

    private WaveLoadingView mWaveLoadingView;
    private Handler mHandler;
    private Runnable mRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setProgressValue(0);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batteryLevel();
                mWaveLoadingView.setProgressValue(level);
                if (level < 50) {
                    mWaveLoadingView.setBottomTitle(String.format("%d%%", level));
                    mWaveLoadingView.setCenterTitle("");
                    mWaveLoadingView.setTopTitle("");
                }else if(level < 80) {
                    mWaveLoadingView.setBottomTitle("");
                    mWaveLoadingView.setCenterTitle(String.format("%d%%", level));
                    mWaveLoadingView.setTopTitle("");
                }else{
                    mWaveLoadingView.setBottomTitle("");
                    mWaveLoadingView.setCenterTitle("");
                    mWaveLoadingView.setTopTitle(String.format("%d%%", level));
                }

                mHandler.postDelayed(mRunnable, 100);
            }
        };

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 0);
    }

public float batteryLevel(){
    Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

    if (level == -1 || scale == -1){
        return 50.0f;
    }
    return ((float) level / (float) scale) * 100.0f;
}

}
