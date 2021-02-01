package com.example.soobook;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {
    FriLibFragment friLibFragment;
    MyLibFragment myLibFragment;

    FindLibFragment findLibFragment;
    MyPageFragment myPageFragment;
    FloatingActionButton add_book_btn;
    String user_email, user_UID,user_UID_login;
    String bottom_frag = "fri_lib";
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottom_frag = getIntent().getStringExtra("fragment");
        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");

        Log.e(this.getClass().getName(), user_email + ", " + user_UID);

       // add_book_btn = findViewById(R.id.add_book_btn);
        friLibFragment = new FriLibFragment();
        myLibFragment = new MyLibFragment();
        findLibFragment = new FindLibFragment();
        myPageFragment = new MyPageFragment();
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        switch(bottom_frag) {
            case "fri_lib":
                /*
                getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
            Bundle toFriendLibFrag = new Bundle();
            toFriendLibFrag.putString("user_email",user_email);
            toFriendLibFrag.putString("user_UID",user_UID);
            friLibFragment.setArguments(toFriendLibFrag);
                 */
                getSupportFragmentManager().beginTransaction().replace(R.id.container, friLibFragment).commit();
                Bundle toFriendLibFrag = new Bundle();
                toFriendLibFrag.putString("user_email",user_email);
                toFriendLibFrag.putString("user_UID",user_UID);
                friLibFragment.setArguments(toFriendLibFrag);
                bottomNavigationView.setSelectedItemId(R.id.fri_lib);
                break;
            case "my_lib":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myLibFragment).commit();
                Bundle toMyLibFrag = new Bundle();
                toMyLibFrag.putString("user_email",user_email);
                toMyLibFrag.putString("user_UID",user_UID);
                myLibFragment.setArguments(toMyLibFrag);
                bottomNavigationView.setSelectedItemId(R.id.my_lib);
                break;
            case "find_lib":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, findLibFragment).commit();
                Bundle findLibFrag = new Bundle();
                findLibFrag.putString("user_email",user_email);
                findLibFrag.putString("user_UID",user_UID);
                findLibFragment.setArguments(findLibFrag);
                bottomNavigationView.setSelectedItemId(R.id.find_lib);
                break;
            case "myPage":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myPageFragment).commit();
        //        Bundle mypageFrag = new Bundle();
         //       mypageFrag.putString("user_email",user_email);
          //      mypageFrag.putString("user_UID",user_UID);
          //      findLibFragment.setArguments(mypageFrag);
                bottomNavigationView.setSelectedItemId(R.id.my_page);
                break;
        }
//        add_book_btn.setOnClickListener((View.OnClickListener) view -> {
//            add_book_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF0000")));
//            bottomNavigationView.setSelectedItemId(R.id.blank);
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, sosMainFragment).commit();
//        });
        bottomNavigationView.setOnNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.fri_lib:
//                            sos_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4472C4")));
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, friLibFragment).commit();
                            Bundle toFirLibFrag = new Bundle();
                            toFirLibFrag.putString("user_email",user_email);
                            toFirLibFrag.putString("user_UID",user_UID);
                            friLibFragment.setArguments(toFirLibFrag);
                            return true;
                        case R.id.my_lib:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, myLibFragment).commit();
                            Bundle toFriendLibFrag = new Bundle();
                            toFriendLibFrag.putString("user_email",user_email);
                            toFriendLibFrag.putString("user_UID",user_UID);
                            myLibFragment.setArguments(toFriendLibFrag);
                            return true;
//                        case R.id.blank:
//                            getSupportFragmentManager().beginTransaction().replace(R.id.container, sosMainFragment).commit();
                        case R.id.find_lib:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, findLibFragment).commit();
                            Bundle toFindLibFrag = new Bundle();
                            toFindLibFrag.putString("user_email",user_email);
                            toFindLibFrag.putString("user_UID",user_UID);
                            findLibFragment.setArguments(toFindLibFrag);
                            return true;
                        case R.id.my_page:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, myPageFragment).commit();
                //            Bundle toMypageFrag = new Bundle();
                 //           toMypageFrag.putString("user_email",user_email);
                   //         toMypageFrag.putString("user_UID",user_UID);
                     //       findLibFragment.setArguments(toMypageFrag);
                            return true;
                    }
                    return false;
                }
        );
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("리얼루 나갈거임??");
        builder.setPositiveButton("나갈건디", (dialog, id) -> {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        });
        builder.setNegativeButton("취소 ㅋ", null);
        dialog = builder.create();
        dialog.show();
    }
}