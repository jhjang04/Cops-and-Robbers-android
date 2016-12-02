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

public class TeamActivity extends FragmentActivity {

    private Button btnToCop, btnToRobber,btnStart;
    private ListView lvCop, lvRobber;
    ArrayList<String> listCop,listRobber;
    ArrayAdapter<String> adapterCop, adapterRobber;
    private String userName = "test";



    private void InitList(){
        listCop.add(userName);
        adapterCop.notifyDataSetChanged();

        listRobber.add("testItem2");
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

        adapterCop.notifyDataSetChanged();
        adapterRobber.notifyDataSetChanged();
    }

    private void InitVariables(){
        btnToCop = (Button)findViewById(R.id.btnCop);
        btnToRobber = (Button)findViewById(R.id.btnRobber);
        btnStart = (Button)findViewById(R.id.btnStart);
        lvCop = (ListView)findViewById(R.id.listCop);
        lvRobber = (ListView)findViewById(R.id.listRobber);

        listCop = new ArrayList<String>();
        listRobber = new ArrayList<String>();
        adapterCop = new ArrayAdapter<String>(this, R.layout.listview_text_layout, listCop);
        adapterRobber = new ArrayAdapter<String>(this, R.layout.listview_text_layout,listRobber);
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
        InitList();

        btnToCop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 리스트의 아이템 이동
                if(IsMovable(listRobber))
                    MoveToList(listRobber,listCop);
            }
        });

        btnToRobber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 리스트의 아이템 이동
                if(IsMovable(listCop))
                    MoveToList(listCop,listRobber);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

       // finish();
    }
}
