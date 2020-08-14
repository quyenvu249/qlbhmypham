package com.example.qlbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo.DetailedState;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BillActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Button btnPicDate, btnAddHD, btnHDCT;
    EditText edTenHD, edTenKH, edTenNV, edDate, edTongTien;
    DatabaseReference mData;
    String billID;
    BillAdapter adapter;
    ListView lvBill;
    List<Bill> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hóa đơn");
        setContentView(R.layout.activity_bill);

        AnhXa();

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

        mData = FirebaseDatabase.getInstance().getReference("Bills");

        final Intent intent = getIntent();
        edTenNV.setText(intent.getStringExtra("username"));

        list = new ArrayList<>();

        adapter = new BillAdapter(BillActivity.this, list, R.layout.item_bill);
        lvBill.setAdapter(adapter);
        LoadData();

        btnAddHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billID = mData.push().getKey();
                Bill bill = new Bill(billID, edTenHD.getText().toString(), edTenKH.getText().toString(), edTenNV.getText().toString(), edDate.getText().toString(), Double.parseDouble(edTongTien.getText().toString()));
                mData.child(billID).setValue(bill, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(BillActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
                            Intent intent1 = new Intent(BillActivity.this, DetailBillActivity.class);
                            intent1.putExtra("billName", edTenHD.getText().toString());
                            startActivity(intent1);
                        } else {
                            Toast.makeText(BillActivity.this, "Lỗi!!!" + databaseError, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        btnHDCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this, DetailBillActivity.class));
            }
        });


    }

    private void LoadData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

            mData.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Bill bill = dataSnapshot.getValue(Bill.class);
                    list.add(new Bill(bill.billID, bill.billName, bill.billCus, bill.billStaff, bill.billTime, bill.billTotal));
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

    private void AnhXa() {
        edTenHD = findViewById(R.id.edTenHD);
        edTenKH = findViewById(R.id.edTenKH);
        edTenNV = findViewById(R.id.edTenNV);
        edDate = findViewById(R.id.edDate);
        edTongTien = findViewById(R.id.edTongTien);
        btnPicDate = findViewById(R.id.btnPicDate);
        btnAddHD = findViewById(R.id.btnThemHD);
        btnHDCT = findViewById(R.id.btnHDCT);
        lvBill = findViewById(R.id.lvHD);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
        setDate(cal);
    }


    private void setDate(Calendar calendar) {
        edDate.setText(sdf.format(calendar.getTime()));
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }

    public void datePicker(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "date");
    }
}
