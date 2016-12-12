package car.adroid.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import car.adroid.config.AppConfig;
import car.adroid.service.TeamSelectHttpService;
import car.adroid.util.ChatArrayAdapter;
import car.adroid.util.ChatMessage;
import car.adroid.util.ProgressThread;

public class TeamChatActivity extends FragmentActivity {

    Context mContext = this;

    private ChatArrayAdapter chatArrayAdapter;


    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private BroadcastReceiver mReceiver;


    Intent intent;
    private boolean side = false;

    private void InitVariables(){
        buttonSend = (Button) findViewById(R.id.btnTeamChat);
        listView = (ListView) findViewById(R.id.lvTeamChat);
        chatText = (EditText) findViewById(R.id.tvTeamMessage);


    }

    private void InitSettings(){
        // hide keyboard when touching background
        listView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        // hide keyboard when touching background
        findViewById(R.id.bgTeamChat).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConfig.BROADCAST_ACTION_IN_GAME)) {
                    String result = intent.getStringExtra("result");
                    if(result.equals("ING")){

                    }
                    else if(result.equals("WIN")) {
//                        startActivity(new Intent(mContext, GameActivity.class));
                        stopService(new Intent(mContext , TeamSelectHttpService.class));
                        finish();
                    }
                    else if(result.equals("LOSE")){
                        stopService(new Intent(mContext , TeamSelectHttpService.class));
                        finish();
                    }
                }
            }
        };
    }


    private void sendChat(){
        new ProgressThread(mContext){
            @Override
            public void run() {

            }
        }.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_chat);

        InitVariables();
        InitSettings();

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });


        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean IsEmptyText(){
        if(chatText.getText().toString().isEmpty() || !chatText.getText().toString().matches(".*\\w.*") )
        {
            chatText.setText("");
            return true;
        }


        return false;
    }

    private boolean sendChatMessage(){

        if(!IsEmptyText())
        {
            chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString(),"testSender"));
            chatText.setText("");
            side = !side;
            return true;
        }

        return false;
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
