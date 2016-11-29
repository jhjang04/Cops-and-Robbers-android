package car.adroid.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public static AppData getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE  = new AppData();
            INSTANCE.updateData(context);
        }
        return INSTANCE;
    }


    public void updateData(Context context){
        AppData data = getInstance(context);
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        getMyInfoAtDB(db);
        getCopsInfoAtDB(db);
        getRobbersInfoAtDb(db);
        db.close();
    }


    private void getMyInfoAtDB(SQLiteDatabase db){
        String sql = "SELECT ROOM_NO\n" +
                "\t, USER_NO\n" +
                "\t, NICKNAME\n" +
                "\t, TEAM\n" +
                "\t, STATE\n" +
                "\t, LATITUDE\n" +
                "\t, LONGITUDE\n" +
                "FROM MY_INFO";

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
        String sql = "SELECT USER_NO\n" +
                "\t, NICKNAME\n" +
                "\t, TEAM\n" +
                "\t, STATE\n" +
                "\t, LATITUDE\n" +
                "\t, LONGITUDE\n" +
                "FROM USER\n" +
                "WHERE TEAM = " + User.TEAM_COP + "\n" +
                "ORDER BY TEAM_SELECT_TIME";
        Cursor c = db.rawQuery(sql , null);
        if(c.moveToFirst()) {
            if(this.mRobbers == null)
                this.mRobbers = new ArrayList<User>();
            else
                this.mRobbers.clear();
            do {
                User user = new User();
                user.userNo = c.getInt(0);
                user.nick = c.getString(1);
                user.team = c.getInt(2);
                user.state = c.getInt(3);
                user.latitude = c.getInt(4);
                user.longitude = c.getInt(5);
                this.mCops.add(user);
            } while (c.moveToNext());
        }
    }

    private void getRobbersInfoAtDb(SQLiteDatabase db){
        String sql = "SELECT USER_NO\n" +
                "\t, NICKNAME\n" +
                "\t, TEAM\n" +
                "\t, STATUS\n" +
                "\t, LATITUDE\n" +
                "\t, LONGITUDE\n" +
                "FROM USER\n" +
                "WHERE TEAM = " + User.TEAM_ROBBER + "\n" +
                "ORDER BY TEAM_SELECT_TIME";

        Cursor c = db.rawQuery(sql , null);
        if(c.moveToFirst()){
            if(this.mRobbers == null)
                this.mRobbers = new ArrayList<User>();
            else
                this.mRobbers.clear();
            do{
                User user = new User();
                user.userNo = c.getInt(0);
                user.nick = c.getString(1);
                user.team = c.getInt(2);
                user.state = c.getInt(3);
                user.latitude = c.getInt(4);
                user.longitude = c.getInt(5);
                this.mRobbers.add(user);
            }while(c.moveToNext());
        }
    }
}
