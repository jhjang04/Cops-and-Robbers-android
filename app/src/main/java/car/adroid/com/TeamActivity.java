package car.adroid.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import car.adroid.config.AppConfig;
import car.adroid.data.AppData;
import car.adroid.data.User;
import car.adroid.service.TeamSelectHttpService;
import car.adroid.util.DateUtil;
import car.adroid.util.ProgressThread;
import car.adroid.util.TeamListAdapter;

public class TeamActivity extends FragmentActivity {

    private Context mContext= this;
    private Button btnToCop, btnToRobber,btnStart;
    private ListView lvCop, lvRobber;
    private TextView tvRoomInfo;

    ArrayAdapter<User> adapterCop, adapterRobber;
    BroadcastReceiver mReceiver = null;

    private void InitList(){
        NotifyChange();
    }

    private void NotifyChange(){
        AppData data = AppData.getInstance(getApplicationContext());
        adapterCop.clear();
        adapterRobber.clear();

        adapterCop.addAll(data.getCops());
        adapterRobber.addAll(data.getRobbers());

        adapterCop.notifyDataSetChanged();
        adapterRobber.notifyDataSetChanged();
    }


    private void InitVariables(){
        btnToCop = (Button)findViewById(R.id.btnCop);
        btnToRobber = (Button)findViewById(R.id.btnRobber);
        btnStart = (Button)findViewById(R.id.btnStart);
        lvCop = (ListView)findViewById(R.id.listCop);
        lvRobber = (ListView)findViewById(R.id.listRobber);
        tvRoomInfo = (TextView)findViewById(R.id.tv_team_room_info);
        AppData data = AppData.getInstance(getApplicationContext());

        String roomInfo  =  "" + data.getRoomId() + " / " + getIntent().getStringExtra("pwd");
        tvRoomInfo.setText(roomInfo);
    }

    private void InitSettings(){
        adapterCop = new TeamListAdapter(getApplicationContext(), R.id.tvTeam);
        adapterRobber = new TeamListAdapter(getApplicationContext(), R.id.tvTeam);
        lvCop.setAdapter(adapterCop);
        lvRobber.setAdapter(adapterRobber);


        btnToCop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AppData appData = AppData.getInstance(getApplicationContext());
                if(appData.getTeam() == User.TEAM_COP || appData.getReadyStatus() == User.READY_STATUS_READY){
                    return;
                }

                stopService(new Intent(mContext , TeamSelectHttpService.class));
                appData.updateTeam(User.TEAM_COP);
                startService(new Intent(mContext , TeamSelectHttpService.class));
                NotifyChange();
            }
        });

        btnToRobber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AppData appData = AppData.getInstance(getApplicationContext());
                if(appData.getTeam() == User.TEAM_ROBBER || appData.getReadyStatus() == User.READY_STATUS_READY){
                    return;
                }
                stopService(new Intent(mContext , TeamSelectHttpService.class));
                appData.updateTeam(User.TEAM_ROBBER);
                startService(new Intent(mContext , TeamSelectHttpService.class));
                NotifyChange();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AppData appData = AppData.getInstance(getApplicationContext());
                appData.ready();
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConfig.BROADCAST_ACTION_TEAM_SELECT)) {
                    String result = intent.getStringExtra("result");
                    if(result.equals("WAIT")){
                        NotifyChange();
                    }
                    if(result.equals("START")) {
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                startActivity(new Intent(mContext, GameActivity.class));
                                finish();
                            }
                        };
                        new ProgressThread(mContext){
                            @Override
                            public void run() {
                                try {
                                    AppData data = AppData.getInstance(getApplicationContext());
                                    Date startTime = DateUtil.getDate(data.getStartTime());
                                    Long sleepTime = startTime.getTime() - new Date().getTime();
                                    if(sleepTime > 0){
                                        sleep(sleepTime);
                                    }
                                    handler.sendEmptyMessage(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        stopService(new Intent(mContext , TeamSelectHttpService.class));
                    }
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        InitVariables();
        InitSettings();
        InitList();
        startService(new Intent(mContext , TeamSelectHttpService.class));

    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.BROADCAST_ACTION_TEAM_SELECT);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
