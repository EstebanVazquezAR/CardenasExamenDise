package com.example.menu_alimentos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

public class Menu extends AppCompatActivity {

    private Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.window = getWindow();
        window.setNavigationBarColor(getResources().getColor(R.color.barcolor2));
        window.setStatusBarColor(getResources().getColor(R.color.navcolor2));
    }
}