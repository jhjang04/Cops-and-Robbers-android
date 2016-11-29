package car.adroid.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by jjh on 2016-11-24.
 */

public class AppDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "car_db.db";


    public AppDBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        String strSql;
        strSql = "CREATE TALBE CHAT(\n" +
                "\tIDX INTEGER\n" +
                "\t, CAHT_FLAG INTEGER\n" +
                "\t, USER_NO INTEGER\n" +
                "\t, NICKNAME TEXT\n" +
                "\t, TEAM INTEGER\n" +
                "\t, WR_TIME TEXT\n" +
                "\t, TEXT TEXT\n" +
                ")";
        db.execSQL(strSql);

        strSql = "\n" +
                "CREATE TALBE MY_INFO(\n" +
                "\tROOM_NO INTEGER PRIMARY KEY\n" +
                "\t, USER_NO INTEGER PRIMARY KEY\n" +
                "\t, NICKNAME INTEGER PRIMARY KEY\n" +
                "\t, TEAM INTEGER\n" +
                "\t, STATE INT\n" +
                "\t, LATITUDE REAL\n" +
                "\t, LONGITUDE REAL\n" +
                ")";

        db.execSQL(strSql);

        strSql = "CREATE TALBE USER(\n" +
                "\t, USER_NO INTEGER PRIMARY KEY\n" +
                "\t, NICKNAME INTEGER PRIMARY KEY\n" +
                "\t, TEAM INTEGER\n" +
                "\t, STATE INTEGER\n" +
                "\t, LATITUDE REAL\n" +
                "\t, LONGITUDE REAL\n" +
                ")";
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void updateAppData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT lastchapter FROM Bookdetails WHERE bookpath=? ";
        String tmp;
        Cursor c = db.rawQuery(selectQuery, new String[] { "param" });

        if (c.moveToFirst()) {
            tmp = c.getString(0);
        }
        c.close();

        SQLiteStatement stmt = db.compileStatement("SELECT * FROM Country WHERE code = ?");
        stmt.bindString(1, "US");
        stmt.execute();
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
