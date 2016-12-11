package car.adroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jjh on 2016-11-24.
 */

public class AppDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "car_db.db";
    Context mContext;


    public AppDBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Table[] TABLES = {
//                new Table("CHAT" , new Column[]{
//                        new Column("IDX", "INTEGER" )
//                        , new Column("CAHT_FLAG", "INTEGER" )
//                        , new Column("USER_NO", "INTEGER")
//                        , new Column("NICKNAME", "TEXT")
//                        , new Column("TEAM", "INTEGER")
//                        , new Column("WR_TIME", "TEXT")
//                        , new Column("TEXT", "TEXT")
//                })
//                , new Table("MY_INFO" , new Column[]{
//                        new Column("ROOM_NO", "INTEGER" , true)
//                        , new Column("USER_NO", "USER_NO" , true)
//                        , new Column("NICKNAME", "INTEGER" , true)
//                        , new Column("TEAM", "INTEGER")
//                        , new Column("STATUS", "INT")
//                        , new Column("TEAM", "INTEGER")
//                        , new Column("LATITUDE", "REAL")
//                        , new Column("LONGITUDE", "REAL")
//                })
//                , new Table("MY_INFO" , new Column[]{
//                        new Column("ROOM_NO", "INTEGER" , true)
//                        , new Column("USER_NO", "USER_NO" , true)
//                        , new Column("NICKNAME", "INTEGER" , true)
//                        , new Column("TEAM", "INTEGER")
//                        , new Column("STATUS", "INT")
//                        , new Column("TEAM", "INTEGER")
//                        , new Column("LATITUDE", "REAL")
//                        , new Column("LONGITUDE", "REAL")
//                })
//        };
//
//        for(int i=0 ; i<TABLES.length ; i++){
//            db.execSQL(TABLES[i].getCreateSql());
//        }

        String[] strSqls = {
                "CREATE TABLE CHAT(IDX INTEGER , CHAT_FLAG INTEGER, TEAM INTEGER , USER_NO INTEGER , NICKNAME TEXT , WR_TIME TEXT , TEXT TEXT , PRIMARY KEY(IDX , CHAT_FLAG));"
                ,"CREATE TABLE USER(USER_NO INTEGER , NICKNAME TEXT , TEAM INTEGER , READY_STATUS INTEGER , STATE INTEGER , LATITUDE REAL , LONGITUDE REAL , TEAM_SELECT_TIME TEXT , LAST_ACCESS TEXT , PRIMARY KEY(USER_NO));"
                ,"CREATE TABLE LOCAL_INFO(ROOM_ID INTEGER , USER_NO INTEGER , NICKNAME INTEGER , TEAM INTEGER , READY_STATUS INTEGER , STATE INTEGER , LATITUDE REAL , LONGITUDE REAL);"
                ,"INSERT INTO LOCAL_INFO(ROOM_ID) VALUES(0);"
        };
        for(int i=0 ; i<strSqls.length ; i++){
            db.execSQL(strSqls[i]);
        }
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public static class Table{
//        public final String TableName;
//        public final Column[] Cols;
//
//        public Table(String _TableName , Column[] _Cols){
//            this.TableName = _TableName;
//            this.Cols = _Cols;
//        }
//
//        public String getCreateSql(){
//            StringBuffer sqlBuffer = new StringBuffer();
//            sqlBuffer.append("CREATE TALBE ").append(TableName).append("(\n");
//            for(int i=0 ; i<Cols.length ; i++){
//                if(i>0) sqlBuffer.append(", ");
//                sqlBuffer.append(Cols[i].Name)
//                        .append(" ").append(Cols[i].Type);
//                if(Cols[i].isKey)
//                    sqlBuffer.append(" PRIMARY KEY");
//                sqlBuffer.append("\n");
//            }
//            sqlBuffer.append(")");
//            return sqlBuffer.toString();
//        }
//    }
//
//    public static class Column{
//        public final String Name;
//        public final String Type ;
//        public final boolean isKey;
//        public Column(String _Name , String _Type){
//            this.Name = _Name;
//            this.Type = _Type;
//            this.isKey = false;
//        }
//
//        public Column(String _Name , String _Type , boolean _isKey){
//            this.Name = _Name;
//            this.Type = _Type;
//            this.isKey = _isKey;
//        }
//    }

}

