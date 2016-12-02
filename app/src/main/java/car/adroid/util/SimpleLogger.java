package car.adroid.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import car.adroid.config.AppConfig;

/**
 * Created by jjh on 2016-11-25.
 */

public class SimpleLogger {

    private static StringBuffer mBuffer = new StringBuffer();
    private static Date mLastShow = new Date();
    private static final String TAG = "SimpleLogger";


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

        Date curDate = new Date();
        if(curDate == null){
            curDate = new Date();
        }
        if(mBuffer == null){
            mBuffer = new StringBuffer();
        }
        if((curDate.getTime() -  mLastShow.getTime()) > 2000) {
            Toast.makeText(context , mBuffer.toString() , Toast.LENGTH_SHORT).show();
            mLastShow = curDate;
            mBuffer.setLength(0);
        }
        else {
            mBuffer.append(txt).append("\n");
        }
    }

}
