package car.adroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import car.adroid.config.AppConfig;
import car.adroid.util.SimpleLogger;

public class HttpDataAccessService extends Service {

    private Context mContext = this;
    private Handler handler = null;
    private static Runnable runnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, AppConfig.HTTP_REQUEST_REPEAT_INTERVAL);
            }
        };
        handler.post(runnable);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int rtn =  super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        return rtn;
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        SimpleLogger.debug(mContext, "");
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


}