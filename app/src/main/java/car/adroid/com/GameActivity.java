package car.adroid.com;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import car.adroid.NMap.NMapPOIflagType;
import car.adroid.NMap.NMapViewerResourceProvider;

public class GameActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener {

    private Button btnTeamChat, btnGlobalChat, btnUserlist;
    private TextView tvTime;

    final private String CLIENT_ID = "1Ure4ugOivjE9hHXmHld";//애플리케이션 클라이언트 아이디값";
    final private String CLIENT_SECRET = "oJoJK6OIFF";//애플리케이션 클라이언트 시크릿값";

    private NMapView mMapView = null;
    private NMapController mMapController = null;
    private LinearLayout MapContainer;


    private String[] navItems = {"Brown", "Cadet Blue", "Dark Olive Green",
            "Dark Orange", "Golden Rod"};
    private ListView lvUsrList;
    private FrameLayout usrContainer;


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    //private NMapOverlayItem a;


    //지도 위 오버레이 객체 드로잉에 필요한 리소스 데이터 제공 클래스
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    //오버레이 객체 관리 클래스
    private NMapOverlayManager mOverlayManager;
    //POI 아이템 선택 상태 변경 시 호출퇴는 콜백 인터페이스
    private NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener;

    private NMapMyLocationOverlay mMyLocationOverlay; //지도 위에 현재 위치를 표시하는 오버레이 클래스
    private NMapLocationManager mMapLocationManager; //단말기의 현재 위치 탐색 기능 사용 클래스
    private NMapCompassManager mMapCompassManager; //단말기의 나침반 기능 사용 클래스




    @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) {
            mMapController.setMapCenter(new NGeoPoint(126.978371, 37.566691), 11);
        } else {
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

    private void testOverlayMaker() { //오버레이 아이템 추가 함수
        int markerAlly = NMapPOIflagType.ALLY; //마커 id설정
        int markerOpponent = NMapPOIflagType.OPPONENT;
        // POI 아이템 관리 클래스 생성(전체 아이템 수, NMapResourceProvider 상속 클래스)
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2); // POI 아이템 추가 시작
        poiData.addPOIitem(127.081667, 37.242222, "marker1", markerAlly, 0);
        poiData.addPOIitem(127.081867, 37.242322, "marker2", markerOpponent, 0);
        poiData.endPOIdata(); // POI 아이템 추가 종료
        //POI data overlay 객체 생성(여러 개의 오버레이 아이템을 포함할 수 있는 오버레이 클래스)
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0); //모든 POI 데이터를 화면에 표시(zomLevel)
        //POI 아이템이 선택 상태 변경 시 호출되는 콜백 인터페이스 설정
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener =
            new NMapLocationManager.OnLocationChangeListener() { //위치 변경 콜백 인터페이스 정의
                //위치가 변경되면 호출
                @Override
                public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
                    if (mMapController != null) {
                        mMapController.animateTo(myLocation); //지도 중심을 현재 위치로 이동
                    }
                    return true;
                }
                //정해진 시간 내에 위치 탐색 실패 시 호출
                @Override
                public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
                }
                //현재 위치가 지도 상에 표시할 수 있는 범위를 벗어나는 경우 호출
                @Override
                public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
                    stopMyLocation(); //내 위치 찾기 중지 함수 호출
                }
            };

    private void startMyLocation() {
        if (mMapLocationManager.isMyLocationEnabled()) { //현재 위치를 탐색 중인지 확인
            if (!mMapView.isAutoRotateEnabled()) { //지도 회전기능 활성화 상태 여부 확인
                mMyLocationOverlay.setCompassHeadingVisible(true); //나침반 각도 표시
                mMapCompassManager.enableCompass(); //나침반 모니터링 시작
                mMapView.setAutoRotateEnabled(true, false); //지도 회전기능 활성화
            }
            mMapView.invalidate();
        } else { //현재 위치를 탐색 중이 아니면
            Boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(false); //현재 위치 탐색 시작
            if (!isMyLocationEnabled) { //위치 탐색이 불가능하면
                Toast.makeText(GameActivity.this, "Please enable a My Location source in system settings",
                        Toast.LENGTH_LONG).show();
                Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(goToSettings);
                return;
            }
        }
    }

    private void stopMyLocation() {
        mMapLocationManager.disableMyLocation(); //현재 위치 탐색 종료
        if (mMapView.isAutoRotateEnabled()) { //지도 회전기능이 활성화 상태라면
            mMyLocationOverlay.setCompassHeadingVisible(false); //나침반 각도표시 제거
            mMapCompassManager.disableCompass(); //나침반 모니터링 종료
            mMapView.setAutoRotateEnabled(false, false); //지도 회전기능 중지
        }
    }

    private void InitVariables(){
        btnTeamChat = (Button) findViewById(R.id.btnTeamChat);
        btnGlobalChat = (Button) findViewById(R.id.btnGlobalChat);
        btnUserlist = (Button) findViewById(R.id.btnUserlist);

        // 남은 시간 출력해주는 textView -> 추후 Service에서 시간이 줄어들도록 구현해야함
        tvTime = (TextView) findViewById(R.id.tvTime);
    }

    private void InitSettings(){
        lvUsrList = (ListView)findViewById(R.id.game_activity_userlist);
        usrContainer = (FrameLayout)findViewById(R.id.container_activity_game);

        lvUsrList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        //lvNavList.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        InitVariables();
        InitSettings();


        // 지도 출력
        MapContainer = (LinearLayout) findViewById(R.id.nmap);
        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID);
        mMapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        MapContainer.addView(mMapView);
        mMapView.setClickable(true);
        mMapView.setOnMapStateChangeListener(this);
        mMapView.setOnMapViewTouchEventListener(this);
        mMapController = mMapView.getMapController();

        // 마커 생성
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        testOverlayMaker();

        // 현재 위치 표시
        //위치 관리 메니저 객체 생성
        mMapLocationManager = new NMapLocationManager(this);
         //현재 위치 변경 시 호출되는 콜백 인터페이스를 설정한다.
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
         //NMapMyLocationOverlay 객체 생성
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager,
                mMapCompassManager);
        startMyLocation(); //내 위치 찾기 함수 호출

        btnTeamChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, TeamChatActivity.class);
                startActivity(intent);
            }
        });

        btnGlobalChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, GlobalChatActivity.class);
                startActivity(intent);
            }
        });

        btnUserlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show all players on the map
            }
        });

/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.game_activity_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer) {

            /** Called when a drawer has settled in a completely closed state. *
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. *
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

                */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*
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
*/
}
