package car.adroid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import car.adroid.com.R;

/**
 * Created by mbj94 on 2016-12-02.
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView senderText, chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private LinearLayout singleMessageContainer;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    @Override
    public void addAll(Collection<? extends ChatMessage> collection) {
        super.addAll(collection);
        chatMessageList.addAll(collection);
    }

    @Override
    public void clear() {
        super.clear();
        chatMessageList.clear();
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        ChatMessage chatMessageObj = getItem(position);
        senderText = (TextView)row.findViewById(R.id.tvSender);
        chatText = (TextView) row.findViewById(R.id.singleMessage);

        senderText.setText(chatMessageObj.getSender());
        chatText.setText(chatMessageObj.getMessage());
        chatText.setBackgroundResource(chatMessageObj.isLeft() ? R.drawable.bubble_b : R.drawable.bubble_a);
        senderText.setGravity(chatMessageObj.isLeft() ? Gravity.LEFT : Gravity.RIGHT);
        singleMessageContainer.setGravity(chatMessageObj.isLeft() ? Gravity.LEFT : Gravity.RIGHT);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}