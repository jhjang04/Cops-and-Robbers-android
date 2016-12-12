package car.adroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import car.adroid.config.AppConfig;
import car.adroid.conn.HttpConnector;
import car.adroid.data.AppDBHelper;
import car.adroid.data.AppData;
import car.adroid.data.User;
import car.adroid.util.SimpleLogger;

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
                try {
                    mHandler.postDelayed(mRunnable, AppConfig.HTTP_REQUEST_REPEAT_INTERVAL);
                    new Thread(){
                        public void run() {
                            AppData appData = AppData.getInstance(getApplicationContext());
                            String result = null;
                            String startTime = null;
                            JSONArray userList = null;
                            try {
                                JSONObject res = new HttpConnector().selectTeam(appData.getRoomId(), appData.getUserNo(), appData.getTeam(), appData.getReadyStatus());
                                result = res.getString("result");
                                userList = res.getJSONArray("userList");
                                startTime = res.getString("startTime");

                                if(!startTime.equals("")) {
                                    appData.updateStartTime(startTime);
                                }

                                User user = new User();
                                AppDBHelper dbHelper = new AppDBHelper(mContext);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                for (int i = 0; i < userList.length(); i++) {
                                    user.getUserInfoAtJsonObject(userList.getJSONObject(i));
                                    appData.aplyUser(db, user);
                                }
                                db.close();
                            } catch (Exception e) {
                                SimpleLogger.debug(mContext, e.toString());
                                e.printStackTrace();
                            }

                            mBroadcast.putExtra("result", result);
                            sendBroadcast(mBroadcast);
                        }
                    }.start();
                }
                catch (Exception e){
                    SimpleLogger.debug(mContext , e.toString());
                    e.printStackTrace();
                }
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
