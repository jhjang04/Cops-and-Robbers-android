package car.adroid.util;

/**
 * Created by mbj94 on 2016-12-02.
 */

public class ChatMessage {
    private boolean left;
    private String message;
    private String sender;

    public String getSender() {
        return sender;
    }

    public boolean IsLeft() {
        return left;
    }

    public String getMessage() {
        return message;
    }


    public ChatMessage(boolean left, String message,String sender) {
        super();
        this.left = left;
        this.message = message;
        this.sender = sender;
    }
}