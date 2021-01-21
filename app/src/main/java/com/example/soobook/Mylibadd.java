package com.example.soobook;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.soobook.ui.gallery.MylibFragment;

public class Mylibadd extends AppCompatActivity {

    ImageButton add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylib_add);

        add_btn = (ImageButton) findViewById(R.id.add_btn);


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mylibadd.this, MylibFragment.class);
                startActivity(intent);
            }
        });


    }
}