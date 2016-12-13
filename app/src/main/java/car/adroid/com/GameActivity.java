package car.adroid.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Date;

import car.adroid.NMap.NMapPOIflagType;
import car.adroid.NMap.NMapViewerResourceProvider;
import car.adroid.config.AppConfig;
import car.adroid.data.AppData;
import car.adroid.data.User;
import car.adroid.service.InGameHttpService;
import car.adroid.service.InGameLocalService;
import car.adroid.util.DateUtil;
import car.adroid.util.SimpleLogger;
import car.adroid.util.UserListAdapter;

public class GameActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener {

    private Context mContext = this;
    private Button btnTeamChat, btnGlobalChat, btnUserlist;
    private TextView tvTime;

    private NMapView mMapView = null;
    private NMapController mMapController = null;
    private LinearLayout MapContainer;


    private UserListAdapter userListAdapter ;
    private ListView lvUsrList;


    private DrawerLayout mDrawerLayout;
    private Date mDate = null;
    private Runnable mRunnable = null;
    private Handler mHandelr = null;

    private Date mBackButtonPressDate = null;


    //지도 위 오버레이 객체 드로잉에 필요한 리소스 데이터 제공 클래스
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    //오버레이 객체 관리 클래스
    private NMapOverlayManager mOverlayManager;
    //POI 아이템 선택 상태 변경 시 호출퇴는 콜백 인터페이스
//    private NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener;

    private NMapMyLocationOverlay mMyLocationOverlay; //지도 위에 현재 위치를 표시하는 오버레이 클래스
    private NMapLocationManager mMapLocationManager; //단말기의 현재 위치 탐색 기능 사용 클래스
    private NMapCompassManager mMapCompassManager; //단말기의 나침반 기능 사용 클래스

    private BroadcastReceiver mReceiver;

    private boolean mCatchShowed = false;



    @Override
    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) {
//            AppData data = AppData.getInstance(getApplication());

//            mMapController.setMapCenter(new NGeoPoint(data.getLongitude(), data.getLatitude()), 11);
        } else {
            SimpleLogger.debug(mContext , "onMapInitHandler: error=" + errorInfo.toString());
//            android.util.Log.e("NMAP", "onMapInitHandler: error=" + errorInfo.toString());
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

//    private void testOverlayMaker() { //오버레이 아이템 추가 함수
//        int markerAlly = NMapPOIflagType.ALLY; //마커 id설정
//        int markerOpponent = NMapPOIflagType.OPPONENT;
//        // POI 아이템 관리 클래스 생성(전체 아이템 수, NMapResourceProvider 상속 클래스)
//        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
//        poiData.beginPOIdata(2); // POI 아이템 추가 시작
//        poiData.addPOIitem(127.081667, 37.242222, "marker1", markerAlly, 0);
//        poiData.addPOIitem(127.081867, 37.242322, "marker2", markerOpponent, 0);
//
//        poiData.endPOIdata(); // POI 아이템 추가 종료
//        //POI data overlay 객체 생성(여러 개의 오버레이 아이템을 포함할 수 있는 오버레이 클래스)
//        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
////        poiDataOverlay.showAllPOIdata(0); //모든 POI 데이터를 화면에 표시(zomLevel)
//        //POI 아이템이 선택 상태 변경 시 호출되는 콜백 인터페이스 설정
////        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
//    }

    private void drawMakers(){
        int markerAlly = NMapPOIflagType.ALLY; //마커 id설정
        int markerOpponent = NMapPOIflagType.OPPONENT;
        // POI 아이템 관리 클래스 생성(전체 아이템 수, NMapResourceProvider 상속 클래스)

        AppData data = AppData.getInstance(getApplicationContext());
        ArrayList<User> cops = data.getCops();
        ArrayList<User> robbers = data.getRobbers();


        int markerAlly = data.getTeam() == User.TEAM_COP ? NMapPOIflagType.ALLY : NMapPOIflagType.OPPONENT; //마커 id설정
        int markerOpponent = data.getTeam() == User.TEAM_ROBBER ? NMapPOIflagType.ALLY : NMapPOIflagType.OPPONENT; //마커 id설정

        NMapPOIdata poiData = new NMapPOIdata(allys.size() + targetEnemys.size(), mMapViewerResourceProvider);
        poiData.removeAllPOIdata();
        poiData.beginPOIdata(cops.size() + robbers.size() - 1); // POI 아이템 추가 시작

        ///////////// test///////
        poiData.addPOIitem(127.081667, 37.242222, "", markerPrison, 0);
        //////////////////////


        for(int i=0 ; i<cops.size() ; i++){
            User user = cops.get(i);
            if(user.getUserNo() == data.getUserNo()){
        poiData.beginPOIdata(allys.size() + targetEnemys.size() - 1); // POI 아이템 추가 시작
        for(int i=0 ; i<allys.size() ; i++) {
            User user = allys.get(i);
            if(user.getUserNo() == data.getUserNo()) {
                continue;
            }
            poiData.addPOIitem(user.getLongitude(), user.getLatitude(), "", markerAlly, 0);
        }
        for(int i=0 ; i<targetEnemys.size() ; i++){
            User user = targetEnemys.get(i);
            poiData.addPOIitem(user.getLongitude(), user.getLatitude() , "" , markerOpponent, 0);
        }

        poiData.endPOIdata(); // POI 아이템 추가 종료
        //POI data overlay 객체 생성(여러 개의 오버레이 아이템을 포함할 수 있는 오버레이 클래스)
        mOverlayManager.clearOverlays();
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);



//        poiDataOverlay.showAllPOIdata(0); //모든 POI 데이터를 화면에 표시(zomLevel)
        //POI 아이템이 선택 상태 변경 시 호출되는 콜백 인터페이스 설정
//        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }



    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener =
            new NMapLocationManager.OnLocationChangeListener() { //위치 변경 콜백 인터페이스 정의
                //위치가 변경되면 호출
                @Override
                public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
                    if (mMapController != null) {
                        mMapController.animateTo(myLocation); //지도 중심을 현재 위치로 이동
                    }
                    Date now = new Date();
                    AppData data = AppData.getInstance(getApplicationContext());

                    if(mDate != null){
                        float[] moveDistance = new float[3];
                        Long moveMiliseconds = now.getTime() - mDate.getTime();
                        Location.distanceBetween(data.getLatitude() , data.getLongitude() , myLocation.getLatitude() , myLocation.getLongitude(), moveDistance);
                        float speed = moveDistance[0] * 1000 / (float)moveMiliseconds ;
                        data.setSpeed(speed);
                    }
                    mDate = now;

                    data.updateLocalLocation(myLocation.getLatitude() , myLocation.getLongitude());


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
//                mMapView.setAutoRotateEnabled(true, false); //지도 회전기능 활성화
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

        // 유저리스트 사이드바
        mDrawerLayout = (DrawerLayout) findViewById(R.id.game_activity_drawer);
    }

    private void InitSettings(){
        lvUsrList = (ListView)findViewById(R.id.game_activity_userlist);
        userListAdapter = new UserListAdapter(getApplicationContext(), R.id.tvUser);
        lvUsrList.setAdapter(userListAdapter);


        // 왼->오른쪽 스와이프로 사이드 바 여는 모드 잠금
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


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
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConfig.BROADCAST_ACTION_IN_GAME)) {
                    AppData data = AppData.getInstance(getApplicationContext());
                    String result = intent.getStringExtra("result");
                    ShowMarkersOnMap();
                    userListAdapter.clear();
                    userListAdapter.addAll(data.getCops());
                    userListAdapter.addAll(data.getRobbers());

                    if(result.equals("ING")) {

                    }
                    else if(result.equals("WIN")) {
//                        startActivity(new Intent(mContext, GameActivity.class));
                        startActivity(new Intent(mContext , WinActivity.class));
                        finish();
                    }
                    else if(result.equals("LOSE")){
                        stopService(new Intent(mContext , InGameHttpService.class));
                        stopService(new Intent(mContext , InGameLocalService.class));
                        startActivity(new Intent(mContext , LoseActivity.class));
                        finish();
                    }
                }
            }
        };

        mHandelr = new Handler();


        mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mHandelr.postDelayed(mRunnable , 1000);
                    AppData data = AppData.getInstance(getApplicationContext());
                    Date start = DateUtil.getDate(data.getStartTime());
                    Date now = new Date();
                    Long remainTime = (start.getTime() + (600L*1000L) - now.getTime())/1000;
                    String text = "" + (remainTime/3600) + ":" + ((remainTime%3600)/60) + ":" + remainTime%60;
                    tvTime.setText(text);

                    data.refreshState();
                    if(data.isViberate()) {
                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(AppConfig.WARNING_VIBERATE_MILISECONDS);
                        mCatchShowed = false;
                    }

                    if(data.getState() == User.STATE_CATCHED && !mCatchShowed){
                        mCatchShowed = true;
                        startActivity(new Intent(mContext , CatchedActivity.class));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mHandelr.post(mRunnable);
    }

    // 유저 리스트에 유저 추가
    // 유저 리스트 상태 변경시, User객체의 상태만 변경된 채로
    // userListAdapter.notifychanges하면 된다.
    private void InitUserList(){
//       userListAdapter.add(users[0]);
//        userListAdapter.add(users[1]);
//        userListAdapter.add(users[2]);
    }

    private void ShowNMap(){
        // 지도 출력
        MapContainer = (LinearLayout) findViewById(R.id.nmap);
        mMapView = new NMapView(this);
        mMapView.setClientId(AppConfig.CLIENT_ID);
        mMapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        MapContainer.addView(mMapView);
        mMapView.setClickable(true);
        mMapView.setOnMapStateChangeListener(this);
        mMapView.setOnMapViewTouchEventListener(this);
        mMapController = mMapView.getMapController();
    }

    private void ShowMarkersOnMap(){
        // 마커 생성
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
//        testOverlayMaker();
        drawMakers();
    }

    private void ShowMyCurrentLocation(){
        // 현재 위치 표시
        //위치 관리 메니저 객체 생성
        mMapLocationManager = new NMapLocationManager(this);
        //현재 위치 변경 시 호출되는 콜백 인터페이스를 설정한다.
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        //NMapMyLocationOverlay 객체 생성
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager,
                mMapCompassManager);
        startMyLocation(); //내 위치 찾기 함수 호출
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        InitVariables();
        InitSettings();
        InitUserList();
        ShowNMap();
        ShowMarkersOnMap();
        ShowMyCurrentLocation();
        startService(new Intent(mContext , InGameHttpService.class));
//        startService(new Intent(mContext , InGameLocalService.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.game_activity_drawer);
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            Date now = new Date();
            if(mBackButtonPressDate != null && (now.getTime() - mBackButtonPressDate.getTime()) < 3000 ){
                stopService(new Intent(mContext , InGameHttpService.class));
                stopService(new Intent(mContext , InGameLocalService.class));
                super.onBackPressed();
            }
            else{
                mBackButtonPressDate = new Date();
                Toast.makeText(mContext , "한번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.BROADCAST_ACTION_IN_GAME);
        registerReceiver(mReceiver, filter);
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        mHandelr.removeCallbacks(mRunnable);
        super.onDestroy();

    }
}
