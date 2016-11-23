package car.adroid.com;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.R.interpolator.linear;

public class GameActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener {

    private Button btnTeamChat, btnGlobalChat, btnUserlist;
    private TextView tvTime;

    final private String CLIENT_ID = "1Ure4ugOivjE9hHXmHld";//애플리케이션 클라이언트 아이디값";
    final private String CLIENT_SECRET = "oJoJK6OIFF";//애플리케이션 클라이언트 시크릿값";

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

        MapContainer = (LinearLayout) findViewById(R.id.nmap);

        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID);
        mMapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        MapContainer.addView(mMapView);
        //setContentView(mMapView);
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

    public static String getReverseGeoCodingInfo(double longitude, double latitude) {
        StringBuffer sb = new StringBuffer();
        String longi = String.valueOf(longitude);
        String lati = String.valueOf(latitude);
        BufferedReader br = null;
        InputStreamReader in = null;
        HttpsURLConnection httpConn = null;
        String address = null;


        // TODO Auto-generated method stub
        //"https://openapi.naver.com/v1/map/reversegeocode?query=127.1141382,37.3599968"
        try {
            URL url = new URL("https://openapi.naver.com/v1/map/reversegeocode?query=" + longi + "," + lati);
            httpConn = (HttpsURLConnection) url.openConnection();
            httpConn.setRequestProperty("X-Naver-Client-Id", "bDBAfTyCHhTCnzNjWBXi");
            httpConn.setRequestProperty("X-Naver-Client-Secret", "KxH3N2Bicc");
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            //int len = conn.getContentLength();
            in = new InputStreamReader(httpConn.getInputStream());
            br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            //return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject;
        JSONObject jsonObject2;
        JSONObject jsonObject3;
        JSONArray jsonArray;


        try {
            jsonObject = new JSONObject(sb.toString());
            jsonObject = (JSONObject) jsonObject.get("result");
            jsonArray = (JSONArray) jsonObject.get("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject2 = (JSONObject) jsonArray.get(i);
                if (null != jsonObject2.get("address")) {
                    address = (String) jsonObject2.get("address").toString();
                    Log.v("qioip", address);

                }
            }

            br.close();
            in.close();
            httpConn.disconnect();


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (address == null || address.isEmpty()) {
            return "위치 파악 중";
        } else {
            return address;
        }
    }

}
