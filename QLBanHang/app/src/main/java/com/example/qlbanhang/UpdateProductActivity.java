package com.example.qlbanhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UpdateProductActivity extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    EditText edTenSP, edNSX, edHSD, edGiaNhap, edMoTa;
    TextView tvGiaBan;
    Button btnPicNSX, btnPicHSD, btnSuaSP;
    ImageView imgSP;
    Calendar calendar1, calendar2;
    String id, prLink, brand;
    Double exPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        setTitle("Cập nhật sản phẩm");

        AnhXa();

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        Bundle bundle = intent.getBundleExtra("bdPr");
        id = bundle.getString("proID");
        prLink = bundle.getString("proLink");
        brand = bundle.getString("proBrand");
        Picasso.get().load(prLink).into(imgSP);
        edTenSP.setText(bundle.getString("proName"));
        edNSX.setText(bundle.getString("NSX"));
        edHSD.setText(bundle.getString("HSD"));
        edGiaNhap.setText(bundle.getDouble("imPrice") + "");
        edMoTa.setText(bundle.getString("proDes"));

        btnSuaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(prLink, id, edTenSP.getText().toString(), brand, edNSX.getText().toString(), edHSD.getText().toString(), edMoTa.getText().toString(), Double.parseDouble(edGiaNhap.getText().toString()), Double.parseDouble(edGiaNhap.getText().toString())*1.3);
                Toast.makeText(UpdateProductActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateProductActivity.this,ProductActivity.class));
            }
        });
    }

    private void updateProduct(String prLink, String prID, String prName, String prBrand, String NSX, String HSD, String prDes, double imPrice, double exPrice) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Products").child(id);
        Product product = new Product(prLink, prID, prName, prBrand, NSX, HSD, prDes, imPrice, exPrice);
        mData.setValue(product);
    }

    private void AnhXa() {
        edTenSP = findViewById(R.id.edTenSP);
        edNSX = findViewById(R.id.edNSX);
        edHSD = findViewById(R.id.edHSD);
        edGiaNhap = findViewById(R.id.edGiaNhap);
        edMoTa = findViewById(R.id.edMoTa);
        btnPicNSX = findViewById(R.id.btnPicNSX);
        btnPicHSD = findViewById(R.id.btnPicHSD);
        btnSuaSP = findViewById(R.id.btnSuaSP);
        imgSP = findViewById(R.id.imgSP);
    }

    public void datePicker1(View view) {
        calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        final int day = calendar1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar1.set(year, month, dayOfMonth);
                edNSX.setText(sdf.format(calendar1.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void datePicker2(View view) {
        calendar2 = Calendar.getInstance();
        int year = calendar2.get(Calendar.YEAR);
        int month = calendar2.get(Calendar.MONTH);
        final int day = calendar2.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar2.set(year, month, dayOfMonth);
                edHSD.setText(sdf.format(calendar2.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

}