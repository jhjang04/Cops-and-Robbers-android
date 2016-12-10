package car.adroid.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import car.adroid.config.AppConfig;

/**
 * Created by jjh on 2016-11-25.
 */

public class SimpleLogger extends Service {

    private static StringBuffer mBuffer = new StringBuffer();
    private static Date mLastShow = new Date();
    private static final String TAG = "SimpleLogger";

    private Context mContext = this;
    private Handler mHandler = null;
    private Runnable mRunnable = null;


    public static void debug(Context context , String txt) {
        String _txt = context.getClass().toString() + "::" + txt;
        Log.d(TAG , _txt);
        toast(context , "debug::" + _txt);
    }

    public static void info(Context context , String txt) {
        String _txt = context.getClass().toString() + "::" + txt;
        Log.i(TAG , _txt);
        toast(context , "info::" + _txt);
    }

    private static void toast(Context context , String txt){
        if(!AppConfig.DEBUG) {
            return;
        }

//        Date curDate = new Date();
//        if(mLastShow == null){
//            mLastShow= new Date();
//        }
        if(mBuffer == null){
            mBuffer = new StringBuffer();
        }
        mBuffer.append(txt).append("\n");

//        if((curDate.getTime() -  mLastShow.getTime()) > AppConfig.LOGGER_SHOW_DELAY) {
//            Toast.makeText(context , mBuffer.toString() , Toast.LENGTH_LONG).show();
//            mLastShow = curDate;
//            mBuffer.setLength(0);
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(!AppConfig.DEBUG){
            //stopService(new Intent(mContext , SimpleLogger.class));\
            stopSelf();
            return;
        }
        mHandler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                mHandler.postDelayed(mRunnable, AppConfig.LOGGER_SHOW_DELAY);
//                if((curDate.getTime() -  mLastShow.getTime()) > AppConfig.LOGGER_SHOW_DELAY) {
                if(mBuffer == null || mBuffer.length() == 0){
                    return;
                }
                Toast.makeText(mContext , mBuffer.toString() , Toast.LENGTH_LONG).show();
//                mLastShow = curDate;
                mBuffer.setLength(0);
            }
        };
        mHandler.post(mRunnable);
    }


    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

}
