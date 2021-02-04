package com.example.soobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class BeforeFindLibFragment extends Fragment {

    boolean go_to_map = true;
    String user_email, user_UID;

    FindLibFragment findLibFragment;
    TextView permission;
    ImageButton map_btn;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_before_find_lib, container, false);

        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_REQUEST_CODE);

        user_email = getArguments().getString("user_email");
        user_UID = getArguments().getString("user_UID");

        map_btn = rootView.findViewById(R.id.map_btn);
        permission = rootView.findViewById(R.id.permission);
        if(go_to_map) {
            permission.setText("넌 지도로 갈 수 이따");
        } else {
            permission.setText("넌 지도로 갈 수 없따");
        }
        map_btn.setOnClickListener(v -> {
            if(go_to_map) {
                ((Home)getActivity()).replaceFragment(FindLibFragment.newInstance());
            }
        });


        return rootView;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                go_to_map = true;
//                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

            }
        }
    }
}