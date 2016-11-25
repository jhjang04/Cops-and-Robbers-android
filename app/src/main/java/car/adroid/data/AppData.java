package car.adroid.data;

import java.util.ArrayList;

/**
 * Created by jhjang04 on 2016-11-24.
 */

public class AppData {

    public static double LATITUDE;
    public static double LONGITUDE;
    public static int ROOM_NO;
    public static String NICK_NAME;
    public static int TEAM;
    public static ArrayList<User> COPS = new ArrayList<User>();
    public static ArrayList<User> ROBBERS = new ArrayList<User>();

    public static ArrayList<User> getCops() {
        return COPS;
    }

    public static ArrayList<User> getRobbers() {
        return ROBBERS;
    }



    public static double getLongitude() {
        return LONGITUDE;
    }

    public static void setLongitude(double LONGITUDE) {
        AppData.LONGITUDE = LONGITUDE;
    }

    public static double getLatitude() {
        return LATITUDE;
    }

    public static void setLatitude(double LATITUDE) {
        AppData.LATITUDE = LATITUDE;
    }

    public static int getRoomNo() {
        return ROOM_NO;
    }

    public static void setRoomNo(int roomNo) {
        ROOM_NO = roomNo;
    }

    public static String getNickName() {
        return NICK_NAME;
    }

    public static void setNickName(String nickName) {
        NICK_NAME = nickName;
    }

}
