package com.example.soobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText et_email, et_pwd;
    TextView find_email_pwd, sign_up;
    Button login_btn;

    String user_email, user_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        find_email_pwd = findViewById(R.id.find_email_pwd);
        sign_up = findViewById(R.id.sign_up);
        login_btn = findViewById(R.id.login_btn);

        user_email = et_email.getText().toString();
        user_pwd = et_pwd.getText().toString();

        find_email_pwd.setOnClickListener(v -> {
            Toast.makeText(this, "정보 찾기 기능은 아직 구현을 못해써 ㅜㅜ.", Toast.LENGTH_LONG).show();
        });
        sign_up.setOnClickListener(v -> {
            Toast.makeText(this, "회원가입 기능은 아직 구현을 못해써 ㅜㅜ", Toast.LENGTH_LONG).show();
        });
        login_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("fragment", "fri_lib");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}