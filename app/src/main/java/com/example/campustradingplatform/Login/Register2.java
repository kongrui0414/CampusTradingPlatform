package com.example.campustradingplatform.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.campustradingplatform.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.Class.forName;

public class Register2 extends AppCompatActivity {

    //连接的数据库
    String url = "jdbc:mysql://121.37.212.124:3306/ctp";
    //连接数据库的用户名
    String userName = "root";
    //连接数据库的密码
    String psw = "ABC123!!";
    Connection connection = null;

    User user;
    EditText edPhoneNum;
    EditText edCaptcha;
    EditText edPsw;
    EditText edSamePsw;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();

            //获取对应的输入框
            edPhoneNum = (EditText)findViewById(R.id.register_phoneNum);
            edCaptcha = (EditText)findViewById(R.id.register_captcha);
            edPsw = (EditText)findViewById(R.id.register_password);
            edSamePsw = (EditText)findViewById(R.id.register_samePassword);
            String phoneNum = edPhoneNum.getText().toString();
            String captcha = edCaptcha.getText().toString();
            String Psw = edPsw.getText().toString();
            String samePsw = edSamePsw.getText().toString();
            boolean flag = true;           //判断是否有空，有空则置为false
            try {
                //加载mysql驱动
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Log.d("Hello", "加载JDBC驱动成功！");
            } catch (Exception e) {
                Log.d("Hello", "加载JDBC驱动失败！");
                e.printStackTrace();
            }
            try {
                //获取与数据库连接
                connection = DriverManager.getConnection(url,userName,psw);
                Log.d("Hello", "数据库连接成功！");
                //数据库操作
                PreparedStatement ps;
                ps = connection.prepareStatement("select * from user where phoneNum = ?");
                ps.setString(1,phoneNum);
                ResultSet rs = ps.executeQuery();
                if(phoneNum.equals("")){
                    flag = false;
                    Toast.makeText(Register2.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                }
                else if(captcha.equals("")){
                    flag = false;
                    Toast.makeText(Register2.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                }
                else if(Psw.equals("")){
                    flag = false;
                    Toast.makeText(Register2.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }
                else if(samePsw.equals("")){
                    flag = false;
                    Toast.makeText(Register2.this,"请确认密码",Toast.LENGTH_SHORT).show();
                }


                if(flag) {
                    if (rs.next()) {
                        Toast.makeText(Register2.this, "手机号已注册", Toast.LENGTH_SHORT).show();
                    } else if (!Psw.equals(samePsw)) {
                        Toast.makeText(Register2.this, "密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        ps = connection.prepareStatement("insert into user values(null,?,?,?,?,?,?,?,?)");
                        ps.setString(1, phoneNum);
                        ps.setString(2, Psw);
                        ps.setString(3, user.getUserName());
                        ps.setString(4, user.getName());
                        ps.setString(5, user.getSex());
                        ps.setString(6, user.getSchool());
                        ps.setString(7, user.getIdentity());
                        ps.setString(8, user.getIdNum());
                        ps.executeUpdate();
                        Toast.makeText(Register2.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register2.this, Login.class);
                        startActivity(intent);
                    }
                }
                connection.close();
            }catch (Exception e){
                Log.d("Hello", "数据库连接失败！");
                e.printStackTrace();
            }

            Looper.loop();
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page2);

        user = (User)getIntent().getSerializableExtra("user");

        final Button button = (Button)findViewById(R.id.register_registerFinish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });
    }
}
