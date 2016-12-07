package car.adroid.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import car.adroid.conn.HttpConnector;
import car.adroid.data.AppData;
import car.adroid.util.SimpleLogger;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class LoginActivity extends FragmentActivity {

    private Context mContext = this;
    private Button btnNext;
    private EditText txtRoomNumber,txtName, txtPwd;
    private TextView tvRoomNumber , tvMessage;
    private RadioButton rdCreate, rdJoin;

    //private User user;
    private String userName;
    private String roomNum;
    private String roomPwd;
    private KeyListener pwdKeyListener;

    // 방 번호는 1~10000 사이의 숫자
    // 캐릭터가 섞여있으면 false
    // 10000이 넘는 숫자일 경우 false
    private Boolean IsRoomNumProper(){
        if ( roomNum.length() == 0 ) {
            // 아닐경우에 대한 적절한 처리
            return false;
        } else {
            return true;
        }
    }
    // 공백일 경우 false
    // 방 비밀번호와 일치하지 않을경우 false
    private Boolean IsPwdProper(){
        if ( roomPwd.length() == 0 ) {
            // 아닐경우에 대한 적절한 처리
            return false;
        } else {
            return true;
        }
    }

    // 이미 존재하는 유저의 닉네임일 경우 false
    private Boolean IsUserNameProper(){
        if (userName.length() == 0 ) {
            // 아닐경우에 대한 적절한 처리
            return false;
        } else {
            return true;
        }
    }

    // 값을 받아온다.
    private void SetUserInfo(){
        userName = txtName.getText().toString();
        roomNum = txtRoomNumber.getText().toString();
        roomPwd = txtPwd.getText().toString();
    }

    private void InitVariables(){
        // mapping variables with id
        // editText for getting userInfo
        txtName = (EditText)findViewById(R.id.txtName);
        txtRoomNumber = (EditText)findViewById(R.id.txtRoomNum);
        txtPwd = (EditText)findViewById(R.id.txtPwd);

        // button for next activity
        btnNext = (Button)findViewById(R.id.btnNext);

        // Radio Button for 2 mode (create room / join room)
        rdCreate = (RadioButton)findViewById(R.id.rdCreate);
        rdJoin = (RadioButton)findViewById(R.id.rdJoin);

        //textview
        tvRoomNumber = (TextView) findViewById(R.id.tvRoomNumber);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }

    private void InitSettings(){
        // hide keyboard when touching background
        findViewById(R.id.activity_login).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        rdCreate.setChecked(true);
        txtRoomNumber.setVisibility(View.GONE);
        tvRoomNumber.setVisibility(View.GONE);
        //pwdKeyListener = txtPwd.getKeyListener();

        //txtPwd.setKeyListener(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitVariables();
        InitSettings();

        // After creating a room, server will give room number itself, so no need to enter roomNum
        rdCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(rdCreate.isChecked()){
                    txtRoomNumber.setText(null);
                    txtPwd.setText(null);
                    txtRoomNumber.setVisibility(View.GONE);
                    tvRoomNumber.setVisibility(View.GONE);
                }
//                txtRoomNumber.setText(null);
                //txtRoomNumber.setKeyListener(null);
            }
        });

        // enable room num editText
        rdJoin.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(rdJoin.isChecked()){
                    txtRoomNumber.setText(null);
                    txtPwd.setText(null);
                    txtRoomNumber.setVisibility(View.VISIBLE);
                    tvRoomNumber.setVisibility(View.VISIBLE);
                }
//                txtRoomNumber.setKeyListener(pwdKeyListener);
                //txtRoomNumber.setKeyListener(null);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SetUserInfo();
                if(userName== null ||userName.equals("")){
                            tvMessage.setText("input user name");
//                            Toast.makeText(mContext , "input user name", Toast.LENGTH_LONG).show();
                    return;
                }
                if(roomPwd == null || roomPwd.equals("")){
                            tvMessage.setText("input password");
//                            Toast.makeText(mContext , "input password", Toast.LENGTH_LONG).show();
                    return;
                }

                if(rdJoin.isChecked() && (roomNum == null || roomNum.equals("")) ){
                            tvMessage.setText("input room number");
//                            Toast.makeText(mContext , "input room number", Toast.LENGTH_LONG).show();
                    return;
                }

                new Thread() {
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, TeamActivity.class);
                        intent.putExtra("name", userName);
                        AppData appData = AppData.getInstance(mContext);

                        JSONObject response = null;
                        String result;
                        int room_id = 0;
                        int user_no = 0;
                        int team = 0;
                        try {
                            if (rdCreate.isChecked()) {   //if make Room
                                response = new HttpConnector().makeRoom(roomPwd, userName);
                                result = response.getString("result");
                                room_id = response.getInt("room_id");
                                user_no = response.getInt("user_no");
                                team = response.getInt("team");
                            } else {    //if join Room
                                room_id = Integer.parseInt(roomNum);
                                response = new HttpConnector().joinRoom(room_id, roomPwd, userName);
                                result = response.getString("result");
                                if(result.equals("PASS")){
                                    user_no = response.getInt("user_no");
                                    team = response.getInt("team");
                                }
                                else{
                                    Toast.makeText(mContext,"방 정보가 없습니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                            appData.updateGameBaseInfo(mContext, room_id, user_no, userName , team);
                            startActivity(intent);
                        } catch (JSONException e) {
                            SimpleLogger.info(LoginActivity.this, e.toString());
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
}
