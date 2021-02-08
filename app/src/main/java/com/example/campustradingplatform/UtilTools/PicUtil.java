package com.example.campustradingplatform.UtilTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.campustradingplatform.R;

public class PicUtil {

    //圆角图片
    public static void roundBitmap(ImageView imageView, Context context){
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
