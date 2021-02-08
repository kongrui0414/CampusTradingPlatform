package com.example.campustradingplatform.UtilTools;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class GlobalUtil {

    //隐藏软键盘
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static Activity getActivityByContext(Context context){
        while(context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static  void getKeyBoard(EditText editText){
        //调用系统输入法
        InputMethodManager inputManager = (InputMethodManager) editText
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }
}
