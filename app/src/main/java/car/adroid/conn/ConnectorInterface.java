package car.adroid.conn;

import org.json.JSONObject;

/**
 * Created by jjh on 2016-11-29.
 */

public interface ConnectorInterface {
    public JSONObject makeRoom(String strPwd , String strNick) throws Exception;
    public JSONObject joinRoom(int roomId , String pwd , String nickname) throws Exception;
    public JSONObject selectTeam(int roomId , int userNo, int team , int readyStatus) throws Exception;
    public JSONObject inGame(int roomId , int userNo , int team , double latitude , double longitude , int state , int lastChatIdx , int lastTeamChatIdx ) throws Exception;
    public JSONObject sendChat(int room_id , int team , int chat_flag , int user_no , String nickname , String text, int lastChatIdx , int lastTeamChatIdx) throws  Exception;

}
