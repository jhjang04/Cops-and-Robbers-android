package car.adroid.config;

/**
 * Created by jjh on 2016-11-24.
 */

public class AppConfig {
    public static final boolean DEBUG = true;

    public static final String SERVER_TYPE = "HTTP";
    public static final String HTTP_URL= "http://163.180.173.169:9999/car";
    public static final int HTTP_REQUEST_REPEAT_INTERVAL = 500;

    public static final int LOCATION_RECIVE_MILISECONDS = 1000;
    public static final int LOCATION_RECEIVE_DISTANCE = 10;

    public static final int LIMIT_WARNING_COUNT = 5;
    public static final float DISTANCE_CATCHED = 30;

    public static final String BROADCAST_ACTION_TEAM_SELECT = "car.adroid.com.TeamSelectService.BROADCAST";
    public static final String BROADCAST_ACTION_IN_GAME = "car.adroid.com.inGameService.BROADCAST";

}
