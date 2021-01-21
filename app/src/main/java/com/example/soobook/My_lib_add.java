package com.example.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;


public class My_lib_add extends AppCompatActivity {

    ImageButton add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lib_add);

        add_btn = (ImageButton) findViewById(R.id.add_btn);

        add_btn.setOnClickListener(v -> {
            Intent intent = new Intent(My_lib_add.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("fragment", "my_lib");
            startActivity(intent);
        });


    }
}