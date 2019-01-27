package com.octet.qbit.wallpaperappadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onClickManageCat(View view) {
        startActivity(new Intent(HomeActivity.this, CategoryActivity.class));
    }

    public void onClickAddImage(View view) {
        startActivity(new Intent(HomeActivity.this, WallpaperActivity.class));
    }

    public void onClickLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }

}
