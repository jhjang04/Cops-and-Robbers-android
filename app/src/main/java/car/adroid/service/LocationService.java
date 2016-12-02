package car.adroid.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import car.adroid.config.AppConfig;
import car.adroid.data.AppDBHelper;
import car.adroid.data.AppData;
import car.adroid.util.SimpleLogger;

public class LocationService extends Service {

    public Context mContext = this;
    private LocationManager mLocMan;
    private String mProvider;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mProvider = mLocMan.getBestProvider(new Criteria(), true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int superRtn = super.onStartCommand(intent, flags, startId);
        SimpleLogger.debug(mContext , "start Location service");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //return START_NOT_STICKY;
            return superRtn;
        }
        mLocMan.requestLocationUpdates(mProvider, AppConfig.LOCATION_RECIVE_MILISECONDS , AppConfig.LOCATION_RECEIVE_DISTANCE , mListener);

        //return START_STICKY;
        return superRtn;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        SimpleLogger.debug(mContext , "stop service");
        mLocMan.removeUpdates(mListener);
    }

    LocationListener mListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d("tag","listen");
            AppData data = AppData.getInstance(mContext);
//            data.setLatitude(location.getLatitude());
//            data.setLongitude(location.getLongitude());
            AppDBHelper dbHelper = new AppDBHelper(mContext);
            data.updateLocalLocation(mContext , location.getLatitude() , location.getLongitude());
            SimpleLogger.debug(mContext , "lat : " + location.getLatitude() + ", lot : " + location.getLongitude() );

        }

        public void onProviderDisabled(String provider) {
            //서비스사용불가
        }

        public void onProviderEnabled(String provider) {
            //서비스 사용 가능
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            String sStatus = "";
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE: //범위 벗어남
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:  //일시적 불능
                    break;
                case LocationProvider.AVAILABLE:    //사용 가능
                    break;
            }
        }
    };
}
