package com.example.campustradingplatform.Msg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.campustradingplatform.R;
import com.example.campustradingplatform.UtilTools.GlobalUtil;

import java.util.List;

public class MsgRowAdapter extends ArrayAdapter<MsgRowItem> {
    View view;
    Context context;

    EditText sendText;
    public MsgRowAdapter(@NonNull Context context, int resource, @NonNull List<MsgRowItem> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MsgRowItem msgRowItem = getItem(position);
         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_row_item,parent,false);
        if(msgRowItem.isLeft()){
            setRowleft(msgRowItem.getMsgRowCon());
        }else{
            setRowRight(msgRowItem.getMsgRowCon());
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
