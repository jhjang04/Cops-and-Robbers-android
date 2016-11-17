package car.adroid.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnCreate, btnJoin;
    private EditText txtRoomNumber,txtName;
    private String roomNum;

    private Boolean IsRoomNumber(){
        if ( txtRoomNumber.getText().toString().length() == 0 ) {
            // 아닐경우에 대한 적절한 처리
            return false;
        } else {
            return true;
        }
    }

    private void GetRoomNumber(){
        roomNum = txtRoomNumber.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (EditText)findViewById(R.id.txtName);
        txtRoomNumber = (EditText)findViewById(R.id.txtRoomNum);
        btnCreate = (Button)findViewById(R.id.btnCreate);
        btnJoin = (Button)findViewById(R.id.btnJoin);

        btnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(IsRoomNumber())
                    GetRoomNumber();

                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                // intent에 Name을 담아 전달
                startActivity(intent);
        }
        });

        btnJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(IsRoomNumber())
                    GetRoomNumber();

                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                startActivity(intent);
            }
        });

    }
}
