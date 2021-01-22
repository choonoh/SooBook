package com.example.soobook;

import android.os.Bundle;
import android.view.MenuItem;

import android.widget.Toast;

import com.example.soobook.ui.MyLib.MyLibFragment;
import com.example.soobook.ui.FriLib.FriLibFragment;
import com.example.soobook.ui.FindLib.FindLibFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    FriLibFragment friLibFragment;
    MyLibFragment myLibFragment;
   FindLibFragment findlibFragment;

    DrawerLayout drawer;
    Toolbar toolbar;

    String fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        friLibFragment = new FriLibFragment();
        myLibFragment = new MyLibFragment();
        findlibFragment = new FindLibFragment();

        fragment = getIntent().getStringExtra("fragment");
        switch (fragment) {
            case "my_lib":
               getSupportFragmentManager().beginTransaction().add(R.id.container, myLibFragment).commit();
                break;
            case "find_lib":
                getSupportFragmentManager().beginTransaction().add(R.id.container, findlibFragment).commit();
            default:
            case "fri_lib":
                getSupportFragmentManager().beginTransaction().add(R.id.container, friLibFragment).commit();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu1) {
            Toast.makeText(this, "첫 번째 메뉴 선택됨.", Toast.LENGTH_LONG).show();
            onFragmentSelected(0, null);
        } else if(id == R.id.menu2) {
            Toast.makeText(this, "두 번째 메뉴 선택됨.", Toast.LENGTH_LONG).show();
            onFragmentSelected(1, null);
        } else if(id == R.id.menu3) {
            Toast.makeText(this, "세 번째 메뉴 선택됨.", Toast.LENGTH_LONG).show();
            onFragmentSelected(2, null);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment curFragment = null;

        if(position == 0) {
            curFragment = friLibFragment;
            toolbar.setTitle(("친구의 서재"));
        } else if(position == 1) {
            curFragment = myLibFragment;
            toolbar.setTitle(("내 서재"));
        } else if(position == 2) {
            curFragment = findlibFragment;
            toolbar.setTitle(("세 번째 화면"));
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
    }
}