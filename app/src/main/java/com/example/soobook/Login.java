package com.example.soobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Login extends AppCompatActivity{

    private EditText et_email, et_pwd;
    private TextView find_email_pwd, sign_up;
    private Button login_btn;
    private CheckBox auto_login;
    private FirebaseAuth firebaseAuth;
    private String auto_email, auto_pwd, user_email, user_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setLogin();

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
                if(auto_login.isChecked()) {
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    //SharedPreferences.Editor 통해 login_email, login_pass 저장
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("auto_email", user_email);
                    autoLogin.putString("inputPwd", user_pwd);
                    autoLogin.apply();
                }
                startLogin();
            }
        });
    }
    /*
    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
    auto_email = auto.getString("auto_email",null);
    auto_pwd = auto.getString("auto_pwd",null);

    if(auto_email !=null && auto_pwd != null) {
        Toast.makeText(Login.this, ("자동 로그인 완룡!"), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Login.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
     */
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
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.putExtra("fragment","fri_lib");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(Login.this,"로긴실패",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}