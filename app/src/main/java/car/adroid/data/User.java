package car.adroid.data;

import org.json.JSONException;
import org.json.JSONObject;

import car.adroid.config.AppConfig;
import car.adroid.util.DateUtil;

/**
 * Created by jjh on 2016-11-24.
 */

public class User {
    public static final int TEAM_COP = 1;
    public static final int TEAM_ROBBER = 2;
    public static final int STATE_ALIVE = 1;
    public static final int STATE_CATCHED= 2;
    public static final int READY_STATUS_READY = 1;
    public static final int READY_STATUS_NOT_READY = 2;

    private int mUserNo = 0;
    private String mNickName = "";

    private int mTeam = 0;
    private int mReadyStatus = READY_STATUS_NOT_READY;
    private int mState = 0;

    private double mLatitude = 0;
    private double mLongitude = 0;
    private String mTeamSelectTime = "";
    private String mLastAccess = "";

    public User(){
        mLatitude = 0;
        mLongitude = 0;
    }
    public User(User user){
        this.cloneUser(user);
    }
    public User(JSONObject jsonUser){
        this.getUserInfoAtJsonObject(jsonUser);
    }

    public void cloneUser(User user){
        mUserNo = user.getUserNo();
        mNickName = user.getNickName();
        mTeam = user.getTeam();
        mReadyStatus = user.getReadyStatus();
        mState = user.getState();
        mLatitude = user.getLatitude();
        mLongitude = user.getLongitude();
    }

    public void getUserInfoAtJsonObject(JSONObject jsonUser){
        try {
            mUserNo = jsonUser.getInt("user_no");
            mNickName = jsonUser.getString("nickname");
            mTeam = jsonUser.getInt("team");
            mReadyStatus = jsonUser.getInt("ready_status");
            mState = jsonUser.getInt("state");
            mTeamSelectTime = jsonUser.getString("team_select_time");
            mLastAccess= jsonUser.getString("last_access");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mLatitude = jsonUser.getDouble("latitude");
            mLongitude = jsonUser.getDouble("longitude");
        } catch (JSONException e) {
            mLatitude = 0;
            mLongitude = 0;
        }
    }


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

    public int getReadyStatus() {
        return mReadyStatus;
    }

    public void setReadyStatus(int mReadyStatus) {
        this.mReadyStatus = mReadyStatus;
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

    public String getTeamSelectTime() { return mTeamSelectTime; }

    public void setTeamSelectTime(String mTeamSelectTime) {
        this.mTeamSelectTime = mTeamSelectTime;
    }

    public String getLastAccess() {
        return mLastAccess;
    }

    public void setLastAccess(String mLastAccess) {
        this.mLastAccess = mLastAccess;
    }

    public boolean isValid(){
        try {
            return DateUtil.getTimeGapMiliseconds(DateUtil.getCurrent() , mLastAccess) < AppConfig.LIMIT_ACCESS_MIILISECONDS;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
