package car.adroid.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import car.adroid.config.AppConfig;
import car.adroid.util.ChatMessage;
import car.adroid.util.DateUtil;

/**
 * Created by jhjang04 on 2016-11-24.
 */

public class AppData {

    private static AppData INSTANCE = null;

    private Context mAppContext = null;

    private int mRoomId = 0;
    private int mUserNo = 0;
    private String mNickName = "";
    private double mLatitude = 0;
    private double mLongitude = 0;
    private int mTeam = 0;
    private int mReadyStatus = User.READY_STATUS_NOT_READY;
    private int mState = User.STATE_ALIVE;
    private String mStartTime = "";

    private ArrayList<User> mCops = new ArrayList<User>();
    private ArrayList<User> mRobbers = new ArrayList<User>();
    private int mLastChatIdx = 0;
    private int mLastTeamChatIdx = 0;

    private int mWarnigCount = 0;
    private Date mWarnigStartTime = null;
    private float mSpeed;
    private Map<Integer, Integer> mUserTeamMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> mUserIdxMap = new HashMap<Integer, Integer>();

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

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
    }

    public ArrayList<User> getCops() {
        return mCops;
    }

    public ArrayList<User> getRobbers() {
        return mRobbers;
    }

    public ArrayList<User> getAllys(int team) {
        return (team == User.TEAM_COP) ? mCops : mRobbers;
    }

    public ArrayList<User> getEnemys(int team) {
        return (team == User.TEAM_ROBBER) ? mCops : mRobbers;
    }

    public int getLastChatIdx() {
        return mLastChatIdx;
    }

    public int getLastTeamChatIdx() {
        return mLastTeamChatIdx;
    }

    public void setLastChatIdx(int mLastChatIdx) {
        this.mLastChatIdx = mLastChatIdx;
    }

    public void setLastTeamChatIdx(int mLastTeamChatIdx) {
        this.mLastTeamChatIdx = mLastTeamChatIdx;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void updateStartTime(String startTime) {

        mStartTime = startTime;

        AppDBHelper dbHelper = new AppDBHelper(mAppContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmtLocalInfo = db.compileStatement("UPDATE LOCAL_INFO SET START_TIME = ?");
        stmtLocalInfo.bindString(1, startTime);

        stmtLocalInfo.executeUpdateDelete();
        db.close();

    }

    private AppData(Context appContext) {
        mAppContext = appContext;
    }

    public static AppData getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppData(context);
            INSTANCE.getData();
        }
        return INSTANCE;
    }

    public static AppData getNewInsance(Context context) {
        AppDBHelper dbHelper = new AppDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmtDeleteUser = db.compileStatement("delete from user");
        stmtDeleteUser.executeUpdateDelete();

        SQLiteStatement stmtDeleteChat = db.compileStatement("delete from chat");
        stmtDeleteChat.executeUpdateDelete();
        db.close();

        INSTANCE = new AppData(context);
        return INSTANCE;
    }

    public void getData() {
        AppDBHelper dbHelper = new AppDBHelper(mAppContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        getLocalInfoAtDB(db);
        getUsersInfoAtDB(db);
        db.close();
    }

    private void getLocalInfoAtDB(SQLiteDatabase db) {

        String sql = "SELECT ROOM_ID , USER_NO , NICKNAME , TEAM , READY_STATUS , STATE , LATITUDE , LONGITUDE , START_TIME FROM LOCAL_INFO;";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToNext()) {
            this.mRoomId = c.getInt(0);
            this.mUserNo = c.getInt(1);
            this.mNickName = c.getString(2);
            this.mTeam = c.getInt(3);
            this.mReadyStatus = c.getInt(4);
            this.mState = c.getInt(5);
            this.mLatitude = c.getDouble(6);
            this.mLongitude = c.getDouble(7);
            this.mStartTime = c.getString(8);
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

            aplyUser(null, user);
        }
    }


    public void refreshState() {
        ArrayList<User> arrEnemy = (mTeam == User.TEAM_COP) ? mRobbers : mCops;

        float[] distance = new float[3];
        int rst = 0;
        boolean bWarning = false;
        for (int i = 0; i < arrEnemy.size(); i++) {
            User user = arrEnemy.get(i);
            Location.distanceBetween(this.getLatitude(), this.getLongitude(), user.getLatitude(), user.getLongitude(), distance);
            if (distance[0] < AppConfig.DISTANCE_CATCHED
                    && mSpeed < AppConfig.SPEED_CATCHED
                    && mState == User.STATE_ALIVE
                    && user.getState() == User.STATE_ALIVE ) {
                bWarning = true;
//                rst = mWarnigCount + 1;
                break;
            }
        }
        if(bWarning && mWarnigStartTime == null){
            mWarnigStartTime = new Date();
        }
        else if(!bWarning){
            mWarnigStartTime = null;
        }
//        mWarnigCount = rst;
        if ( mTeam == User.TEAM_ROBBER
                && mWarnigStartTime != null
                && (new Date().getTime() - mWarnigStartTime.getTime()) > AppConfig.CATCHED_MILLISECONDS ) {
            mState = User.STATE_CATCHED;
        }
    }


    public boolean isViberate() {
        return mState == User.STATE_ALIVE && mWarnigStartTime != null;
    }

    synchronized public void aplyUser(SQLiteDatabase db, User user) {
        if (mCops == null) mCops = new ArrayList<User>();
        if (mRobbers == null) mRobbers = new ArrayList<User>();
        if (mUserIdxMap == null) mUserIdxMap = new HashMap<Integer, Integer>();
        if (mUserTeamMap == null) mUserTeamMap = new HashMap<Integer, Integer>();

        ArrayList<User> allyArray = ((mUserTeamMap.get(user.getUserNo()) != null ? mUserTeamMap.get(user.getUserNo()) : user.getTeam()) == User.TEAM_COP) ? mCops : mRobbers;
        ArrayList<User> enemyArray = ((mUserTeamMap.get(user.getUserNo()) != null ? mUserTeamMap.get(user.getUserNo()) : user.getTeam()) == User.TEAM_ROBBER) ? mCops : mRobbers;
        User targetUser = this.getUser(user.getUserNo());
        boolean isInsert = (targetUser == null);

        if (targetUser == null) {        //new user
            targetUser = new User(user);
            mUserTeamMap.put(targetUser.getUserNo(), targetUser.getTeam());
            mUserIdxMap.put(targetUser.getUserNo(), allyArray.size());
            allyArray.add(targetUser);
        } else {   //exists
            targetUser.cloneUser(user);

            if (!mUserTeamMap.get(targetUser.getUserNo()).equals(targetUser.getTeam())) {
//                allyArray.remove(getUserIdx(targetUser.getUserNo()).intValue());
                allyArray.remove(targetUser);

                for (int i = 0; i < allyArray.size(); i++) {
                    User tmpUser = allyArray.get(i);
                    mUserIdxMap.put(tmpUser.getUserNo(), i);
                }
                mUserTeamMap.put(user.getUserNo(), user.getTeam());
                mUserIdxMap.put(user.getUserNo(), enemyArray.size());
                enemyArray.add(targetUser);
            }
        }
        if (db == null) return;
        String strSql = "";
        if (isInsert) {
            strSql = "insert into user(user_no , nickname , team , ready_status , state , latitude , longitude , team_select_time , last_access) values( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        } else {
            strSql = "update user set user_no = ? , nickname = ? , team = ? , ready_status = ? , state = ? , latitude = ? , longitude = ? , team_select_time = ? , last_access = ? where user_no = ?";
        }
        SQLiteStatement stmt = db.compileStatement(strSql);
        stmt.bindDouble(1, user.getUserNo());
        stmt.bindString(2, user.getNickName());
        stmt.bindLong(3, user.getTeam());
        stmt.bindLong(4, user.getReadyStatus());
        stmt.bindLong(5, user.getState());
        stmt.bindDouble(6, user.getLatitude());
        stmt.bindDouble(7, user.getLongitude());
        stmt.bindString(8, user.getTeamSelectTime());
        stmt.bindString(9, user.getLastAccess());
        if (!isInsert) {
            stmt.bindLong(10, user.getUserNo());
        }

        stmt.executeUpdateDelete();
    }


    private User getUser(int userNo) {
        Integer idx = getUserIdx(userNo);
        if (idx == null)
            return null;
        else {
            return mUserTeamMap.get(userNo) == User.TEAM_COP ? mCops.get(idx) : mRobbers.get(idx);
        }
    }

    private Integer getUserIdx(int userNo) {
        return mUserIdxMap.get(userNo);
    }


    public void updateLocalLocation(double latitude, double longitude) {
        User user = getUser(mUserNo);
        user.setLatitude(latitude);
        user.setLongitude(longitude);

        mLatitude = latitude;
        mLongitude = longitude;

        AppDBHelper dbHelper = new AppDBHelper(mAppContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        aplyUser(db, user);

        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET LATITUDE = ? , LONGITUDE = ?");
        stmt.bindDouble(1, latitude);
        stmt.bindDouble(2, longitude);
        stmt.executeUpdateDelete();
        db.close();
    }

    public void updateGameBaseInfo(int roomId, int userNo, String nickname, int team) {
        mRoomId = roomId;
        mUserNo = userNo;
        mNickName = nickname;
        mTeam = team;
        int rtn = 0;

        AppDBHelper dbHelper = new AppDBHelper(mAppContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmtLocalInfo = db.compileStatement("UPDATE LOCAL_INFO SET ROOM_ID = ? , USER_NO = ? , NICKNAME = ? , TEAM = ?");
        stmtLocalInfo.bindLong(1, roomId);
        stmtLocalInfo.bindLong(2, userNo);
        stmtLocalInfo.bindString(3, nickname);
        stmtLocalInfo.bindLong(4, team);

        stmtLocalInfo.executeUpdateDelete();

        User user = new User();
        Date date = new Date();

        user.setUserNo(userNo);
        user.setTeam(team);
        user.setNickName(nickname);
        user.setReadyStatus(User.READY_STATUS_NOT_READY);
        user.setLastAccess(DateUtil.getCurrent());
        user.setTeamSelectTime(DateUtil.getCurrent());

        aplyUser(db, user);

        db.close();
    }

    public void updateTeam(int team) {
        User user = getUser(mUserNo);
        mTeam = team;
        user.setTeam(team);

        AppDBHelper dbHelper = new AppDBHelper(mAppContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        aplyUser(db, user);

        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET TEAM = ?");
        stmt.bindLong(1, team);

        stmt.executeUpdateDelete();
        db.close();

    }

    public void ready() {
        User user = getUser(mUserNo);
        mReadyStatus = User.READY_STATUS_READY;
        user.setReadyStatus(User.READY_STATUS_READY);

        AppDBHelper dbHelper = new AppDBHelper(mAppContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        aplyUser(db, user);

        SQLiteStatement stmt = db.compileStatement("update local_info set ready_status = ?");
        stmt.bindLong(1, User.READY_STATUS_READY);

        stmt.executeUpdateDelete();
        db.close();
    }


    public void updateState(int state) {
        AppDBHelper dbHelper = new AppDBHelper(mAppContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE LOCAL_INFO SET STATE = ?");
        stmt.bindLong(1, state);

        stmt.executeUpdateDelete();
        db.close();
    }

    public void insertChatData(SQLiteDatabase db, int idx, int chat_flag, int team, int user_no, String nickname, String wr_time, String text) {

        try {
            SQLiteStatement stmt = db.compileStatement("insert into chat(idx , chat_flag , team , user_no , nickname , wr_time , text) values(? , ? , ? , ? , ? , ? , ?)");
            stmt.bindLong(1, idx);
            stmt.bindLong(2, chat_flag);
            stmt.bindLong(3, team);
            stmt.bindLong(4, user_no);
            stmt.bindString(5, nickname);
            stmt.bindString(6, wr_time);
            stmt.bindString(7, text);

            stmt.executeUpdateDelete();
        } catch (Exception e) {
            //데이터 중복 에러를 예상 할 수 있다.
            //처리 내용 필요없음.
        }
    }

    public ArrayList<ChatMessage> getChatList() {
        SQLiteDatabase db = new AppDBHelper(mAppContext).getReadableDatabase();
        String sql = "select idx , team , user_no , nickname , wr_time , text from chat where chat_flag = 0";
        Cursor c = db.rawQuery(sql, null);
        ArrayList<ChatMessage> arr = new ArrayList<ChatMessage>();
        while (c.moveToNext()) {
            int idx = c.getInt(0);
            int team = c.getInt(1);
            int user_no = c.getInt(2);
            String nickname = c.getString(3);
            String wr_time = c.getString(4);
            String text = c.getString(5);
            ChatMessage m = new ChatMessage();
            m.setIsLeft(user_no != mUserNo);
            m.setSender(nickname);
            m.setMessage(text);
            arr.add(m);
        }
        c.close();
        db.close();
        return arr;
    }

    public ArrayList<ChatMessage> getTeamChatList() {
        SQLiteDatabase db = new AppDBHelper(mAppContext).getReadableDatabase();
        String sql = "select idx , team , user_no , nickname , wr_time , text from chat where chat_flag = ?";
        Cursor c = db.rawQuery(sql, new String[] {"" + mTeam});
        ArrayList<ChatMessage> arr = new ArrayList<ChatMessage>();
        while (c.moveToNext()) {
            int idx = c.getInt(0);
            int team = c.getInt(1);
            int user_no = c.getInt(2);
            String nickname = c.getString(3);
            String wr_time = c.getString(4);
            String text = c.getString(5);
            ChatMessage m = new ChatMessage();
            m.setIsLeft(user_no != mUserNo);
            m.setSender(nickname);
            m.setMessage(text);
            arr.add(m);
        }
        c.close();
        db.close();
        return arr;
    }


    public float getDistanceWithMe(double lat , double lot){
        float[] f = new float[3];
        Location.distanceBetween(mLatitude , mLongitude , lat , lot , f);
        return f[0];
    }
}

