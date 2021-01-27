package com.example.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.Toast;

import com.example.soobook.ui.MyLib.MyLibFragment;
import com.example.soobook.ui.FriLib.FriLibFragment;
import com.example.soobook.ui.FindLib.FindLibFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    private FriLibFragment friLibFragment;
    private MyLibFragment myLibFragment;
    private FindLibFragment findlibFragment;

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private Button logout_btn;
    private String fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);


        setSupportActionBar(toolbar);
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
        try{
            switch (fragment) {
                case "my_lib":
                    getSupportFragmentManager().beginTransaction().add(R.id.container, myLibFragment).commit();
                    break;
                case "find_lib":
                    getSupportFragmentManager().beginTransaction().add(R.id.container, findlibFragment).commit();
                    break;
                default:
                case "fri_lib":
                    getSupportFragmentManager().beginTransaction().add(R.id.container, friLibFragment).commit();
                    break;
            }
        } catch (Exception e) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, friLibFragment).commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_logOut) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut(); //로그아웃
            Intent intent=new Intent(Home.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu1) {
            onFragmentSelected(0, null);
        } else if(id == R.id.menu2) {
            onFragmentSelected(1, null);
        } else if(id == R.id.menu3) {
            onFragmentSelected(2, null);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment curFragment = null;

        if (position == 0) {
            curFragment = friLibFragment;
            toolbar.setTitle(("친구의 서재"));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
        } else if (position == 1) {
            curFragment = myLibFragment;
            toolbar.setTitle(("내 서재"));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
        } else if (position == 2) {
            curFragment = findlibFragment;
            toolbar.setTitle(("도서관 지도"));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
        }
    }
}