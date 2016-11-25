package car.adroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jjh on 2016-11-24.
 */

public class AppDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "car_db.db";

    public static final Table[] TABLES = {
            new Table("CHAT" , new Column[]{
                    new Column("IDX", "INTEGER" , true)
                    , new Column("CAHT_FLAG", "INTEGER" , true)
                    , new Column("USER_NO", "INTEGER")
                    , new Column("NICKNAME", "VARCHAR(20)")
                    , new Column("TEAM", "INTEGER")
                    , new Column("WR_TIME", "TIMESTAMP")
                    , new Column("TEXT", "VARCAHR(200)")
            })
    };


    public AppDB(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i=0 ; i<TABLES.length ; i++){
            db.execSQL(TABLES[i].getCreateSql());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class Table{
        public final String TableName;
        public final Column[] Cols;

        public Table(String _TableName , Column[] _Cols){
            this.TableName = _TableName;
            this.Cols = _Cols;
        }

        public String getCreateSql(){
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("CREATE TALBE ").append(TableName).append("(\n");
            for(int i=0 ; i<Cols.length ; i++){
                if(i>0) sqlBuffer.append(", ");
                sqlBuffer.append(Cols[i].Name)
                        .append(" ").append(Cols[i].Type);
                if(Cols[i].isKey)
                    sqlBuffer.append(" PRIMARY KEY");
                sqlBuffer.append("\n");
            }
            sqlBuffer.append(")");
            return sqlBuffer.toString();
        }

        public String getSelectSql(String whereTxt){
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("SELECT ");
            for(int i=0 ; i<Cols.length ; i++){
                if(i>0) sqlBuffer.append("\t, ");
                sqlBuffer.append(Cols[i].Name).append("\n");
            }
            sqlBuffer.append("FROM ").append(TableName).append("\n");
            sqlBuffer.append(whereTxt);
            return sqlBuffer.toString();
        }

        public String getUpdSql(){
            return null;
        }
    }

    public static class Column{
        public final String Name;
        public final String Type ;
        public final boolean isKey;
        public Column(String _Name , String _Type){
            this.Name = _Name;
            this.Type = _Type;
            this.isKey = false;
        }

        public Column(String _Name , String _Type , boolean _isKey){
            this.Name = _Name;
            this.Type = _Type;
            this.isKey = _isKey;
        }
    }
}
