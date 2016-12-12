package car.adroid.com;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import car.adroid.service.InGameHttpService;
import car.adroid.service.InGameLocalService;
import car.adroid.service.LocationService;
import car.adroid.service.TeamSelectHttpService;
import car.adroid.util.SimpleLogger;

public class MainActivity extends FragmentActivity {

    public static final int PERMISSION_REQ_CODE = 0;
    private boolean mStarted = false;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET , Manifest.permission.VIBRATE};
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQ_CODE);

//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},2);


        //startService(new Intent(MainActivity.this , LocationService.class));

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    //해당 권한이 승낙된 경우.
                    mStarted = true;
                    startService(new Intent( mContext , SimpleLogger.class));
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    //해당 권한이 거절된 경우.
                    Toast.makeText(this,"permission denied.." , Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if(mStarted) {
            stopService(new Intent(mContext, SimpleLogger.class));
            stopService(new Intent(mContext, LocationService.class));
            stopService(new Intent(mContext, InGameLocalService.class));
            stopService(new Intent(mContext, InGameHttpService.class));
            stopService(new Intent(mContext, TeamSelectHttpService.class));
            finish();
        }
    }
}
