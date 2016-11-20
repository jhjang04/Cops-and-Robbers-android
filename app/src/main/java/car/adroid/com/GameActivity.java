package car.adroid.com;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends FragmentActivity {

    private Button btnTeamChat, btnGlobalChat, btnChance;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnTeamChat = (Button)findViewById(R.id.btnTeamChat);
        btnGlobalChat = (Button)findViewById(R.id.btnGlobalChat);
        btnChance = (Button)findViewById(R.id.btnChance);
        tvTime = (TextView)findViewById(R.id.tvTime);

        btnTeamChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, TeamChat.class);
                startActivity(intent);
            }
        });

        btnGlobalChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, GlobalChat.class);
                startActivity(intent);
            }
        });

        btnChance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // show all players on the map
            }
        });

    }
}
