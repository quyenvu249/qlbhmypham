package com.example.qlbanhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    TextView tvUsername;
    ListView lvList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("Người dùng");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        tvUsername = findViewById(R.id.tvUsername);
        lvList = findViewById(R.id.lvList);

        String func[] = new String[]{"Đổi mật khẩu","Đăng xuất"};
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < func.length; i++) {
            arrayList.add(func[i]);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_list_item_1, arrayList);
        lvList.setAdapter(arrayAdapter);
        Intent intent = getIntent();
        tvUsername.setText(intent.getStringExtra("username"));
    }
}