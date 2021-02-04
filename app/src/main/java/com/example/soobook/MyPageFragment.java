package com.example.soobook;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
    TextView admin_btn, add_btn, changepw_btn, logout_txt_btn, del_id_btn;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_page, container, false);
        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");
        admin_btn = root.findViewById(R.id.admin_btn);
        add_btn = root.findViewById(R.id.add_btn);
        changepw_btn = root.findViewById(R.id.changpw_btn);
        logout_txt_btn = root.findViewById(R.id.logout_txt_btn);
        del_id_btn = root.findViewById(R.id.del_id_btn);


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
            Intent intent = new Intent(getActivity(), FindPw.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        del_id_btn.setOnClickListener(v -> {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("회원탈퇴")
                    .setMessage("탈퇴시 모든 정보가 삭제되며 복구하실 수 없습니다.\n그래도 회원탈퇴를 진행하시겠습니까?")
                    .setPositiveButton("네", (dialog1, which) -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                        DatabaseReference data = database.getReference("User/" + user_UID);
                        data.removeValue();
                        DatabaseReference data2 = database.getReference("Friend/" + user_UID);
                        data2.removeValue();
                        DatabaseReference data3 = database.getReference("Book/" + user_UID);
                        data3.removeValue();

                        mAuth = FirebaseAuth.getInstance();
                        mAuth.getCurrentUser().delete();

                        Toast.makeText(getActivity(), "지금까지 수북을 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();

                        getActivity().finishAffinity();

                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "계정 탈퇴를 취소했습니다.", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .create()
                    .show();
        });


        logout_txt_btn.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();

            Intent intent = new Intent(getActivity(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });

        return root;
    }
}