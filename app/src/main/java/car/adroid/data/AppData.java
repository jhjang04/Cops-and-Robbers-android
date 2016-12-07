package car.adroid.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import car.adroid.config.AppConfig;

/**
 * Created by jhjang04 on 2016-11-24.
 */

public class AppData {

    private static AppData INSTANCE = null;
    private int mRoomId = 0;
    private int mUserNo = 0;
    private String mNickName = "";
    private double mLatitude = 0;
    private double mLongitude = 0;
    private int mTeam = 0;
    private int mReadyStatus = User.READY_STATUS_NOT_READY;
    private int mState = 0;

    private ArrayList<User> mCops = null;
    private ArrayList<User> mRobbers = null;
    private int mLastChatIdx = 0;
    private int mLastTeamChatIdx = 0;

    private int mWarnigCount = 0;
    private float mSpeed;
    private Map<Integer, Integer> mUserTeamMap = new HashMap<Integer,Integer>();
    private Map<Integer, Integer> mUserIdxMap = new HashMap<Integer,Integer>();

    public int getRoomId() {
        return mRoomId;
    }

    public int getUserNo() {
        return mUserNo;
    }

    public String getNickName() {
        return mNickName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public int getTeam() {
        return mTeam;
    }

    public int getReadyStatus() {
        return mReadyStatus;
    }

    public int getState() {
        return mState;
    }

    public ArrayList<User> getCops() {
        return mCops;
    }

    public ArrayList<User> getRobbers() {
        return mRobbers;
    }

    public int getLastChatIdx() {
        return mLastChatIdx;
    }

    public int getLastTeamChatIdx() {
        return mLastTeamChatIdx;
    }

    public static AppData getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE  = new AppData();
            INSTANCE.getData(context);
        }
        return INSTANCE;
    }

    public void getData(Context context){
        AppData data = getInstance(context);
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        getLocalInfoAtDB(db);
        getUsersInfoAtDB(db);
        db.close();
    }

    private void getLocalInfoAtDB(SQLiteDatabase db){

        String sql = "SELECT ROOM_ID , USER_NO , NICKNAME , TEAM , READY_STATUS , STATE , LATITUDE , LONGITUDE FROM LOCAL_INFO;";
        Cursor c = db.rawQuery(sql , null);

        if (c.moveToNext()) {
            this.mRoomId = c.getInt(0);
            this.mUserNo = c.getInt(1);
            this.mNickName = c.getString(2);
            this.mTeam = c.getInt(3);
            this.mReadyStatus = c.getInt(4);
            this.mState = c.getInt(5);
            this.mLatitude = c.getDouble(6);
            this.mLongitude = c.getDouble(7);
        }
        c.close();
    }

    private void getUsersInfoAtDB(SQLiteDatabase db) {
        String sql = "SELECT USER_NO , NICKNAME , TEAM , READY_STATUS , STATE , LATITUDE , LONGITUDE FROM USER ORDER BY TEAM_SELECT_TIME;";
        Cursor c = db.rawQuery(sql, null);
        User user = new User();
        while (c.moveToNext()) {
            int userNo = c.getInt(0);

            user.setUserNo(c.getInt(0));
            user.setNickName(c.getString(1));
            user.setTeam(c.getInt(2));
            user.setState(c.getInt(3));
            user.setLatitude(c.getDouble(4));
            user.setLongitude(c.getDouble(5));
//            if (user.getTeam() == User.TEAM_COP)
//                this.mCops.add(user);
//            else
//                this.mRobbers.add(user);
            aplyUser(user);
        }
    }


    public void refreshState(){
        ArrayList<User> arrEnemy = (mTeam == User.TEAM_COP) ? mRobbers : mCops;

        float[] distance = new float[3];
        int rst = 0;
        for(int i=0 ; i<arrEnemy.size() ; i++) {
            User user = arrEnemy.get(i);
            Location.distanceBetween(this.getLatitude() , this.getLongitude() , user.getLatitude() , user.getLongitude() , distance);
            if(distance[0] < AppConfig.DISTANCE_CATCHED
                    && mSpeed < AppConfig.SPEED_CATCHED
                    && mState == User.STATE_ALIVE
                    && user.getState() == User.STATE_ALIVE) {
                rst = mWarnigCount + 1;
                break;
            }
        }
        mWarnigCount = rst;
        if(mTeam == User.TEAM_ROBBER && mWarnigCount > AppConfig.LIMIT_WARNING_COUNT){
            mState = User.STATE_CATCHED;
        }
    }


    public boolean isViberate(){
        return mState == User.STATE_ALIVE && mWarnigCount > 0;
    }

    synchronized public void aplyUser(User user)
    {
        if(mCops == null) mCops = new ArrayList<User>();
        if(mRobbers == null) mRobbers= new ArrayList<User>();
        if(mUserIdxMap == null) mUserIdxMap = new HashMap<Integer , Integer>();
        if(mUserTeamMap == null) mUserTeamMap = new HashMap<Integer , Integer>();
        ArrayList<User> allyArray = (user.getTeam() == User.TEAM_COP) ? mCops : mRobbers;
        ArrayList<User> enemyArray = (user.getTeam() == User.TEAM_ROBBER) ? mCops : mRobbers;
        User targetUser = this.getUser(user.getUserNo());

        if(targetUser == null ){        //new user
            targetUser = new User(user);
            mUserTeamMap.put(targetUser.getUserNo() , targetUser.getTeam());
            mUserIdxMap.put(targetUser.getUserNo() , allyArray.size());
            allyArray.add(targetUser);
        }
        else{   //exists
            targetUser.cloneUser(user);
            if(!mUserTeamMap.get(user.getUserNo()).equals(user.getTeam())) {
                allyArray.remove(getUserIdx(user.getUserNo()));
                for(int i=0 ; i<allyArray.size() ; i++){
                    User tmpUser = allyArray.get(i);
                    mUserIdxMap.put(tmpUser.getUserNo() , i);
                }
                mUserTeamMap.put(user.getUserNo() , user.getTeam());
                mUserIdxMap.put(user.getUserNo() , enemyArray.size());
                enemyArray.add(targetUser);
            }
        }
    }

    private User getUser(int userNo) {
        Integer idx = getUserIdx(userNo);
        if(idx == null)
            return null;
        else{
            return mUserTeamMap.get(userNo) == User.TEAM_COP ? mCops.get(idx) : mRobbers.get(idx);
        }
    }

    private Integer getUserIdx(int userNo){
        return mUserIdxMap.get(userNo);
    }


    public int updateLocalLocation(Context context , double latitude , double longitude ){
        mLatitude = latitude;
        mLongitude = longitude;
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET LATITUDE = ? , LONGITUDE = ?");
        stmt.bindDouble(1,latitude);
        stmt.bindDouble(2,longitude);
        int rtn = stmt.executeUpdateDelete();
        db.close();
        return rtn;
    }

    public int updateGameBaseInfo(Context context , int roomId , int userNo , String nickname , int team){
        mRoomId = roomId;
        mUserNo = userNo;
        mNickName = nickname;
        mTeam = team;

        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET ROOM_ID = ? , USER_NO = ? , NICKNAME = ? , TEAM = ?");
        stmt.bindLong(1 , roomId);
        stmt.bindLong(2 , userNo);
        stmt.bindString(3 , nickname);
        stmt.bindLong(4 , team);

        int rtn = stmt.executeUpdateDelete();
        db.close();
        return rtn;
    }

    public int updateTeam(Context context , int team){
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET TEAM = ?");
        stmt.bindLong(1 , team);

        int rtn = stmt.executeUpdateDelete();
        db.close();
        return rtn;
    }


    public int updateState(Context context , int state) {
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET STATE = ?");
        stmt.bindLong(1 , state);

        int rtn = stmt.executeUpdateDelete();
        db.close();
        return rtn;
    }
}
