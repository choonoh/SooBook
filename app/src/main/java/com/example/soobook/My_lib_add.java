package com.example.soobook;

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
import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;
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


    Button btn_Insert;
    EditText edit_isbn;
    EditText edit_Age;
    TextView title;
    TextView author;
    TextView pub ;
    CheckBox check_good;
    CheckBox check_bad;
    String isbn;
    String ID;
    String name;
    long age;
    String rec = "";
    boolean inTitle = false, inAuthor = false, inPub = false;

    String Title = null, Author = null, Pub = null;

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lib_add);

        StrictMode.enableDefaults();
        EditText isbn = findViewById(R.id.isbn_txt);

        ImageButton sc = findViewById(R.id.isbn_sc);

        title = findViewById(R.id.book_title_add);
        author = findViewById(R.id.book_author_add);
        pub = findViewById(R.id.book_pub_add);
        edit_Age = findViewById(R.id.edit_age);
        edit_isbn = findViewById(R.id.isbn_txt);

        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        check_bad = (CheckBox) findViewById(R.id.check_bad);
        check_bad.setOnClickListener(this);
        check_good = (CheckBox) findViewById(R.id.check_good);
        check_good.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);

        getFirebaseDatabase();
        btn_Insert.setEnabled(true);

        sc.setOnClickListener(v -> {


            try{
                URL url = new URL("http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key=1af3f780faeed316e48de8f0e2541d43eecf78d212859aed298460eddff2bd16" +
                        "&result_style=xml&page_no=1&page_size=10&isbn="+isbn.getText().toString()); //검색 URL부분

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                System.out.println("파싱시작합니다.");

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

    public void setInsertMode(){
        title.setText(Title);
        author.setText(Author);
        pub.setText(Pub);
        edit_Age.setText("");
        check_bad.setChecked(false);
        check_good.setChecked(false);
        btn_Insert.setEnabled(true);

    }

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData);
            edit_isbn.setText(tempData[0].trim());
           title.setText(tempData[1].trim());
            author.setText(tempData[2].trim());
            edit_Age.setText(tempData[3].trim());
            if(tempData[4].trim().equals("good")){
                check_good.setChecked(true);
                rec = "추천";
            }else{
                check_bad.setChecked(true);
                rec = "비추천";
            }
            title.setEnabled(false);
            btn_Insert.setEnabled(false);

        }
    };

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(isbn,ID, name, age, rec);
            postValues = post.toMap();
        }
        childUpdates.put("/Book/"+ isbn, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {get.isbn, get.title, get.auth, String.valueOf(get.star), get.rec};
                    String Result = setTextLength(info[0], 10) + setTextLength(info[1], 10) + setTextLength(info[2], 10) + setTextLength(info[3], 10) + setTextLength(info[4], 10);
                    arrayData.add(Result);
                    arrayIndex.add(key);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3] + info[4]);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(arrayData);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("/Book/");
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public String setTextLength(String text, int length) {
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
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
                    getFirebaseDatabase();
                    setInsertMode();
                }else{
                    Toast.makeText(My_lib_add.this, "이미 존재하는 책 입니다. 다른 책등록하셈.", Toast.LENGTH_LONG).show();
                }
                edit_isbn.requestFocus();
                edit_isbn.setCursorVisible(true);
                break;

            case R.id.btn_select:
                getFirebaseDatabase();
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