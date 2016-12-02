package car.adroid.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;

import java.util.ArrayList;

import car.adroid.config.AppConfig;
import car.adroid.util.SimpleLogger;

/**
 * Created by jhjang04 on 2016-11-24.
 */

public class AppData {

    private static AppData INSTANCE = null;
    private int mRoomNo;
    private int mUserNo;
    private String mNickName;
    private double mLatitude;
    private double mLongitude;
    private int mTeam;
    private int mState;
    private ArrayList<User> mCops;
    private ArrayList<User> mRobbers;
    private int mLastChatIdx = 0;
    private int mLastTeamChatIdx = 0;
    private int mWarnigCount = 0;

    public int getRoomNo() {
        return mRoomNo;
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


    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public static AppData getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE  = new AppData();
            INSTANCE.getData(context);
        }
        return INSTANCE;
    }

    public void refreshState(){
        ArrayList<User> arr = null;
        if(mTeam == User.TEAM_COP){
            arr = mRobbers;
        }
        else{
            arr = mCops;
        }

        float[] distance = new float[3];
        int rst = 0;
        for(int i=0 ; i<arr.size() ; i++) {
            User user = arr.get(i);
            Location.distanceBetween(this.getLatitude() , this.getLongitude() , user.getLatitude() , user.getLongitude() , distance);
            if(distance[0] < AppConfig.DISTANCE_CATCHED) {
                rst = mWarnigCount + 1;
                break;
            }
        }
        mWarnigCount = rst;
        if(mTeam == User.TEAM_ROBBER && mWarnigCount > AppConfig.LIMIT_WARNING_COUNT){
            mState = User.STATE_CATCHED;
        }
    }





    public void getData(Context context){
        AppData data = getInstance(context);
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        getLocalInfoAtDB(db);
        getCopsInfoAtDB(db);
        getRobbersInfoAtDb(db);
        db.close();
    }

    public void getLocalData(Context context){
        AppData data = getInstance(context);
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        getLocalInfoAtDB(db);
        db.close();
    }



    private void getLocalInfoAtDB(SQLiteDatabase db){

        String sql = "SELECT ROOM_NO , USER_NO , NICKNAME , TEAM , STATE , LATITUDE , LONGITUDE FROM LOCAL_INFO";
        Cursor c = db.rawQuery(sql , null);

        if (c.moveToFirst()) {
            this.mRoomNo = c.getInt(0);
            this.mUserNo = c.getInt(1);
            this.mNickName = c.getString(2);
            this.mTeam = c.getInt(3);
            this.mState = c.getInt(4);
            this.mLatitude = c.getDouble(5);
            this.mLongitude = c.getDouble(6);
        }
        c.close();
    }

    private void getCopsInfoAtDB(SQLiteDatabase db){

        String sql = "SELECT USER_NO , NICKNAME , TEAM , STATE , LATITUDE , LONGITUDE FROM USER WHERE TEAM = " + User.TEAM_COP + " ORDER BY TEAM_SELECT_TIME";
        Cursor c = db.rawQuery(sql , null);
        if(c.moveToFirst()) {
            if(this.mRobbers == null)
                this.mRobbers = new ArrayList<User>();
            else
                this.mRobbers.clear();
            do {
                User user = new User();
                user.setUserNo(c.getInt(0));
                user.setNickName(c.getString(1));
                user.setTeam(c.getInt(2));
                user.setState(c.getInt(3));
                user.setLatitude(c.getInt(4));
                user.setLongitude(c.getInt(5));
                this.mCops.add(user);
            } while (c.moveToNext());
        }
    }

    private void getRobbersInfoAtDb(SQLiteDatabase db){
        String sql= "SELECT USER_NO , NICKNAME , TEAM , STATE , LATITUDE , LONGITUDE FROM USER WHERE TEAM = " + User.TEAM_ROBBER + " ORDER BY TEAM_SELECT_TIME";
        Cursor c = db.rawQuery(sql , null);
        if(c.moveToFirst()){
            if(this.mRobbers == null)
                this.mRobbers = new ArrayList<User>();
            else
                this.mRobbers.clear();
            do{
                User user = new User();
                user.setUserNo(c.getInt(0));
                user.setNickName(c.getString(1));
                user.setTeam(c.getInt(2));
                user.setState(c.getInt(3));
                user.setLatitude(c.getInt(4));
                user.setLongitude(c.getInt(5));
                this.mRobbers.add(user);
            }while(c.moveToNext());
        }
    }


    public int updateLocalLocation(Context context , double latitude , double longitude ){
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET LATITUDE = ? , LONGITUDE = ?");
        stmt.bindDouble(1,latitude);
        stmt.bindDouble(2,longitude);
        int rtn = stmt.executeUpdateDelete();
        SimpleLogger.debug(context,"update my location");
        db.close();
        return rtn;
    }

    public int updateGameInfo(Context context , int room_no , int user_no ,  String nick_name ){
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET ROOM_NO= ? , USER_NO= ? , NICKNAME = ?");
        stmt.bindLong(1 , room_no);
        stmt.bindLong(2 , user_no);
        stmt.bindString(3 , nick_name);

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

    public int updateState(Context context , int state){
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET STATE = ?");
        stmt.bindLong(1 , state);

        int rtn = stmt.executeUpdateDelete();
        db.close();
        return rtn;
    }


}
