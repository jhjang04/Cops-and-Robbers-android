package car.adroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import car.adroid.config.AppConfig;
import car.adroid.util.SimpleLogger;

public class InGameHttpService extends Service {
    private Context mContext = this;
    private Handler mHandler = null;
    private static Runnable mRunnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                mHandler.postDelayed(mRunnable, AppConfig.HTTP_REQUEST_REPEAT_INTERVAL);
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
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        SimpleLogger.debug(mContext, "");
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

}
