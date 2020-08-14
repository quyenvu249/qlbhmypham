package com.example.qlbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DetailBillActivity extends AppCompatActivity {
    EditText edTenHD, edTenSP, edSL, edĐG, edGC, edSale;
    TextView tvTT;
    Button btnThemHDCT, btnTT;
    String dtBillID;
    double thanhtien;
    DatabaseReference mData;
    List<DetailBill> list;
    DetailBillAdapter adapter;
    ListView lvList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);
        setTitle("Hóa đơn chi tiết");

        edTenHD = findViewById(R.id.edTenHD);
        edTenSP = findViewById(R.id.edTenSP);
        edSL = findViewById(R.id.edSL);
        edSale = findViewById(R.id.edSale);
        edĐG = findViewById(R.id.edĐG);
        edGC = findViewById(R.id.edGC);
        tvTT = findViewById(R.id.tvTT);
        btnThemHDCT = findViewById(R.id.btnThemHDCT);
        btnTT = findViewById(R.id.btnTT);
        lvList = findViewById(R.id.lvHDCT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
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
        edTenHD.setText(intent.getStringExtra("billName"));

        mData = FirebaseDatabase.getInstance().getReference("DetailBills");

        list = new ArrayList<>();
        adapter = new DetailBillAdapter(DetailBillActivity.this, list, R.layout.item_detailbill);
        lvList.setAdapter(adapter);

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBillActivity.this);
                builder.setMessage("Xóa hóa đơn chi tiết?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        mData.child(list.get(i).id).removeValue();
                        Toast.makeText(DetailBillActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        LoadData();

        btnTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thanhtien = Integer.parseInt(edSL.getText().toString()) * Double.parseDouble(edĐG.getText().toString()) - Integer.parseInt(edSL.getText().toString()) * Double.parseDouble(edĐG.getText().toString()) * Double.parseDouble(edSale.getText().toString());
                tvTT.setText(String.valueOf(thanhtien));
            }
        });

        btnThemHDCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtBillID = mData.push().getKey();
                DetailBill dtbill = new DetailBill(dtBillID, edTenHD.getText().toString(), edTenSP.getText().toString(), Integer.parseInt(edSL.getText().toString()), Double.parseDouble(edĐG.getText().toString()), Double.parseDouble(edSale.getText().toString()), Double.parseDouble(tvTT.getText().toString()), edGC.getText().toString());
                mData.child(dtBillID).setValue(dtbill, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(DetailBillActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetailBillActivity.this, "Lỗi!!!" + databaseError, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void LoadData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

            mData.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DetailBill detailBill = dataSnapshot.getValue(DetailBill.class);
                    list.add(new DetailBill(detailBill.id, detailBill.billName, detailBill.billPr, detailBill.account, detailBill.unitPrice, detailBill.sale, detailBill.totalAmount, detailBill.note));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 10);
        }
    }
}