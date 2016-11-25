package car.adroid.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import car.adroid.config.AppConfig;

/**
 * Created by jjh on 2016-11-25.
 */

public class SimpleLogger {

    public static void debug(Context context , String txt) {
        Log.d(context.getClass().toString() , txt );
        if(AppConfig.DEBUG){
            String _txt = "debug::" + context.getClass().toString() + txt;
            Toast.makeText(context , _txt , Toast.LENGTH_SHORT).show();
        }
    }

    public static void info(Context context , String txt) {
        Log.i(context.getClass().toString() , txt );
        if(AppConfig.DEBUG){
            String _txt = "info::" + context.getClass().toString() + txt;
            Toast.makeText(context , _txt , Toast.LENGTH_SHORT).show();
        }
    }




}
