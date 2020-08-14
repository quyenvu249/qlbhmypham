package com.example.qlbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    EditText edTenSP, edNSX, edHSD, edGiaNhap, edMoTa;
    Spinner spinner;
    Button btnPicNSX, btnChoosePic, btnThemSP;
    Button btnCamera;
    ImageView imgSP;
    Bitmap bitmap;
    DatabaseReference mData;
    int REQUEST_CODE_IMAGE = 100;
    int REQUEST_CODE_IMAGE_STORAGE = 200;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String keyID;
    StorageReference storageRef;
    List<Brand> brList;
    Calendar calendar1, calendar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setTitle("Thêm Sản Phẩm");
        AnhXa();
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
        DatabaseReference brRoot = FirebaseDatabase.getInstance().getReference("Brands");
        brRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String brName = areaSnapshot.child("brName").getValue(String.class);
                    final String[] nameList = {brName};
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AddProductActivity.this, android.R.layout.simple_spinner_item, nameList);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(areasAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mData = FirebaseDatabase.getInstance().getReference("Products");
        storageRef = storage.getReferenceFromUrl("gs://qlbh-e6609.appspot.com");

        btnChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicture();
            }
        });

        btnThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void addProduct() {
        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("imageSP" + calendar.getTimeInMillis() + "png");
        imgSP.setDrawingCacheEnabled(true);
        imgSP.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgSP.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(AddProductActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                keyID = mData.push().getKey();
                                Product product = new Product(uri.toString(), keyID, edTenSP.getText().toString(), String.valueOf(spinner.getSelectedItem()), edNSX.getText().toString(), edHSD.getText().toString(), edMoTa.getText().toString(), Double.parseDouble(edGiaNhap.getText().toString()), Double.parseDouble(edGiaNhap.getText().toString()) * 1.3);
                                final String linkAnh = uri.toString();

                                mData.child(keyID).setValue(product, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Toast.makeText(AddProductActivity.this, "Thêm dữ liệu thành công", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("prID", keyID);
                                            bundle.putString("prLink", linkAnh);
                                            bundle.putString("prName", edTenSP.getText().toString());
                                            bundle.putString("NSX", edNSX.getText().toString());
                                            bundle.putString("HSD", edHSD.getText().toString());
                                            bundle.putString("prDes", edMoTa.getText().toString());
                                            bundle.putDouble("imPrice", Double.parseDouble(edGiaNhap.getText().toString()));
                                            bundle.putDouble("exPrice", Double.parseDouble(edGiaNhap.getText().toString()) * 1.3);
                                            intent.putExtra("bdPr", bundle);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(AddProductActivity.this, "Lỗi!!!" + databaseError, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });

                    }
                }


            }
        });

    }

    private void AnhXa() {
        edTenSP = findViewById(R.id.edTenSP);
        edNSX = findViewById(R.id.edNSX);
        edHSD = findViewById(R.id.edHSD);
        edGiaNhap = findViewById(R.id.edGiaNhap);
        edMoTa = findViewById(R.id.edMoTa);
        spinner = findViewById(R.id.spPrBr);
        btnPicNSX = findViewById(R.id.btnPicNSX);
        btnCamera = findViewById(R.id.btnCamera);
        btnChoosePic = findViewById(R.id.btnImage);
        btnThemSP = findViewById(R.id.btnThemSP);
        imgSP = findViewById(R.id.imgSP);
    }


    private void choosePicture() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CODE_IMAGE_STORAGE);
    }

    //xử lý chụp hình
    private void capturePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_IMAGE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            //xử lí h.ả lúc chụp
            bitmap = (Bitmap) data.getExtras().get("data");
            imgSP.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_CODE_IMAGE_STORAGE && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgSP.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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