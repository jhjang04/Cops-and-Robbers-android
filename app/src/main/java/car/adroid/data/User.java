package car.adroid.data;

/**
 * Created by jjh on 2016-11-24.
 */

public class User {
    public static final int TEAM_COP = 1;
    public static final int TEAM_ROBBER = 2;
    public static final int STATE_ALIVE = 1;
    public static final int STATE_CATCHED= 2;


    public int userNo;
    public String nick;

    public int team;
    public int state;

    public double latitude;
    public double longitude;

}
