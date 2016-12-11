package car.adroid.util;

import org.json.JSONObject;

/**
 * Created by mbj94 on 2016-12-02.
 */

public class ChatMessage {
    public static int CHAT_ALL = 0;
    public static int CHAT_COP = 1;
    public static int CHAT_ROBBERS = 2;

    private boolean left;
    private String message;
    private String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isLeft() {
        return left;
    }

    public void setIsLeft(boolean left) {
        this.left = left;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public ChatMessage(){}
    public ChatMessage(boolean left, String message,String sender) {
        super();
        this.left = left;
        this.message = message;
        this.sender = sender;
    }

    public ChatMessage(JSONObject json) throws Exception{
        json.getString("team");
        json.getString("chat_flag");
        json.getInt("idx");
        json.getInt("user_no");
        json.getString("nickname");
        json.getString("wr_time");
        json.getString("text");
    }
}