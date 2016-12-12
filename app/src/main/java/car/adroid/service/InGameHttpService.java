package car.adroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import car.adroid.config.AppConfig;
import car.adroid.conn.HttpConnector;
import car.adroid.data.AppDBHelper;
import car.adroid.data.AppData;
import car.adroid.data.User;
import car.adroid.util.SimpleLogger;

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
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mHandler.postDelayed(mRunnable , AppConfig.HTTP_REQUEST_REPEAT_INTERVAL);
            }
        };
        mBroadcast = new Intent();
        mBroadcast.setAction(AppConfig.BROADCAST_ACTION_IN_GAME);

        mRunnable = new Runnable() {
            public void run() {

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        AppData data = AppData.getInstance(getApplicationContext());
                        String result = "";
                        try {
                            JSONObject response = new HttpConnector().inGame(data.getRoomId() , data.getUserNo() , data.getTeam() , data.getLatitude()
                                    , data.getLongitude() , data.getState() , data.getLastChatIdx() , data.getLastTeamChatIdx());
                            result = response.getString("result");

                            JSONArray userList  = response.getJSONArray("userList");
                            JSONArray chatList = response.getJSONArray("chatList");
                            JSONArray teamChatList = response.getJSONArray("teamChatList");
                            int lastChatIdx = response.getInt("lastChatIdx");
                            int lastTeamChatIdx = response.getInt("lastTeamChatIdx");

                            data.setLastChatIdx(lastChatIdx);
                            data.setLastTeamChatIdx(lastTeamChatIdx);

                            SQLiteDatabase db = new AppDBHelper(mContext).getWritableDatabase();
                            User user = new User();
                            for(int i=0 ; i<userList.length() ; i++){
                                user.getUserInfoAtJsonObject(userList.getJSONObject(i));
                                data.aplyUser(db , user);
                            }


                            for(int i=0 ; i<chatList.length() ; i++){
                                JSONObject chatObj = chatList.getJSONObject(i);
                                data.insertChatData(db
                                        , chatObj.getInt("idx")
                                        , chatObj.getInt("chat_flag")
                                        , chatObj.getInt("team")
                                        , chatObj.getInt("user_no")
                                        , chatObj.getString("nickname")
                                        , chatObj.getString("wr_time")
                                        , chatObj.getString("text"));
                            }

                            for(int i=0 ; i<teamChatList.length() ; i++){
                                JSONObject chatObj = teamChatList.getJSONObject(i);
                                data.insertChatData(db
                                        , chatObj.getInt("idx")
                                        , chatObj.getInt("chat_flag")
                                        , chatObj.getInt("team")
                                        , chatObj.getInt("user_no")
                                        , chatObj.getString("nickname")
                                        , chatObj.getString("wr_time")
                                        , chatObj.getString("text"));
                            }

                            db.close();

                        } catch (Exception e) {
                            SimpleLogger.debug(mContext , e.toString());
                            e.printStackTrace();
                        }
                        mBroadcast.putExtra("result" , result);
                        sendBroadcast(mBroadcast);
                        mHandler.sendEmptyMessage(0);
                    }
                }.start();
            }
        };

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int rtn =  super.onStartCommand(intent, flags, startId);
        mHandler.post(mRunnable);
        return rtn;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

}
