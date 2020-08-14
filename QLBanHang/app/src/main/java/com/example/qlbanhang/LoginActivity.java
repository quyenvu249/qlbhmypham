package com.example.qlbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //EditText edEmail, edPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox chkpass;
    private FirebaseAuth mAuth;
    TextInputEditText edEmail,edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Đăng nhập");
        setContentView(R.layout.activity_login);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        chkpass = findViewById(R.id.checkBox);

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

        sharedPreferences = getSharedPreferences("Shared", MODE_PRIVATE);
        edEmail.setText(sharedPreferences.getString("username", ""));
        edPassword.setText(sharedPreferences.getString("password", ""));

        mAuth = FirebaseAuth.getInstance();

    }


    public void logIn(View view) {
        String tk = edEmail.getText().toString();
        String mk = edPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(tk, mk)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công",
                                    Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("username", edEmail.getText().toString());
                                    intent.putExtra("password", edPassword.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                            }, 500);

                            if (chkpass.isChecked()) {
                                editor = sharedPreferences.edit();
                                editor.putString("username", edEmail.getText().toString().trim());
                                editor.putString("password", edPassword.getText().toString().trim());
                                Toast.makeText(LoginActivity.this, "Đã lưu mật khẩu", Toast.LENGTH_SHORT).show();
                                editor.commit();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, kiểm tra lại email và password",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void checkPass(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String ten = edEmail.getText().toString();
        String mk = edPassword.getText().toString();
        boolean check = chkpass.isChecked();
        if (!check) {
            editor.clear();
        } else {
            editor.putString("username", ten);
            editor.putString("password", mk);
            editor.putBoolean("save", check);
        }
        editor.commit();
    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);

    }
}