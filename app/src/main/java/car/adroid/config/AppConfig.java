package car.adroid.config;

/**
 * Created by jjh on 2016-11-24.
 */

public class AppConfig {
    public static final String CLIENT_ID = "1Ure4ugOivjE9hHXmHld";//애플리케이션 클라이언트 아이디값";
    public static final String CLIENT_SECRET = "oJoJK6OIFF";//애플리케이션 클라이언트 시크릿값";
    public static final boolean DEBUG = true;
    public static final int LOGGER_SHOW_DELAY = 5000;

    public static final String SERVER_TYPE = "HTTP";
    public static final String HTTP_URL= "http://163.180.173.169:9999/Cops_and_Robbers";
//    public static final String HTTP_URL= "http://192.168.0.19/car";
    public static final int HTTP_REQUEST_REPEAT_INTERVAL = 500;

    public static final int LOCATION_RECIVE_MILISECONDS = 1000;
    public static final int LOCATION_RECEIVE_DISTANCE = 10;

    public static final int LIMIT_WARNING_COUNT = 5;
    public static final float SPEED_CATCHED = 2.5f;
    public static final float DISTANCE_CATCHED = 30;

    public static final String BROADCAST_ACTION_TEAM_SELECT = "car.android.com.TeamSelectService.BROADCAST";
    public static final String BROADCAST_ACTION_IN_GAME = "car.android.com.inGameService.BROADCAST";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final long LIMIT_ACCESS_MIILISECONDS = 5000;

}
