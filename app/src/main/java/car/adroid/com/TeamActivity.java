package car.adroid.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class TeamActivity extends AppCompatActivity {

    private Button btnCop, btnRobber,btnStart;
    private ListView listCop, listRobber;

    private Boolean IsListFocused(){
        if(listCop.isFocused())
            return true;
        else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        btnCop = (Button)findViewById(R.id.btnCop);
        btnRobber = (Button)findViewById(R.id.btnRobber);
        btnStart = (Button)findViewById(R.id.btnStart);
        listCop = (ListView)findViewById(R.id.listCop);
        listRobber = (ListView)findViewById(R.id.listRobber);

        btnCop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //if(IsListFocused())
                // 리스트의 아이템 이동
            }
        });

        btnRobber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //if(IsListFocused())
                // 리스트의 아이템 이동
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }
}
