package car.adroid.config;

/**
 * Created by jjh on 2016-11-24.
 */

public class AppConfig {
    public static final String CLIENT_ID = "1Ure4ugOivjE9hHXmHld";//애플리케이션 클라이언트 아이디값";
    public static final String CLIENT_SECRET = "oJoJK6OIFF";//애플리케이션 클라이언트 시크릿값";
    public static final boolean DEBUG = false;
    public static final int LOGGER_SHOW_DELAY = 5000;

    public static final String SERVER_TYPE = "HTTP";
//    public static final String HTTP_URL= "http://163.180.173.169:9999/Cops_and_Robbers";
    public static final String HTTP_URL= "http://ec2-52-78-178-33.ap-northeast-2.compute.amazonaws.com/car";
//    public static final String HTTP_URL= "http://192.168.0.81/car";
    public static final int HTTP_REQUEST_REPEAT_INTERVAL = 500;

    public static final int LOCATION_RECIVE_MILISECONDS = 1000;
    public static final int LOCATION_RECEIVE_DISTANCE = 10;

    public static final int LIMIT_WARNING_COUNT = 5;
    public static final int CATCHED_MILLISECONDS = 15000;
    public static final float SPEED_CATCHED = 2.5f;
    public static final float DISTANCE_CATCHED = 30f;
    public static final float DISTANCE_SHOW_MIN = 100f;
    public static final float DISTANCE_SHOW_MAX = 200f;
    public static final int WARNING_VIBERATE_MILISECONDS = 500;

    public static final String BROADCAST_ACTION_TEAM_SELECT = "car.android.com.TeamSelectService.BROADCAST";
    public static final String BROADCAST_ACTION_IN_GAME = "car.android.com.inGameService.BROADCAST";
    public static final String BROADCAST_ACTION_IN_GAME_LOCAL = "car.android.com.inGameLocalService.BROADCAST";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final long LIMIT_ACCESS_MIILISECONDS = 5000;

    public static final double PRISON_LATITUDE = 37.24736496;
    public static final double PRISON_LONGITUDE = 127.07819944;




}

