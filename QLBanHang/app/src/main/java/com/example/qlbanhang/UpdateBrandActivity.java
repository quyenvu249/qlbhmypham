package com.example.qlbanhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class UpdateBrandActivity extends AppCompatActivity{
    ImageView imgTH;
    Button btnSuaTH;
    EditText edTenTH;
    String ID, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_brand);
        setTitle("Cập nhật thương hiệu");
        imgTH = findViewById(R.id.imgTH);
        btnSuaTH = findViewById(R.id.btnSuaTH);
        edTenTH = findViewById(R.id.edTenTH);

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

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bdBr");
        link = bundle.getString("brLink");
        Picasso.get().load(link).into(imgTH);
        edTenTH.setText(bundle.getString("brName"));
        ID = bundle.getString("brID");

        btnSuaTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBr(ID, edTenTH.getText().toString(), link);
                Toast.makeText(UpdateBrandActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateBrandActivity.this,BrandActivity.class));
            }
        });
    }

    private void updateBr(String brID, String brName, String brLink) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Brands").child(ID);
        Brand brand = new Brand(brID, brName, brLink);
        mData.setValue(brand);
    }

}