package car.adroid.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import car.adroid.com.GameActivity;
import car.adroid.com.R;
import car.adroid.data.User;

/**
 * Created by mbj94 on 2016-12-07.
 */

public class UserListAdapter extends ArrayAdapter<GameActivity.TestUser> {
    private TextView tvUser;
    private List<GameActivity.TestUser> userList = new ArrayList<GameActivity.TestUser>();

    @Override
    public void add(GameActivity.TestUser user) {
        userList.add(user);
        super.add(user);
    }

    public UserListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.textview_user, parent, false);
        }
        GameActivity.TestUser user = getItem(position);
        tvUser = (TextView)row.findViewById(R.id.tvUserState);
        tvUser.setText("test");
        //tvUser.setText(user.getNickName());

        if(user.getTeam() == 1)
            tvUser.setTextColor(Color.BLUE);
        else{
            if(user.getState() == 2)
                tvUser.setTextColor(Color.GRAY);
            else
                 tvUser.setTextColor(Color.RED);
        }
        return row;
    }
}
