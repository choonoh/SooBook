package com.example.soobook;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity{

    private EditText et_email, et_pwd;
    private TextView find_email_pwd, sign_up;
    private Button login_btn;
    private CheckBox auto_login;
    private FirebaseAuth firebaseAuth;

    private  static  boolean auto;
    private String user_email, user_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setLogin();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && auto) {
            Log.e(this.getClass().getName(), "자동로그인");
            Intent intent = new Intent(Login.this, Home.class);
            intent.putExtra("fragment","fri_lib");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast toast = Toast.makeText(Login.this, "자동로그인 성공", Toast.LENGTH_SHORT); toast.show();
            Handler handler = new Handler();
            handler.postDelayed(toast::cancel, 1000);
            startActivity(intent);
        }
        find_email_pwd.setOnClickListener(v -> {
            Intent intent=new Intent(Login.this, FindPw.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        sign_up.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Sign_up.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        login_btn.setOnClickListener(v -> {
            user_email = et_email.getText().toString();
            user_pwd = et_pwd.getText().toString();
            final Toast toast;
            Handler handler = new Handler();
            if (user_email.length() <= 0) {
                toast = Toast.makeText(Login.this, "이메일을 입력하셈유", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else if (user_pwd.length() <= 0) {
                toast = Toast.makeText(Login.this, "비밀번호를 입력하셈유", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else {
                startLogin();
            }
        });
    }
    public void setLogin() {
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        login_btn = findViewById(R.id.login_btn);
        sign_up = findViewById(R.id.sign_up);
        find_email_pwd = findViewById(R.id.find_email_pwd);
        auto_login = findViewById(R.id.auto_login);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void startLogin(){
        firebaseAuth.signInWithEmailAndPassword(et_email.getText().toString(), et_pwd.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        auto = auto_login.isChecked();
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.putExtra("fragment","fri_lib");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast toast = Toast.makeText(Login.this, "실패", Toast.LENGTH_SHORT); toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(toast::cancel, 1000);
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(Login.this, Home.class));
            finish();
        }
    }
}