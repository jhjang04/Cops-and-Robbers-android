package car.adroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import car.adroid.config.AppConfig;
import car.adroid.data.AppData;

public class InGameLocalService extends Service {
    private Context mContext = this;
    private Handler mHandler = null;
    private Runnable mRunnable = null;
    private Intent mBroadcast = null;

    public InGameLocalService() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        mHandler = new Handler();
        mBroadcast = new Intent();
        mBroadcast.setAction(AppConfig.BROADCAST_ACTION_IN_GAME_LOCAL);

        mRunnable = new Runnable() {
            public void run() {
                mHandler.postDelayed(mRunnable , AppConfig.WARNING_VIBERATE_MILISECONDS);
                AppData data = AppData.getInstance(getApplicationContext());
                if (data.isViberate()) {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(AppConfig.WARNING_VIBERATE_MILISECONDS);

                }

            }
        };


        mHandler.post(mRunnable);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int rtn = super.onStartCommand(intent, flags, startId);
        return rtn;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
