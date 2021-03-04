package com.example.campustradingplatform.Msg;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.PicUtil;

public class MsgRow extends LinearLayout {

    Context context;
    public MsgRow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.msg_row_item,this);
        this.context = context;
    }

    public void setMsgRowLeft(String imgurl,String text){
        LinearLayout leftLayout = (LinearLayout)findViewById(R.id.msg_row_left);
        leftLayout.setVisibility(VISIBLE);

        LinearLayout rightLayout = (LinearLayout)findViewById(R.id.msg_row_right);
        rightLayout.setVisibility(GONE);

        ImageView headimgView = (ImageView)findViewById(R.id.msg_left_head);
        PicUtil.roundBitmap(headimgView,context);
    }

    public void setMsgRowRight(String imgurl,String text){
        LinearLayout leftLayout = (LinearLayout)findViewById(R.id.msg_row_left);
        leftLayout.setVisibility(GONE);

        LinearLayout rightLayout = (LinearLayout)findViewById(R.id.msg_row_right);
        rightLayout.setVisibility(VISIBLE);

        ImageView headimgView = (ImageView)findViewById(R.id.msg_right_head);
        PicUtil.roundBitmap(headimgView,context);
    }
}
