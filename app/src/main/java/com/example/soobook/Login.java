package com.example.soobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity{
    private final String TAG ="failErr";
    private EditText et_email, et_pwd;
    private TextView find_email_pwd, sign_up;
    private Button login_btn;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setID();
        setEvents();
        find_email_pwd = (TextView) findViewById(R.id.find_email_pwd);
        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Sign_up.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        find_email_pwd.setOnClickListener(new View.OnClickListener() { //비밀번호 찾기 화면 이동
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, FindPw.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }

    public void setID() {
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        login_btn = findViewById(R.id.login_btn);
        sign_up = findViewById(R.id.sign_up);
        find_email_pwd = findViewById(R.id.find_email_pwd);
        firebaseAuth = FirebaseAuth.getInstance();

    }
    public void setEvents() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().length() <= 0 || et_pwd.getText().toString().length() <= 0) {
                    Toast.makeText(Login.this, "이메일 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT);
                    return;
                } else {
                    startLogin();}
            }

        });
    }


    public void startLogin(){
        firebaseAuth.signInWithEmailAndPassword(et_email.getText().toString(), et_pwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Login.this,"로긴실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}