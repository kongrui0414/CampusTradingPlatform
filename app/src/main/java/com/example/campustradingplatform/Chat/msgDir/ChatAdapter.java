package com.example.campustradingplatform.Chat.msgDir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.campustradingplatform.Chat.ChatBean.ChatItem;
import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.PicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/3/31
 * 用于处理MainChatACtivity的chatList界面显示
 */
public class ChatAdapter extends ArrayAdapter<ChatItem> {

    //总chatList 数据
    private Context context;
    private List<ChatItem> chatList = new ArrayList<>();

    //每个chatIterm的界面
    private TextView userName;
    private TextView textDetail;
    private ImageView headImagView;
    private TextView sendTimeView;


    public ChatAdapter(@NonNull Context context, int resource, @NonNull List<ChatItem> objects) {
        super(context, resource, objects);
        this.context=context;
        chatList = objects;
    }


    /**
     * methodName(方法名）
     * description: 初始化item的界面
     * @param userName : 用户姓名
     * @param textDetail: 最后一条信息
     * @param headImagViwe: 用户头像
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatItem chatItem = getItem(position);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        userName = (TextView)view.findViewById(R.id.user_name);
        textDetail = (TextView)view.findViewById(R.id.msg_detail);
        headImagView = (ImageView)view.findViewById(R.id.head_img);
        sendTimeView = (TextView)view.findViewById(R.id.send_time);
        //设置具体界面内容
        textDetail.setText(chatItem.getLastMsg());
        userName.setText(chatItem.getSeller().getUserName());
        sendTimeView.setText(chatItem.getLastMsgSendTime());

        //图片设置为圆角
        PicUtil.roundBitmap(headImagView,getContext());
        return view;
    }

}
