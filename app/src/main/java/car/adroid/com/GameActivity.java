package car.adroid.com;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;

public class GameActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener {

    private Button btnTeamChat, btnGlobalChat, btnUserlist;
    private TextView tvTime;

    public static final String API_KEY = "81o5Z9_NQ8ryz_suFhyi";
    private NMapView mMapView = null;
    private NMapController mMapController = null;
    private LinearLayout MapContainer;

    @Override
    public void onMapInitHandler(NMapView mapView, NMapError errorInfo){
        if (errorInfo == null){
            mMapController.setMapCenter(new NGeoPoint(126.978371, 37.566691), 11);
        }
        else{
            android.util.Log.e("NMAP", "onMapInitHandler: error=" + errorInfo.toString());
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onLongPressCanceled(NMapView nMapView) {

    }

    @Override
    public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {

    }

    @Override
    public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnTeamChat = (Button)findViewById(R.id.btnTeamChat);
        btnGlobalChat = (Button)findViewById(R.id.btnGlobalChat);
        btnUserlist = (Button)findViewById(R.id.btnUserlist);
        tvTime = (TextView)findViewById(R.id.tvTime);

        MapContainer = (LinearLayout)findViewById(R.id.nmap);

        mMapView = new NMapView(this);
        mMapView.setApiKey(API_KEY);
        setContentView(mMapView);
        mMapView.setClickable(true);
        mMapView.setOnMapStateChangeListener(this);
        mMapView.setOnMapViewTouchEventListener(this);
        mMapView.setBuiltInZoomControls(true, null);
        mMapController = mMapView.getMapController();


        btnTeamChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, TeamChat.class);
                startActivity(intent);
            }
        });

        btnGlobalChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, GlobalChat.class);
                startActivity(intent);
            }
        });

        btnUserlist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // show all players on the map
            }
        });

    }

}
