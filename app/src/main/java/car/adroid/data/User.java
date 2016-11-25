package car.adroid.data;

import java.util.Date;

/**
 * Created by jjh on 2016-11-24.
 */

public class User {
    public static int TEAM_COP = 1;
    public static int TEAM_ROBBER = 2;
    public static int STATE_ALIVE = 1;
    public static int STATE_CATCHED= 2;

    public int team;
    public int state;

    public int userNo;
    public String nick;

    public double latitude;
    public double longitude;

    public Date lastAccess;

}
