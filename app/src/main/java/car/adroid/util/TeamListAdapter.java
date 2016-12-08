package car.adroid.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import car.adroid.com.GameActivity;
import car.adroid.com.R;
import car.adroid.com.TestUser;

/**
 * Created by mbj94 on 2016-12-08.
 */

public class TeamListAdapter extends ArrayAdapter<TestUser> {
    private TextView tvTeam;
    private List<TestUser> userList = new ArrayList<TestUser>();

    @Override
    public void add(TestUser user) {
        userList.add(user);
        super.add(user);
    }

    public TeamListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.textview_team_select, parent, false);
        }
        TestUser user = getItem(position);
        tvTeam = (TextView)row.findViewById(R.id.tvTeam);
        tvTeam.setText(user.getNickName());


        // 방장일 경우? -> BLACK
        if(user.getState() == 1)
            tvTeam.setTextColor(Color.GREEN);
        else
            tvTeam.setTextColor(Color.RED);

        return row;
    }
}
