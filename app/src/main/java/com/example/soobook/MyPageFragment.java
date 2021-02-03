package com.example.soobook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class MyPageFragment extends Fragment {

    ImageButton logout_btn;
    TextView admin_btn, add_btn, changepw_btn, logout_txt_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_page, container, false);
        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");
        admin_btn = root.findViewById(R.id.admin_btn);
        logout_btn = root.findViewById(R.id.logout_btn);
        add_btn = root.findViewById(R.id.add_btn);
        changepw_btn = root.findViewById(R.id.changpw_btn);
        logout_txt_btn = root.findViewById(R.id.logout_txt_btn);

        admin_btn.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), AdminFrnd.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        });
        add_btn.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Add_frnd.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        });

        changepw_btn.setOnClickListener(v -> {

            Intent intent=new Intent(getActivity(), FindPw.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        logout_btn.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();

            Intent intent = new Intent(getActivity(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.getActivity().finish();
        });

        logout_txt_btn.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();

            Intent intent = new Intent(getActivity(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.getActivity().finish();
        });

        return root;
    }
}
