package com.example.campustradingplatform.Chat.msgDir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.campustradingplatform.Chat.ChatBean.ChatDetailItem;
import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.GlobalUtil;

import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/5
 */
public class ChatDetailAdapter  extends ArrayAdapter<ChatDetailItem> {
    Context context;
    private View view;
    private EditText sendText;

    public ChatDetailAdapter(@NonNull Context context, int resource, @NonNull List<ChatDetailItem> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatDetailItem chatDetailItem = getItem(position);


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_detail_item,parent,false);

        if(!chatDetailItem.isMeSend()){//右侧
            setRowleft(chatDetailItem.getMsg_con());
        }else{//左侧
            setRowRight(chatDetailItem.getMsg_con());
        }
        return view;
    }

    private void setRowleft(String msgRowCon) {
        LinearLayout leftLayout = (LinearLayout)view.findViewById(R.id.msg_row_left);
        leftLayout.setVisibility(View.VISIBLE);

        LinearLayout rightLayout =(LinearLayout)view.findViewById(R.id.msg_row_right);
        rightLayout.setVisibility(View.GONE);


        EditText editText = (EditText)view.findViewById(R.id.msg_left_text);
        if(msgRowCon!="")
            editText.setText(msgRowCon);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText.clearFocus();
                GlobalUtil.hideKeyboard(GlobalUtil.getActivityByContext(context));
            }
        });
    }

    private void setRowRight(String msgRowCon) {
        LinearLayout leftLayout = (LinearLayout)view.findViewById(R.id.msg_row_left);
        leftLayout.setVisibility(View.GONE);

        LinearLayout rightLayout =(LinearLayout)view.findViewById(R.id.msg_row_right);
        rightLayout.setVisibility(View.VISIBLE);

        EditText editText = (EditText)view.findViewById(R.id.msg_right_text);
        if(msgRowCon!="")
            editText.setText(msgRowCon);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText.clearFocus();
                GlobalUtil.hideKeyboard(GlobalUtil.getActivityByContext(context));
            }
        });
    }

    public void setSendText(EditText editText){
        this.sendText = editText;
    }
}
