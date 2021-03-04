package com.example.campustradingplatform.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.campustradingplatform.R;

import java.io.Serializable;

public class Register1 extends AppCompatActivity {

    User user;
    EditText edName;
    EditText edRealName;
    RadioGroup rgSex;
    EditText edSchool;
    RadioGroup rgIdentity;
    EditText edNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page1);

        user = new User();

        Button next = (Button)findViewById(R.id.register_nextPage);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edName = (EditText)findViewById(R.id.register_name);
                edRealName = (EditText)findViewById(R.id.register_realName);
                rgSex = (RadioGroup)findViewById(R.id.register_sex);
                edSchool = (EditText)findViewById(R.id.register_school);
                rgIdentity = (RadioGroup)findViewById(R.id.register_identity);
                edNo = (EditText)findViewById(R.id.register_No);

                boolean flag = true;           //判断是否有空，有空则置为false


                //用户名
                String str = "";
                str = edName.getText().toString();
                if(!str.equals("")){
                    user.setUserName(str);     //设置user对象的属性
                }
                else{
                    flag = false;               //标识置为false，表示有空
                    Toast.makeText(Register1.this,"请输入用户名",Toast.LENGTH_SHORT).show();//显示为空的输入框
                }

                //姓名
                str = edRealName.getText().toString();
                if(!str.equals("")){
                    user.setName(str);
                }
                else{
                    flag = false;
                    Toast.makeText(Register1.this,"请输入姓名",Toast.LENGTH_SHORT).show();
                }

                //性别
                str = "";
                int i;
                for(i = 0;i < rgSex.getChildCount();i++){
                    RadioButton rb = (RadioButton)rgSex.getChildAt(i);
                    if(rb.isChecked()){
                        str = rb.getText().toString();
                        break;
                    }
                }
                if(!str.equals("")){
                    user.setSex(str);
                }
                else{
                    flag = false;
                    Toast.makeText(Register1.this,"请选择性别",Toast.LENGTH_SHORT).show();
                }

                //学校
                str = edSchool.getText().toString();
                if(!str.equals("")){
                    user.setSchool(str);
                }
                else{
                    flag = false;
                    Toast.makeText(Register1.this,"请输入学校",Toast.LENGTH_SHORT).show();
                }

                //身份
                str = "";
                for(i = 0;i < rgIdentity.getChildCount();i++){
                    RadioButton rb = (RadioButton)rgIdentity.getChildAt(i);
                    if(rb.isChecked()){
                        str = rb.getText().toString();
                        break;
                    }
                }
                if(!str.equals("")){
                    user.setIdentity(str);
                }
                else{
                    flag = false;
                    Toast.makeText(Register1.this,"请选择身份",Toast.LENGTH_SHORT).show();
                }

                //学号
                str = edNo.getText().toString();
                if(!str.equals("")){
                    user.setIdNum(str);
                }
                else{
                    flag = false;
                    Toast.makeText(Register1.this,"请输入学号/工号",Toast.LENGTH_SHORT).show();
                }



                if(flag) {
                    Intent intent = new Intent(Register1.this, Register2.class);
                    intent.putExtra("user", (Serializable) user);   //将user对象传给Register2活动,以便后续一起存入数据库
                    startActivity(intent);
                }
            }
        });
    }
}
