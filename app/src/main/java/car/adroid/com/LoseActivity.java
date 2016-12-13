package car.adroid.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import car.adroid.service.InGameHttpService;

public class LoseActivity extends FragmentActivity {

    private Context mContext = this;
    private Button btnTeamChat, btnGlobalChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        btnTeamChat = (Button) findViewById(R.id.btnTeamChat);
        btnGlobalChat = (Button) findViewById(R.id.btnGlobalChat);

        btnTeamChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TeamChatActivity.class);
                startActivity(intent);
            }
        });

        btnGlobalChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , GlobalChatActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext() , InGameHttpService.class));
        super.onDestroy();
    }
}
