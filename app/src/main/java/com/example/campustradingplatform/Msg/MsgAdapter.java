package com.example.campustradingplatform.Msg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.campustradingplatform.R;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<MsgItem> {

    private Context context;
    private TextView userName;
    private TextView textDetail;
    private ImageView headImagView;

    public MsgAdapter(@NonNull Context context, int resource, @NonNull List<MsgItem> objects) {
        super(context, resource, objects);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MsgItem msgItem = getItem(position);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        userName = (TextView)view.findViewById(R.id.user_name);
        textDetail = (TextView)view.findViewById(R.id.msg_detail);
        headImagView = (ImageView)view.findViewById(R.id.head_img);

        textDetail.setText(msgItem.getMsgDetail());
        userName.setText(msgItem.getUserName());
        //图片设置为圆角
        roundBitmap(headImagView);
        return view;
    }


    //圆角图片
    private void roundBitmap(ImageView imageView){
        //如果是圆的时候，我们应该把bitmap图片进行剪切成正方形， 然后再设置圆角半径为正方形边长的一半即可
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        Bitmap bitmap = null;
        //将长方形图片裁剪成正方形图片
        if (image.getWidth() == image.getHeight()) {
            bitmap = Bitmap.createBitmap(image, image.getWidth() / 2 - image.getHeight() / 2, 0, image.getHeight(), image.getHeight());
        } else {
            bitmap = Bitmap.createBitmap(image, 0, image.getHeight() / 2 - image.getWidth() / 2, image.getWidth(), image.getWidth());
        }
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        //圆角半径为正方形边长的一半
        roundedBitmapDrawable.setCornerRadius(bitmap.getWidth() / 2);
        //抗锯齿
        roundedBitmapDrawable.setAntiAlias(true);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

}
