package com.example.soobook;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MyPageFragment extends Fragment {

    FloatingActionButton add_frnd_btn;
    ImageButton logout_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_page, container, false);

        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");
        add_frnd_btn = root.findViewById(R.id.add_frnd_btn);
        logout_btn = root.findViewById(R.id.logout_btn);

        Log.e(this.getClass().getName(), user_email + ", " + user_UID);

        add_frnd_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminFrnd.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        return root;
    }
}