package com.example.qlbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddBrandActivity extends AppCompatActivity {
    ImageView imgTH;
    EditText edTenTH;
    Button btnCamera, btnThemTH, btnImage;
    int REQUEST_CODE_IMAGE = 100;
    int REQUEST_CODE_IMAGE_STORAGE = 200;
    Bitmap bitmap;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference mData;
    String keyID;
    StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);
        setTitle("Thêm Thương Hiệu");
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

        mData = FirebaseDatabase.getInstance().getReference("Brands");

        storageRef = storage.getReferenceFromUrl("gs://qlbh-e6609.appspot.com");


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePicture();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        btnThemTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBrand();
            }
        });

    }

    private void addBrand() {
        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("imageTH" + calendar.getTimeInMillis() + ".png");
        imgTH.setDrawingCacheEnabled(true);
        imgTH.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgTH.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AddBrandActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        Toast.makeText(AddBrandActivity.this, "Thành công", Toast.LENGTH_LONG).show();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                keyID = mData.push().getKey();
                                Brand thuongHieu = new Brand(keyID, edTenTH.getText().toString(), uri.toString());
                                final String linkAnh = uri.toString();
                                mData.child(keyID).setValue(thuongHieu, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Log.d("key", keyID);
                                        if (databaseError == null) {
                                            Toast.makeText(AddBrandActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(AddBrandActivity.this, BrandActivity.class);
                                            intent.putExtra("id", keyID);
                                            intent.putExtra("brLink", linkAnh);
                                            intent.putExtra("brName", edTenTH.getText().toString());
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(AddBrandActivity.this, "Lỗi!!!" + databaseError, Toast.LENGTH_LONG).show();
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
        btnCamera = findViewById(R.id.btnCamera);
        btnImage = findViewById(R.id.btnImage);
        imgTH = findViewById(R.id.imgTH);
        edTenTH = findViewById(R.id.edTenTH);
        btnThemTH = findViewById(R.id.btnThemTH);
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
            imgTH.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_CODE_IMAGE_STORAGE && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgTH.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}