package com.example.campustradingplatform.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campustradingplatform.MainActivity;
import com.example.campustradingplatform.R;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends AppCompatActivity {
    //连接的数据库
    String url = "jdbc:mysql://121.37.212.124:3306/ctp";
    //连接数据库的用户名
    String userName = "root";
    //连接数据库的密码
    String psw = "ABC123!!";
    Connection connection = null;

    EditText edPhoneNum;
    EditText edPsw;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            edPhoneNum = (EditText)findViewById(R.id.login_phoneNum);
            edPsw = (EditText)findViewById(R.id.login_password);
            String PhoneNum = edPhoneNum.getText().toString();
            String Psw = edPsw.getText().toString();

            try {
                //加载mysql驱动
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Log.d("aaa","加载数据库驱动成功");
            } catch (Exception e) {
                Log.d("aaa","加载数据库驱动失败！");
                e.printStackTrace();
            }

            try{
                //连接数据库
                connection = DriverManager.getConnection(url,userName,psw);
                Log.d("aaa","连接数据库成功");
                //数据库操作
                PreparedStatement ps;
                ps = connection.prepareStatement("select * from user where phoneNum = ? and psw = ?");
                ps.setString(1,PhoneNum);
                ps.setString(2,Psw);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    User user = new User();
                    user.setName(rs.getString("name"));
                    user.setPhoneNum(rs.getString("phoneNum"));
                    user.setIdNum(rs.getString("idNum"));
                    user.setIdentity(rs.getString("identity"));
                    user.setUserName(rs.getString("username"));
                    user.setSchool(rs.getString("school"));
                    user.setPsw(rs.getString("psw"));
                    user.setSex(rs.getString("sex"));
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    intent.putExtra("user2", (Serializable) user);
                    startActivity(intent);
                }else {
                    Toast.makeText(Login.this,"登录失败，请检查用户名和密码",Toast.LENGTH_SHORT).show();
                }
                connection.close();
            }catch (Exception e){
                Log.d("aaa","连接数据库失败");
                e.printStackTrace();
            }
            Looper.loop();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_login1);
        final Button Login = (Button)findViewById(R.id.login_login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });

        TextView register = (TextView)findViewById(R.id.Login_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register1.class);
                startActivity(intent);
            }
        });

        TextView login2 = (TextView)findViewById(R.id.login_PhoneNumLogin);
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,PhoneNumLogin.class);
                startActivity(intent);
            }
        });

    }
}
