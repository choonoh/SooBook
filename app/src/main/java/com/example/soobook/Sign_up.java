package com.example.soobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_up extends AppCompatActivity {

    private EditText et_email, et_pwd, et_pwd_check;
    private Button only_one_btn, sign_up_btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setID();
        setEvents();
    }
    public void setID() {
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_check = findViewById(R.id.et_pwd_check);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void setEvents() {
        sign_up_btn.setOnClickListener(v -> {
            Toast toast;
            Handler handler = new Handler();
            if (et_email.getText().toString().length() <= 0) {
                toast = Toast.makeText(Sign_up.this, "이메일 입력 ㅡㅡ", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else if (et_pwd.getText().toString().length() <= 0 || et_pwd_check.getText().toString().length() <= 0) {
                toast = Toast.makeText(Sign_up.this, "비밀번호 입력 ㅡㅡ", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else if(!et_pwd.getText().toString().equals(et_pwd_check.getText().toString())) {
                toast = Toast.makeText(Sign_up.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            }else {
                startSignUp();
            }
        });
    }
    public void startSignUp() {
        firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_pwd.getText().toString()).addOnCompleteListener(this, task -> {
            Toast toast;
            Handler handler = new Handler();
            if (task.isSuccessful()) {
                currentUser = firebaseAuth.getCurrentUser();
                firebaseAuth.signOut();
                Intent intent = new Intent(Sign_up.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                toast = Toast.makeText(Sign_up.this, "회원가입 성공!", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
                finish();
            } else {
                toast = Toast.makeText(Sign_up.this, "잉 이미 있는 이메일이여", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            }
        });
    }
}