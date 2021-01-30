package com.example.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.soobook.ui.MyLib.MyLibFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MyBookDetailView extends AppCompatActivity {

    TextView isbn, title, auth, pub, star, rec, owner;
    String isbn_txt, title_txt, auth_txt, pub_txt, star_txt, rec_txt, owner_txt,uid;
    ImageButton del_btn;
    private FirebaseAuth firebaseAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_detail_view);
       del_btn = findViewById(R.id.del_btn);


        del_btn.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

                    DatabaseReference data = database.getReference("Book/"+uid+"/"+isbn_txt);
                    data.removeValue();

            Intent intent = new Intent(MyBookDetailView.this,Home.class);
            intent.putExtra("user_email", owner_txt);
            intent.putExtra("user_UID", uid);
            intent.putExtra("fragment", "my_lib");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
                });

   setDetailView();
   getIntentString();
   setTextViews();
    }
    public void setDetailView() {
       isbn = findViewById(R.id.book_isbn);
       title = findViewById(R.id.book_title);
       auth = findViewById(R.id.book_auth);
       pub = findViewById(R.id.book_pub);
       star = findViewById(R.id.book_star);
       rec = findViewById(R.id.book_rec);


    }
    public void getIntentString() {
        uid = getIntent().getStringExtra("uid");
        owner_txt = getIntent().getStringExtra("owner");
        isbn_txt= getIntent().getStringExtra("isbn");
        title_txt= getIntent().getStringExtra("title");
        auth_txt= getIntent().getStringExtra("auth");
        pub_txt= getIntent().getStringExtra("pub");
        star_txt= getIntent().getStringExtra("star");
        rec_txt= getIntent().getStringExtra("rec");

    }
    public void setTextViews() {
        isbn.setText(isbn_txt);
        title.setText(title_txt);
        auth.setText(auth_txt);
        pub.setText(pub_txt);
        star.setText(star_txt);
        rec.setText(rec_txt);

    }
}