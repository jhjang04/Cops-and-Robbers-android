package car.adroid.com;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private Button btnNext;
    private EditText txtRoomNumber,txtName, txtPwd;
    private RadioButton rdCreate, rdJoin;

    private String userName;
    private String roomNum;
    private String roomPwd;
    private  KeyListener pwdKeyListener;
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
        if ( userName.length() == 0 ) {
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide keyboard when touching background
        findViewById(R.id.activity_main).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        InitVariables();

        rdCreate.setChecked(true);
        pwdKeyListener = txtPwd.getKeyListener();
        txtPwd.setKeyListener(null);


        // After creating a room, server will give pwd itself, so no need to enter pwd
        rdCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                txtPwd.setText(null);
                txtPwd.setKeyListener(null);
            }
        });

        // enable pwd editText
        rdJoin.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                txtPwd.setKeyListener(pwdKeyListener);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SetUserInfo();

                /*
                // 예외 상황에 맞게 toast
                // 방 안에 이미 존재하는 닉네임인지
                if(!IsUserNameProper()){

                }
                // 존재하는 방 번호를 입력했는지
                else if(!IsRoomNumProper()){

                }
                else if( !IsPwdProper() & rdJoin.isChecked()){

                }
                else{

                }
                */

                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                // intent에 Name을 담아 전달
                intent.putExtra("name", userName);
                startActivity(intent);
        }
        });
    }
}
