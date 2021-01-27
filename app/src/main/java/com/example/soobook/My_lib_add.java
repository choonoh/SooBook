package com.example.soobook;

import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.StrictMode;
import android.widget.ImageButton;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import com.example.soobook.ui.MyLib.MyLibFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class My_lib_add  extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mPostReference;
    private FirebaseUser currentUser;

    ImageButton btn_Insert;
    EditText edit_isbn, edit_Age;
    TextView title, author, pub ;
    CheckBox check_good;
    CheckBox check_bad;

    long age;
    String rec = "";
    String isbn, ID, name, user_email;
    String Title = null, Author = null, Pub = null;
    boolean inTitle = false, inAuthor = false, inPub = false;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
//    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lib_add);

        StrictMode.enableDefaults();

        ImageButton sc = findViewById(R.id.isbn_sc);
        title = findViewById(R.id.book_title_add);
        author = findViewById(R.id.book_author_add);
        pub = findViewById(R.id.book_pub_add);
        edit_Age = findViewById(R.id.edit_age);
        edit_isbn = findViewById(R.id.isbn_txt);
        user_email = getIntent().getStringExtra("user_email");
        Log.e(this.getClass().getName(), user_email);

        btn_Insert = findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        check_bad = findViewById(R.id.check_bad);
        check_bad.setOnClickListener(this);
        check_good = findViewById(R.id.check_good);
        check_good.setOnClickListener(this);
        sc.setOnClickListener(v -> {
            try{
                URL url = new URL("http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key=1af3f780faeed316e48de8f0e2541d43eecf78d212859aed298460eddff2bd16" +
                        "&result_style=xml&page_no=1&page_size=10&isbn="+edit_isbn.getText().toString()); //검색 URL부분

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                Log.e(this.getClass().getName(), "start parsing");

                while (parserEvent != XmlPullParser.END_DOCUMENT){
                    switch(parserEvent){
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                            if(parser.getName().equals("TITLE")){ //title 만나면 내용을 받을수 있게 하자
                                inTitle = true;
                            }
                            if(parser.getName().equals("AUTHOR")){ //address 만나면 내용을 받을수 있게 하자
                                inAuthor = true;
                            }
                            if(parser.getName().equals("PUBLISHER")){ //mapx 만나면 내용을 받을수 있게 하자
                                inPub = true;
                            }
                            break;
                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            if(inTitle){ //isTitle이 true일 때 태그의 내용을 저장.
                                Title = parser.getText();
                                inTitle = false;
                            }
                            if(inAuthor){ //isAddress이 true일 때 태그의 내용을 저장.
                                Author = parser.getText();
                                inAuthor = false;
                            }
                            if(inPub){ //isMapx이 true일 때 태그의 내용을 저장.
                                Pub = parser.getText();
                                inPub = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("docs")){
                                title.setText(Title);
                                author.setText(Author);
                                pub.setText(Pub);
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch(Exception e){
                Log.e(this.getClass().getName(), "error", e);
            }
        });
    }
    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(isbn, ID, name, age, rec);
            postValues = post.toMap();

        }

  //      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //        String email = user.getEmail();

        childUpdates.put("/Book/"+ isbn, postValues);
        mPostReference.updateChildren(childUpdates);
    }
    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(isbn);
        return IsExist;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:

               isbn = edit_isbn.getText().toString();
                ID = title.getText().toString();
                name = author.getText().toString();
                age = Long.parseLong(edit_Age.getText().toString());

                title.requestFocus();
                title.setCursorVisible(true);

                if(!IsExistID()){
                    postFirebaseDatabase(true);

                }else{
                    Toast.makeText(My_lib_add.this, "이미 존재하는 책 입니다. 다른 책등록하셈.", Toast.LENGTH_LONG).show();
                }
                edit_isbn.requestFocus();
                edit_isbn.setCursorVisible(true);
                break;

            case R.id.check_good:
                check_bad.setChecked(false);
                rec = "추천";
                break;

            case R.id.check_bad:
                check_good.setChecked(false);
                rec = "비추천";
                break;
        }
    }
}