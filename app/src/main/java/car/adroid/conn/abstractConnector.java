package car.adroid.conn;

/**
 * Created by jjh on 2016-11-29.
 */

public abstract class abstractConnector {
    public abstract void makeRoom(String strPwd , String strNick);
    public abstract void joinRoom(String strRoomNo , String strPwd , String strNick);
}
