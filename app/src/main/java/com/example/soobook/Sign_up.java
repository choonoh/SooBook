package com.example.soobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_up extends AppCompatActivity {
    private final String TAG ="failErr";
    private EditText et_email, et_pwd, et_pwd_check;
    private Button only_one_btn, sign_up_btn;
    private FirebaseAuth firebaseAuth;
    private String user_email, user_pwd, user_pwd_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setID();
        setEvents(); //버튼 이벤트 설정


    }
    public void setID() {
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_check = findViewById(R.id.et_pwd_check);
        only_one_btn = findViewById(R.id.only_one_btn);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void setEvents() {
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().length() <= 0 || et_pwd.getText().toString().length() <= 0) {
                    Toast.makeText(Sign_up.this, "이메일 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT);
                    return;
                }
                if (et_pwd.getText().toString().equals(et_pwd_check.getText().toString())) {
                    startSignup();
                } else {
                    Toast.makeText(Sign_up.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                }
            }

        });
    }
        public void startSignup() {
            firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_pwd.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(Sign_up.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        Toast.makeText(Sign_up.this, "회원가입 성공!", Toast.LENGTH_SHORT);
                        finish();
                    } else {
                        Log.w(TAG, "회원가입 실패", task.getException());
                        Toast.makeText(Sign_up.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

}