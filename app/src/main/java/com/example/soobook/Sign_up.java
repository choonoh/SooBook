package com.example.soobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Sign_up extends AppCompatActivity {

    EditText et_email, et_pwd, et_pwd_check;
    Button only_one_btn, sign_up_btn;

    String user_email, user_pwd, user_pwd_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_check = findViewById(R.id.et_pwd_check);
        only_one_btn = findViewById(R.id.only_one_btn);
        sign_up_btn = findViewById(R.id.sign_up_btn);

        user_email = et_email.getText().toString();
        user_pwd = et_pwd.getText().toString();
        user_pwd_check = et_pwd_check.getText().toString();

        only_one_btn.setOnClickListener(v -> {
            Toast.makeText(this, "중복 확인 기능은 아직 구현을 못해써 ㅜㅜ.", Toast.LENGTH_LONG).show();
        });
        sign_up_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Sign_up.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}