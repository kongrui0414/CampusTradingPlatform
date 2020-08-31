package com.example.campustradingplatform.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.campustradingplatform.MainActivity;
import com.example.campustradingplatform.R;

public class PhoneNumLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_login2);

        final Button Login = (Button)findViewById(R.id.login_login2);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneNumLogin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView register = (TextView)findViewById(R.id.Login_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneNumLogin.this,Register1.class);
                startActivity(intent);
            }
        });

        TextView login2 = (TextView)findViewById(R.id.login_PswLogin);
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneNumLogin.this,Login.class);
                startActivity(intent);
            }
        });
    }
}
