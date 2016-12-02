package car.adroid.conn;

import org.json.JSONObject;

/**
 * Created by jjh on 2016-11-29.
 */

public abstract class abstractConnector {
    public abstract JSONObject makeRoom(String strPwd , String strNick);
    public abstract JSONObject joinRoom(String strRoomNo , String strPwd , String strNick);
}
