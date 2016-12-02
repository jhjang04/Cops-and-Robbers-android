package car.adroid.data;

/**
 * Created by jjh on 2016-11-24.
 */

public class User {
    public static final int TEAM_COP = 1;
    public static final int TEAM_ROBBER = 2;
    public static final int STATE_ALIVE = 1;
    public static final int STATE_CATCHED= 2;


    private int mUserNo;
    private String mNickName;

    private int mTeam;
    private int mState;

    private double mLatitude;
    private double mLongitude;

    public int getUserNo() {
        return mUserNo;
    }

    public void setUserNo(int mUserNo) {
        this.mUserNo = mUserNo;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public int getTeam() {
        return mTeam;
    }

    public void setTeam(int mTeam) {
        this.mTeam = mTeam;
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
