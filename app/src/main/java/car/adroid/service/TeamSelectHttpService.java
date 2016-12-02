package car.adroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import car.adroid.config.AppConfig;

public class TeamSelectHttpService extends Service {

    private Context mContext = this;
    private Handler mHandler = null;
    private Runnable mRunnable = null;
    private Intent mBroadcast = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        mHandler = new Handler();
        mBroadcast = new Intent();
        mBroadcast.setAction(AppConfig.BROADCAST_ACTION_TEAM_SELECT);

        mRunnable = new Runnable() {
            public void run() {
                mHandler.postDelayed(mRunnable, AppConfig.HTTP_REQUEST_REPEAT_INTERVAL);

                sendBroadcast(mBroadcast);
            }
        };
        mHandler.post(mRunnable);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int rtn =  super.onStartCommand(intent, flags, startId);
        return rtn;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

}
