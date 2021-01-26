package com.example.soobook;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
    Button btn_Search;
    EditText edit_Isbn;
    EditText edit_ID;
    EditText edit_Name;
    EditText edit_Age;
    TextView text_ID;
    TextView text_Name;
    TextView text_Age;
    TextView text_rec;
    CheckBox check_good;
    CheckBox check_bad;
    String isbn;
    String ID;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String Email = user.getEmail();
    String name;
    long age;
    String rec = "";


    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lib_add);



        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        btn_Search=(Button)findViewById(R.id.search_btn);
        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_Name = (EditText) findViewById(R.id.edit_named);
        edit_Age = (EditText) findViewById(R.id.edit_age);
        edit_Isbn = (EditText)findViewById(R.id.edit_isbn);
        text_ID = (TextView) findViewById(R.id.text_idd);
        text_Name = (TextView) findViewById(R.id.text_name);
        text_Age = (TextView) findViewById(R.id.text_age);
        text_rec= (TextView) findViewById(R.id.text_rec);
        check_bad = (CheckBox) findViewById(R.id.check_bad);
        check_bad.setOnClickListener(this);
        check_good = (CheckBox) findViewById(R.id.check_good);
        check_good.setOnClickListener(this);


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        getFirebaseDatabase();
        btn_Insert.setEnabled(true);
    }

    public void setInsertMode(){
        edit_Isbn.setText("");
        edit_ID.setText("");
        edit_Name.setText("");
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
            edit_Isbn.setText(tempData[0].trim());
            edit_ID.setText(tempData[1].trim());
            edit_Name.setText(tempData[2].trim());
            edit_Age.setText(tempData[3].trim());
            if(tempData[4].trim().equals("good")){
                check_good.setChecked(true);
                rec = "추천";
            }else{
                check_bad.setChecked(true);
                rec = "비추천";
            }
            edit_ID.setEnabled(false);
            btn_Insert.setEnabled(false);

        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            final String[] nowData = arrayData.get(position).split("\\s+");
            ID = nowData[0];
            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3]+ ", " + nowData[4];
            AlertDialog.Builder dialog = new AlertDialog.Builder(My_lib_add.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postFirebaseDatabase(false);
                            getFirebaseDatabase();
                            setInsertMode();
                            edit_ID.setEnabled(true);
                            Toast.makeText(My_lib_add.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(My_lib_add.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            setInsertMode();
                            edit_ID.setEnabled(true);
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(ID);
        return IsExist;
    }

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(isbn,ID, name, age, rec);
            postValues = post.toMap();
        }
        childUpdates.put("/Book/"+ ID, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {get.isbn, get.title, get.auth, String.valueOf(get.star), get.rec};
                    String Result = setTextLength(info[0],10) + setTextLength(info[1],10) + setTextLength(info[2],10) + setTextLength(info[3],10)+ setTextLength(info[4],10);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                isbn = edit_Isbn.getText().toString();
                ID = edit_ID.getText().toString();
                name = edit_Name.getText().toString();
                age = Long.parseLong(edit_Age.getText().toString());
                if(!IsExistID()){
                    postFirebaseDatabase(true);
                    getFirebaseDatabase();
                    setInsertMode();
                }else{
                    Toast.makeText(My_lib_add.this, "이미 존재하는 ID 입니다. 다른 ID로 설정해주세요.", Toast.LENGTH_LONG).show();
                }
                edit_ID.requestFocus();
                edit_ID.setCursorVisible(true);
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