package com.example.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soobook.ui.FriLib.FriLibFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Add_frnd extends AppCompatActivity {

    private DatabaseReference mPostReference;

    private ImageButton make_frnd;
    private EditText email_frnd;
    String email_frnd_text = email_frnd.getText().toString();
    private String user_UID = "11111111"; //test
    static ArrayList<String> arrayIndex = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frnd);
        make_frnd = findViewById(R.id.make_frnd);

    }
}

/*
    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        email_frnd = findViewById(R.id.email_frnd);

        if(add){
            FirebaseuserPost post = new FirebaseuserPost(email_frnd_text);
            postValues = post.toMap();

        }

        String root ="/Frnd/"+user_UID+"/"+email_frnd_text;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);
    }
    public boolean IsExistID() {
        boolean IsExist = arrayIndex.contains(email_frnd_text);
        return IsExist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.make_frnd:
                email_frnd_text = email_frnd.getText().toString();

                if(!IsExistID()){
                    postFirebaseDatabase(true);
                }else{
                    Toast.makeText(Add_frnd.this, "이미 존재하는 칭구.", Toast.LENGTH_LONG).show();
                }

                break;

        }



}}
*/