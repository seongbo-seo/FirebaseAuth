package com.example.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button loginButton, signUpButton, pwFindButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton =findViewById(R.id.loginButton);
        signUpButton =findViewById(R.id.signUpButton);
        pwFindButton =findViewById(R.id.pwFindButton);

        //회원가입 버튼을 눌렀을 때 전화번호로 인증하러 보내는 메소드
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PhoneAuth.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),MainFrame.class);
                startActivity(intent1);
            }
        });
    }



}

