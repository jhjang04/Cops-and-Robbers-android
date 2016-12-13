package car.adroid.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import car.adroid.config.AppConfig;
import car.adroid.conn.HttpConnector;
import car.adroid.data.AppDBHelper;
import car.adroid.data.AppData;
import car.adroid.util.ChatArrayAdapter;
import car.adroid.util.ChatMessage;
import car.adroid.util.ProgressThread;

public class GlobalChatActivity extends FragmentActivity {

    private Context mContext = this;

    private ChatArrayAdapter chatArrayAdapter;
//    private RelativeLayout layout;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;

    private BroadcastReceiver mReceiver = null;
    private Handler mHandler = null;

    private boolean mIsDown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);

        chatText = (EditText) findViewById(R.id.tvGlobalMessage);
        buttonSend = (Button) findViewById(R.id.btnGlobalSend);
        listView = (ListView) findViewById(R.id.lvGlobalChat);
//        layout = (RelativeLayout) findViewById(R.id.bgGlobalChat);

         //hide keyboard when touching background
        listView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        // hide keyboard when touching background
        findViewById(R.id.bgGlobalChat).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });


        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        chatText.setMovementMethod(null);
        listView.setAdapter(chatArrayAdapter);
        chatText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                listView.setFocusable(!hasFocus);
//                layout.setFocusable(!hasFocus);
                //findViewById(R.id.form).setFocusable(!hasFocus);
            }
        });


//        chatText.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    return sendChatMessage();
//                }
//                return false;
//            }
//        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mIsDown = false;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if(listView.getSelectedItemPosition() == (chatArrayAdapter.getCount()-1)){
                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                }
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConfig.BROADCAST_ACTION_IN_GAME)) {
                    refreshScreen();
                }
            }
        };

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listView.setSelection(chatArrayAdapter.getCount() - 1);
                refreshScreen();
            }
        };
        refreshScreen();
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
            mIsDown = true;
            final String text = chatText.getText().toString();
            new ProgressThread(mContext){
                @Override
                public void run() {
                    try {
                        AppData data = AppData.getInstance(getApplicationContext());
                        JSONObject response = new HttpConnector().sendChat(data.getRoomId() , data.getTeam() , 0 , data.getUserNo() , data.getNickName() , text , data.getLastChatIdx() , data.getLastTeamChatIdx());
                        JSONArray chatList = response.getJSONArray("chatList");
                        JSONArray teamChatList = response.getJSONArray("teamChatList");
                        int lastChatIdx = response.getInt("lastChatIdx");
                        int lastTeamChatIdx = response.getInt("lastTeamChatIdx");

                        data.setLastChatIdx(lastChatIdx);
                        data.setLastTeamChatIdx(lastTeamChatIdx);

                        SQLiteDatabase db = new AppDBHelper(mContext).getWritableDatabase();



                        for(int i=0 ; i<chatList.length() ; i++){
                            JSONObject chatObj = chatList.getJSONObject(i);
                            data.insertChatData(db
                                    , chatObj.getInt("idx")
                                    , chatObj.getInt("chat_flag")
                                    , chatObj.getInt("team")
                                    , chatObj.getInt("user_no")
                                    , chatObj.getString("nickname")
                                    , chatObj.getString("wr_time")
                                    , chatObj.getString("text"));
                        }

                        for(int i=0 ; i<teamChatList.length() ; i++){
                            JSONObject chatObj = teamChatList.getJSONObject(i);
                            data.insertChatData(db
                                    , chatObj.getInt("idx")
                                    , chatObj.getInt("chat_flag")
                                    , chatObj.getInt("team")
                                    , chatObj.getInt("user_no")
                                    , chatObj.getString("nickname")
                                    , chatObj.getString("wr_time")
                                    , chatObj.getString("text"));
                        }

                        mHandler.sendEmptyMessage(0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            chatText.setText("");
            return true;
        }
        return false;
    }

    public void refreshScreen(){

        AppData data = AppData.getInstance(getApplicationContext());
        ArrayList<ChatMessage> chatList = data.getChatList();


        if(mIsDown)
            listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        else{
            listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
        }
        chatArrayAdapter.clear();
        chatArrayAdapter.addAll(chatList);
        chatArrayAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.BROADCAST_ACTION_IN_GAME);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
