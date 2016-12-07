package car.adroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import car.adroid.config.AppConfig;
import car.adroid.conn.HttpConnector;
import car.adroid.data.AppData;

public class InGameHttpService extends Service {

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
        mBroadcast.setAction(AppConfig.BROADCAST_ACTION_IN_GAME);

        mRunnable = new Runnable() {
            public void run() {
                mHandler.postDelayed(mRunnable, AppConfig.HTTP_REQUEST_REPEAT_INTERVAL);

                AppData data = AppData.getInstance(mContext);

                Map params = new HashMap<>();
                params.put("room_id",data.getRoomId());
                params.put("user_no",data.getUserNo());
                params.put("latitude",data.getLatitude());
                params.put("longitude",data.getLongitude());
                params.put("state",data.getState());
                params.put("lastChatIdx",data.getLastChatIdx());
                params.put("lastTeamChatIdx",data.getLastTeamChatIdx());

                try {
                    JSONObject rst = HttpConnector.SimpleRequest("playing",params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
