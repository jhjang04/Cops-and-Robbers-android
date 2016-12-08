package car.adroid.com;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import car.adroid.util.TeamListAdapter;

public class TeamActivity extends FragmentActivity {

    private Button btnToCop, btnToRobber,btnStart;
    private ListView lvCop, lvRobber;

    private TestUser[] users = new TestUser[]{new TestUser(1,1,"cop"), new TestUser(2,2,"robber")};
    ArrayAdapter<TestUser> adapterCop, adapterRobber;

    private String userName = "test";



    private void InitList(){
        adapterCop.add(users[0]);
        adapterRobber.add(users[1]);
        NotifyChange();

    }

    private void NotifyChange(){
        adapterCop.notifyDataSetChanged();
        adapterRobber.notifyDataSetChanged();
    }


    private Boolean IsMovable(ArrayList<String> list){
        if(list.contains(userName)){
            return true;
        }
        else{
            return false;
        }
    }

    private void MoveToList(ArrayList<String> srcList,ArrayList<String> dstList ){
        int idx = srcList.indexOf(userName);
        srcList.remove(idx);
        dstList.add(userName);

        NotifyChange();
    }

    private void InitVariables(){
        btnToCop = (Button)findViewById(R.id.btnCop);
        btnToRobber = (Button)findViewById(R.id.btnRobber);
        btnStart = (Button)findViewById(R.id.btnStart);
        lvCop = (ListView)findViewById(R.id.listCop);
        lvRobber = (ListView)findViewById(R.id.listRobber);

    }

    private void InitSettings(){
        adapterCop = new TeamListAdapter(getApplicationContext(), R.id.tvTeam);
        adapterRobber = new TeamListAdapter(getApplicationContext(), R.id.tvTeam);
        lvCop.setAdapter(adapterCop);
        lvRobber.setAdapter(adapterRobber);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        Intent intent = getIntent();
        userName = intent.getExtras().getString("name");

        InitVariables();
        InitSettings();
        InitList();

        btnToCop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /*
                // 리스트의 아이템 이동
                if(IsMovable(listRobber))
                    MoveToList(listRobber,listCop);
                    */
            }
        });

        btnToRobber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               /*
                // 리스트의 아이템 이동
                if(IsMovable(listCop))
                    MoveToList(listCop,listRobber);
                */
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
